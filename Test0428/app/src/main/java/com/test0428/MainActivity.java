package com.test0428;

import android.gesture.Gesture;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private View layer01;
    private View layer02;
    private View layer03;
    private TextView tv01;
    private GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //터치를 위한 레이어
        layer01 = findViewById(R.id.layer01);

        //제스처를 위한 레이어
        layer02 = findViewById(R.id.layer02);
        layer03 = findViewById(R.id.layer03);

        //결과값을 보여주는 텍스트 뷰
        tv01 = findViewById(R.id.tv01);

        layer01.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();
                float curX = event.getX();
                float curY = event.getY();

                if (action == event.ACTION_DOWN) {
                    println("손가락 눌림 : "+curX+", "+curY);
                } else if (action == event.ACTION_MOVE) {
                    println("손가락 움직임 : "+curX+", "+curY);
                } else if (action == event.ACTION_UP) {
                    println("손가락 뗌 : "+curX+", "+curY);
                }

                return false;

            }
        });

        detector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(@NonNull MotionEvent e) {
                println("onDown()이 호출됨");
                return false;
            }

            @Override
            public void onShowPress(@NonNull MotionEvent e) {
                println("onShowPress()이 호출됨");
            }

            @Override
            public boolean onSingleTapUp(@NonNull MotionEvent e) {
                println("onSingleTapUp()이 호출됨");
                return false;
            }

            @Override
            public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
                println("onScroll()이 호출됨 : "+distanceX+", "+distanceY);
                return false;
            }

            @Override
            public void onLongPress(@NonNull MotionEvent e) {
                println("onLongPress()이 호출됨");
            }

            @Override
            public boolean onFling(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                println("onFling()이 호출됨 : "+velocityX+", "+velocityY);
                return false;
            }
        });

        layer02.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                detector.onTouchEvent(event);
                return false;
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(getApplicationContext(), "시스템[BACK] 버튼이 눌렸습니다.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void println(String data) {
        tv01.append(data+"\n");
    }

}