package com.test0309_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //리스너 버튼
        Button btn2=findViewById(R.id.btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "안녕하세요. 버튼2 입니다.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //함수 버튼
    public void sendBtn1(View v) {
        Toast.makeText(getApplicationContext(), "안녕하세요. 버튼1 입니다.", Toast.LENGTH_SHORT).show();
    }

    //네이버 웹 이동
    public void onBtn3(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.naver.com"));
        startActivity(intent);
    }

    //전화걸기
    public void onBtn4(View v) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel: 010-1234-5678"));
        startActivity(intent);
    }

}