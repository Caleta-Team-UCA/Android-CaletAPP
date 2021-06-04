package com.caletateam.caletapp.app.md;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.caletateam.caletapp.R;

public class MainActivityMD extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_m_d);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appcolor)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));
        }
    }
}