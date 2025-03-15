package com.test0312;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    Button btn1;
    CheckBox checkBox1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        btn1 = findViewById(R.id.btn1);
        checkBox1 = findViewById(R.id.checkBox1);
//        checkBox1.setChecked(true);

        //버튼 클릭 함수
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
                public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "버튼입니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //체크박스 클릭 함수
        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()) {
                    Toast.makeText(getApplicationContext(), "동의하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "동의하지 않았습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}