package com.planner;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
// ← RecyclerView는 android.widget이 아니라 androidx 패키지입니다.
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView      tvYear, tvMonth;
    private EditText      etMonthlyTodo;
    private CalendarView  calendarView;
    private SharedPreferences prefs;
    private int           currentYear, currentMonth;
    private Button        btnSaveMonthly, btnEditMonthly, btnClearMonthly;
    private ImageButton   btnHome;

    // 날짜 지정용 보관 변수 (package-private 으로 변경)
    TodoItem pendingMoveItem;
    int      pendingOrigY, pendingOrigM, pendingOrigD;

    private AlertDialog currentTodoDialog;
    private boolean     calendarInitialized = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prefs = getSharedPreferences("planner_prefs", MODE_PRIVATE);
        setContentView(R.layout.activity_main);

        // 뷰 바인딩
        tvYear        = findViewById(R.id.tvYear);
        tvMonth       = findViewById(R.id.tvMonth);
        etMonthlyTodo = findViewById(R.id.etMonthlyTodo);
        calendarView  = findViewById(R.id.calendarView);
        btnSaveMonthly  = findViewById(R.id.btnSaveMonthly);
        btnEditMonthly  = findViewById(R.id.btnEditMonthly);
        btnClearMonthly = findViewById(R.id.btnClearMonthly);
        btnHome         = findViewById(R.id.btnHome);

        // 오늘 연/월 표시 + CalendarView 초기화 (리스너는 등록 전)
        goToToday();

        // 그 다음에 날짜 클릭 리스너 등록
        calendarView.setOnDateChangeListener((view, y, mZero, d) -> {
            if (!calendarInitialized) {
                calendarInitialized = true;
                return;
            }
            handleDateSelected(y, mZero, d);
        });

        // 월간 할일 로드
        loadMonthlyTodo();

        // 월간 할일 버튼 세팅
        btnSaveMonthly .setOnClickListener(v -> {
            String key = currentYear + "-" + currentMonth + "-monthly";
            prefs.edit().putString(key, etMonthlyTodo.getText().toString()).apply();
            Toast.makeText(this, "이 달의 할 일 저장 완료", Toast.LENGTH_SHORT).show();
            etMonthlyTodo.setEnabled(false);
            btnSaveMonthly.setEnabled(false);
            btnEditMonthly.setEnabled(true);
        });
        btnEditMonthly .setOnClickListener(v -> {
            etMonthlyTodo.setEnabled(true);
            btnSaveMonthly.setEnabled(true);
            btnEditMonthly.setEnabled(false);
            etMonthlyTodo.requestFocus();
        });
        btnClearMonthly.setOnClickListener(v -> {
            String key = currentYear + "-" + currentMonth + "-monthly";
            prefs.edit().remove(key).apply();
            etMonthlyTodo.setText("");
            etMonthlyTodo.setEnabled(true);
            btnSaveMonthly.setEnabled(true);
            btnEditMonthly.setEnabled(false);
            Toast.makeText(this, "이 달의 할 일 초기화됨", Toast.LENGTH_SHORT).show();
        });

        // 연/월 선택 다이얼로그
        tvYear .setOnClickListener(v -> showYearPicker());
        tvMonth.setOnClickListener(v -> showMonthPicker());

        // 홈 버튼: 오늘로 돌아가기
        btnHome .setOnClickListener(v -> goToToday());
    }

    /** 실제 사용자 터치로 날짜 선택 시만 실행 */
    private void handleDateSelected(int year, int monthZero, int day) {
        if (pendingMoveItem != null) {
            List<TodoItem> list = loadTodosForDate(year, monthZero + 1, day);
            list.add(pendingMoveItem);
            saveTodosForDate(year, monthZero + 1, day, list);
            pendingMoveItem = null;
        }
        showTodoDialog(year, monthZero + 1, day);
    }

    // 날짜별 Todo 목록 로드
    public List<TodoItem> loadTodosForDate(int year, int month, int day) {
        String key = String.format("todos_%04d_%02d_%02d", year, month, day);
        String json = prefs.getString(key, null);
        Log.d("DEBUG", "LOAD " + key + " ← " + json);

        // ① 키가 없으면 빈 리스트 반환
        if (json == null) {
            return new ArrayList<>();
        }
        // ② 빈 배열만 남아 있으면 키도 제거하고 빈 리스트 반환
        if (json.equals("[]")) {
            prefs.edit().remove(key).apply();
            return new ArrayList<>();
        }

        // ③ 실제 JSON 파싱 로직
        List<TodoItem> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONArray item = arr.getJSONArray(i);
                TodoItem ti = new TodoItem(item.getString(0));
                ti.setDone(item.getBoolean(1));
                list.add(ti);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 날짜별 Todo 목록 저장
    public void saveTodosForDate(int year, int month, int day, List<TodoItem> list) {
        String key = String.format("todos_%04d_%02d_%02d", year, month, day);
        JSONArray arr = new JSONArray();
        for (TodoItem ti : list) {
            JSONArray item = new JSONArray();
            item.put(ti.getText());
            item.put(ti.isDone());
            arr.put(item);
        }
        String json = arr.toString();
        Log.d("DEBUG", "SAVE " + key + " → " + json);
        prefs.edit().putString(key, json).apply();
    }

    private void loadMonthlyTodo() {
        String key = currentYear + "-" + currentMonth + "-monthly";
        String saved = prefs.getString(key, "");
        etMonthlyTodo.setText(saved);
        boolean has = !saved.isEmpty();
        etMonthlyTodo.setEnabled(!has);
        btnSaveMonthly.setEnabled(!has);
        btnEditMonthly.setEnabled(has);
    }

    // 오늘 날짜의 달력 화면으로 돌아가기
    private void goToToday() {
        Calendar now = Calendar.getInstance();
        currentYear  = now.get(Calendar.YEAR);
        currentMonth = now.get(Calendar.MONTH) + 1;
        // 헤더 텍스트 갱신
        tvYear.setText(currentYear + "년");
        tvMonth.setText(currentMonth + "월");
        // CalendarView 를 오늘로 이동
        calendarView.setDate(now.getTimeInMillis(), true, true);
        // 이 달의 할일도 다시 로드
        loadMonthlyTodo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Calendar now = Calendar.getInstance();
        int year  = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        if (year != currentYear || month != currentMonth) {
            currentYear  = year;
            currentMonth = month;
            tvYear.setText(currentYear + "년");
            tvMonth.setText(currentMonth + "월");
            Calendar c = Calendar.getInstance();
            c.set(currentYear, currentMonth - 1, 1);
            calendarView.setDate(c.getTimeInMillis(), true, true);
            loadMonthlyTodo();
        }
    }

    private void showYearPicker() {
        NumberPicker np = new NumberPicker(this);
        np.setMinValue(1970); np.setMaxValue(2100); np.setValue(currentYear);
        new AlertDialog.Builder(this)
                .setTitle("연도 선택")
                .setView(np)
                .setPositiveButton("확인", (d,w) -> {
                    currentYear = np.getValue();
                    tvYear.setText(currentYear + "년");
                    Calendar c = Calendar.getInstance();
                    c.set(currentYear, currentMonth - 1, 1);
                    calendarView.setDate(c.getTimeInMillis(), true, true);
                    loadMonthlyTodo();
                }).setNegativeButton("취소",null).show();
    }

    private void showMonthPicker() {
        NumberPicker np = new NumberPicker(this);
        np.setMinValue(1); np.setMaxValue(12); np.setValue(currentMonth);
        new AlertDialog.Builder(this)
                .setTitle("월 선택")
                .setView(np)
                .setPositiveButton("확인", (d,w) -> {
                    currentMonth = np.getValue();
                    tvMonth.setText(currentMonth + "월");
                    Calendar c = Calendar.getInstance();
                    c.set(currentYear, currentMonth - 1, 1);
                    calendarView.setDate(c.getTimeInMillis(), true, true);
                    loadMonthlyTodo();
                }).setNegativeButton("취소",null).show();
    }

    private void showTodoDialog(int year, int month, int day) {
        // 1) 다이얼로그 뷰 inflate
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_todo_list, null);
        TextView tvDate    = dialogView.findViewById(R.id.tvDialogDate);
        ImageButton btnAdd = dialogView.findViewById(R.id.btnAddTodo);
        RecyclerView rv    = dialogView.findViewById(R.id.rvTodoList);
        Button btnSelectAll    = dialogView.findViewById(R.id.btnSelectAll);
        Button btnDeleteAll    = dialogView.findViewById(R.id.btnDeleteAll);
        Button btnDeleteSelect = dialogView.findViewById(R.id.btnDeleteSelected);

        tvDate.setText(month + "월 " + day + "일 (" +
                getDayOfWeekString(year, month, day) + ")");

        // 2) RecyclerView / Adapter 세팅
        List<TodoItem> todos = loadTodosForDate(year, month, day);
        TodoAdapter adapter = new TodoAdapter(
                this, todos, year, month, day,
                () -> saveTodosForDate(year, month, day, todos)
        );
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

        // 3) + 버튼: 다이얼로그로 입력받고 추가
        btnAdd.setOnClickListener(v -> {
            final EditText input = new EditText(this);
            input.setHint("할 일을 입력하세요");
            new AlertDialog.Builder(this)
                    .setTitle("새 할 일 추가")
                    .setView(input)
                    .setPositiveButton("추가", (d,i) -> {
                        String text = input.getText().toString().trim();
                        if (!text.isEmpty()) {
                            adapter.add(text);
                            rv.scrollToPosition(adapter.getItemCount()-1);
                        }
                    })
                    .setNegativeButton("취소", null)
                    .show();
        });

        // 4) 하단 버튼들 연결
        btnSelectAll   .setOnClickListener(v -> adapter.selectAll(true));
        btnDeleteAll   .setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("전체 삭제")
                .setMessage("정말 모든 할 일을 삭제하겠습니까?")
                .setPositiveButton("삭제", (d,i) -> adapter.deleteAll())
                .setNegativeButton("취소", null)
                .show());
        btnDeleteSelect.setOnClickListener(v -> new AlertDialog.Builder(this)
                .setTitle("선택 삭제")
                .setMessage("선택된 할 일을 삭제하겠습니까?")
                .setPositiveButton("삭제", (d,i) -> adapter.deleteSelected())
                .setNegativeButton("취소", null)
                .show());

        // 5) **한 번만** 다이얼로그 띄우기
        currentTodoDialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();
        currentTodoDialog.show();
    }


    public void dismissCurrentTodoDialog() {
        if (currentTodoDialog != null && currentTodoDialog.isShowing()) {
            currentTodoDialog.dismiss();
        }
    }

    // 요일 문자열 리턴 헬퍼
    private String getDayOfWeekString(int y, int m, int d) {
        Calendar c = Calendar.getInstance();
        c.set(y, m-1, d);
        switch (c.get(Calendar.DAY_OF_WEEK)) {
            case Calendar.SUNDAY:    return "일";
            case Calendar.MONDAY:    return "월";
            case Calendar.TUESDAY:   return "화";
            case Calendar.WEDNESDAY: return "수";
            case Calendar.THURSDAY:  return "목";
            case Calendar.FRIDAY:    return "금";
            default:                 return "토";
        }
    }

}