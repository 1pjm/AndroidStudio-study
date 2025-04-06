package com.test0319;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity6 extends AppCompatActivity {

    private ImageView imageView;
    private Button btn1;
    private ScrollView scrollView;

    BitmapDrawable bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main6);

        imageView = findViewById(R.id.imageView);
        btn1 = findViewById(R.id.btn1);
        scrollView = findViewById(R.id.scrollView);

        // 가로에도 스크롤바를 생성하게 설정
        scrollView.setHorizontalScrollBarEnabled(true);

        // 첫번째 이미지 세팅
        Resources res = getResources();
        bitmap = (BitmapDrawable)res.getDrawable(R.drawable.sunset);

        int bitmapWidth = bitmap.getIntrinsicWidth();
        int bitmapHeight = bitmap.getIntrinsicHeight();
        Log.v("bitmapWidth : ", String.valueOf(bitmapWidth)+"px");
        Log.v("bitmapHeight : ", String.valueOf(bitmapHeight)+"px");

        imageView.setImageDrawable(bitmap);
        imageView.getLayoutParams().width = bitmapWidth;
        imageView.getLayoutParams().height = bitmapHeight;

        // 버튼을 눌렀을 때 두번째 이미지 세팅
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Resources res = getResources();
                bitmap = (BitmapDrawable)res.getDrawable(R.drawable.puffins);
                int bitmapWidth = bitmap.getIntrinsicWidth();
                int bitmapHeight = bitmap.getIntrinsicHeight();
                Log.v("bitmapWidth : ", String.valueOf(bitmapWidth)+"px");
                Log.v("bitmapHeight : ", String.valueOf(bitmapHeight)+"px");

                imageView.setImageDrawable(bitmap);
                imageView.getLayoutParams().width = bitmapWidth;
                imageView.getLayoutParams().height = bitmapHeight;
            }
        });

    }
}