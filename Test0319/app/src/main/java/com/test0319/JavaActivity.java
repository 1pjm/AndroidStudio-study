package com.test0319;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class JavaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_java);

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
}