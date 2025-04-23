package com.test0423;

import android.os.Bundle;
import android.util.Log;

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

//        javaExam01();
        javaExam02();
        System.out.println("PI의 값 : "+Math.PI);

    }

    //자바 문법 예제 01
    public void javaExam01(){
        int var1 = 10;
        float var2 = 10.1f;
        double var3 = 10.2;
        String var4 = "안";

        System.out.println(var1);
        System.out.println(var2);
        System.out.println(var3);
        System.out.println(var4);

        Log.v("var1", String.valueOf(var1));
        Log.v("var2", String.valueOf(var2));
        Log.v("var3", String.valueOf(var3));
        Log.v("var4", var4);
    }

    //자바 문법 예제 02
    public void javaExam02(){

        Car myCar1 = new Car("빨강", 0);
//        myCar1.color = "빨강";
//        myCar1.speed = 0;

        Car myCar2 = new Car("파랑", 0);
//        myCar2.color = "파랑";
//        myCar2.speed = 150;

        myCar1.upSpeed(50);
        System.out.println("자동차1의 색상은 "+myCar1.getColor()+"이며, 속도는 "+myCar1.getSpeed()+"km 이다.");

    }

}