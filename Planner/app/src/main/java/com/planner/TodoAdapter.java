package com.planner;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.VH> {

    public interface OnDataChanged {
        void onListChanged();
    }

    private List<TodoItem> items;
    private Context ctx;
    private OnDataChanged listener;
    private int origY, origM, origD;

    public TodoAdapter(Context ctx, List<TodoItem> items, int y, int m, int d, OnDataChanged listener) {
        this.ctx = ctx;
        this.items = items;
        this.origY = y;   // 저장
        this.origM = m;
        this.origD = d;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx)
                .inflate(R.layout.item_todo, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        // 1) 뷰에 데이터 바인딩
        TodoItem it = items.get(pos);  // 변수 it는 이 한 줄로만 선언!
        h.check.setOnCheckedChangeListener(null);
        h.check.setChecked(it.isDone());
        h.text.setText(it.getText());
        h.text.setPaintFlags(
                it.isDone()
                        ? h.text.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
                        : h.text.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG
        );
        h.check.setOnCheckedChangeListener((btn, isChecked) -> {
            it.setDone(isChecked);
            notifyItemChanged(h.getAdapterPosition());
            if (listener != null) listener.onListChanged();
        });

        // 2) 메뉴 버튼 클릭 리스너 (it를 재선언하지 않습니다)
        h.menu.setOnClickListener(v -> {
            PopupMenu pm = new PopupMenu(ctx, h.menu);
            pm.inflate(R.menu.todo_item_menu);
            pm.setOnMenuItemClickListener(mi -> {
                int id = mi.getItemId();
                MainActivity host = (MainActivity) ctx;
                Calendar c = Calendar.getInstance();
                c.set(origY, origM-1, origD);

                if (id == R.id.action_edit) {
                    showEditDialog(pos);
                }
                else if (id == R.id.action_move_tomorrow) {
                    // 1) 오늘 목록에서 제거
                    TodoItem moved = items.remove(pos);
                    notifyItemRemoved(pos);

                    // 2) 오늘 리스트 저장 (원본 날짜 key 로)
                    host.saveTodosForDate(origY, origM, origD, items);

                    // 3) 내일 날짜 계산
                    c.add(Calendar.DATE, 1);
                    int y = c.get(Calendar.YEAR);
                    int m = c.get(Calendar.MONTH) + 1;
                    int d = c.get(Calendar.DAY_OF_MONTH);

                    // 4) 내일 리스트 불러와 추가·저장
                    List<TodoItem> tomorrow = host.loadTodosForDate(y, m, d);
                    tomorrow.add(moved);
                    host.saveTodosForDate(y, m, d, tomorrow);
                }

                else if (id == R.id.action_copy_tomorrow) {
                    // 내일 날짜 계산
                    c.add(Calendar.DATE, 1);
                    int y2 = c.get(Calendar.YEAR);
                    int m2 = c.get(Calendar.MONTH) + 1;
                    int d2 = c.get(Calendar.DAY_OF_MONTH);

                    // 복제해서 추가·저장
                    TodoItem copy = new TodoItem(it.getText());
                    List<TodoItem> tomorrow2 = host.loadTodosForDate(y2, m2, d2);
                    tomorrow2.add(copy);
                    host.saveTodosForDate(y2, m2, d2, tomorrow2);
                }

                else if (id == R.id.action_move_date) {
                    host.pendingMoveItem = it;
                    host.pendingOrigY    = origY;
                    host.pendingOrigM    = origM;
                    host.pendingOrigD    = origD;
                    // 오늘 목록 저장
                    host.saveTodosForDate(origY, origM, origD, items);
                    // 다이얼로그 닫기
                    host.dismissCurrentTodoDialog();
                    return true;
                } else {
                    return false;
                }

                if (listener != null) listener.onListChanged();
                return true;
            });
            pm.show();
        });
    }


    @Override public int getItemCount() { return items.size(); }

    public void add(String text) {
        items.add(new TodoItem(text));
        notifyItemInserted(items.size()-1);
        if (listener!=null) listener.onListChanged();
    }

    public void selectAll(boolean select) {
        for (TodoItem it: items) it.setDone(select);
        notifyDataSetChanged();
        if (listener!=null) listener.onListChanged();
    }

    public void deleteAll() {
        items.clear();
        notifyDataSetChanged();
        if (listener!=null) listener.onListChanged();
    }

    public void deleteSelected() {
        for (int i = items.size() - 1; i >= 0; i--) {
            if (items.get(i).isDone()) {
                items.remove(i);
            }
        }
        notifyDataSetChanged();
        if (listener != null) listener.onListChanged();
    }


    private void showEditDialog(int pos) {
        TodoItem it = items.get(pos);
        EditText input = new EditText(ctx);
        input.setText(it.getText());
        new AlertDialog.Builder(ctx)
                .setTitle("수정")
                .setView(input)
                .setPositiveButton("확인", (d,w) -> {
                    it.setText(input.getText().toString());
                    notifyItemChanged(pos);
                    if (listener!=null) listener.onListChanged();
                })
                .setNegativeButton("취소", null)
                .show();
    }

    static class VH extends RecyclerView.ViewHolder {
        CheckBox check;
        TextView text;
        ImageView menu;
        VH(View v) {
            super(v);
            check = v.findViewById(R.id.cbTodo);
            text  = v.findViewById(R.id.tvTodo);
            menu  = v.findViewById(R.id.ivMenu);
        }
    }
}