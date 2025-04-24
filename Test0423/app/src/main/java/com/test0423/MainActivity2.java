package com.test0423;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TextView tv_title = findViewById(R.id.tv_title);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);

        RadioButton rbtn1 = findViewById(R.id.rbtn1);
        RadioButton rbtn2 = findViewById(R.id.rbtn2);
        RadioButton rbtn3 = findViewById(R.id.rbtn3);

        CheckBox checkBox1 = findViewById(R.id.checkBox1);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId==R.id.rbtn1) {
                    Toast.makeText(getApplicationContext(), "First World", Toast.LENGTH_SHORT).show();
                    tv_title.setText("첫 번째를 선택하였습니다.");

                } else if (checkedId==R.id.rbtn2) {
                    Toast.makeText(getApplicationContext(), "Second World", Toast.LENGTH_SHORT).show();
                    tv_title.setText("두 번째를 선택하였습니다.");

                } else {
                    Toast.makeText(getApplicationContext(), "Third World", Toast.LENGTH_SHORT).show();
                    tv_title.setText("세 번째를 선택하였습니다.");

                }

//                Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();
            }
        });

        checkBox1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( ((CheckBox)v).isChecked() ) {
                    Toast.makeText(getApplicationContext(), "동의 하였습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "비동의 시 서비스 이용이 불가합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}