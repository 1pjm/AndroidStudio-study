package com.planner;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private TextView tvYear, tvMonth;
    private EditText etMonthlyTodo;
    private CalendarView calendarView;
    private SharedPreferences prefs;
    private int currentYear, currentMonth;
    private Button btnSaveMonthly, btnEditMonthly, btnClearMonthly;
    private ImageButton btnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("planner_prefs", MODE_PRIVATE);

        tvYear        = findViewById(R.id.tvYear);
        tvMonth       = findViewById(R.id.tvMonth);
        etMonthlyTodo = findViewById(R.id.etMonthlyTodo);
        calendarView  = findViewById(R.id.calendarView);
        btnSaveMonthly  = findViewById(R.id.btnSaveMonthly);
        btnEditMonthly  = findViewById(R.id.btnEditMonthly);
        btnClearMonthly = findViewById(R.id.btnClearMonthly);
        btnHome = findViewById(R.id.btnHome);

        // 오늘 연/월 초기화
        Calendar now = Calendar.getInstance();
        currentYear  = now.get(Calendar.YEAR);
        currentMonth = now.get(Calendar.MONTH) + 1;
        tvYear.setText(currentYear + "년");
        tvMonth.setText(currentMonth + "월");

        // 이 달의 할일 로드
        loadMonthlyTodo();

        // 월간 할일 저장
        etMonthlyTodo.setEnabled(true);
        btnEditMonthly.setEnabled(false);

        btnSaveMonthly.setOnClickListener(v -> {
            String key = currentYear + "-" + currentMonth + "-monthly";
            prefs.edit()
                    .putString(key, etMonthlyTodo.getText().toString())
                    .apply();
            Toast.makeText(this, "이 달의 할 일 저장 완료", Toast.LENGTH_SHORT).show();
            // 저장 후에는 수정 버튼 활성화, 입력란 비활성화
            etMonthlyTodo.setEnabled(false);
            btnSaveMonthly.setEnabled(false);
            btnEditMonthly.setEnabled(true);
        });

        btnEditMonthly.setOnClickListener(v -> {
            etMonthlyTodo.setEnabled(true);
            btnSaveMonthly.setEnabled(true);
            btnEditMonthly.setEnabled(false);
            etMonthlyTodo.requestFocus();
        });

        // 비우기 버튼
        btnClearMonthly.setOnClickListener(v -> {
            String key = currentYear + "-" + currentMonth + "-monthly";
            // SharedPreferences에서 삭제
            prefs.edit().remove(key).apply();
            // 화면에서 초기화
            etMonthlyTodo.setText("");
            etMonthlyTodo.setEnabled(true);
            btnSaveMonthly.setEnabled(true);
            btnEditMonthly.setEnabled(false);
            Toast.makeText(this, "이 달의 할 일 초기화됨", Toast.LENGTH_SHORT).show();
        });

        // 년/월 선택 다이얼로그
        tvYear.setOnClickListener(v -> showYearPicker());
        tvMonth.setOnClickListener(v -> showMonthPicker());

        // 달력 날짜 선택 → 투두리스트 다이얼로그
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            showTodoDialog(year, month + 1, dayOfMonth);
        });

        // 집 버튼 클릭 시 오늘 날짜로 리셋
        btnHome.setOnClickListener(v -> goToToday());
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
        View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_todo_list, null);
        TextView tvDate = dialogView.findViewById(R.id.tvDialogDate);
        tvDate.setText(month + "월 " + day + "일");

        new AlertDialog.Builder(this)
                .setView(dialogView)
                .show();
    }
}
