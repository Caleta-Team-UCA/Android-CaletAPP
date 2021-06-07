package com.caletateam.caletapp.app.md;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.caletateam.caletapp.R;
import com.caletateam.caletapp.app.babyList.BabyModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMD extends AppCompatActivity {
    HorizontalScrollView babylist;
    List<BabyModel> babys;
    LinearLayout linearbabys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_m_d);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appcolor)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));
        }
        babys = new ArrayList<>();
        babylist = findViewById(R.id.scrollBabys);
        linearbabys = findViewById(R.id.linearBabys);
        Log.e("AQUI","asdfasd");
        initBabys();
    }

    @Override
    protected void onStart() {
        super.onStart();
        addBabys(babys);
    }

    private void initBabys(){
        for(int i=0; i < 10;i++) {
            babys.add(new BabyModel("Baby "+i, i,R.drawable.baby));
        }
    }


    public void addBabys(List<BabyModel> babys){
        for(int i=0; i < babys.size();i++){
            View view;
            LayoutInflater inflater = (LayoutInflater)   this.getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.babyprofile, null);
            LinearLayout content= view.findViewById(R.id.babycontent);
            //if(i%2==0)
            //content.setBackgroundColor(Color.parseColor("#ff0000"));
            //else content.setBackgroundColor(Color.parseColor("#00ff00"));
            ImageView img = view.findViewById(R.id.babyimg);
            img.setImageResource((Integer) babys.get(i).getPhoto());
            TextView text = (TextView) view.findViewById(R.id.babyname);
            text.setText(babys.get(i).getName());
            linearbabys.addView(view);
        }
    }
}