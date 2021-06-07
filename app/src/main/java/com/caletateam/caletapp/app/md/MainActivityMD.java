package com.caletateam.caletapp.app.md;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

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
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivityMD extends AppCompatActivity {
    HorizontalScrollView babylist;
    List<BabyModel> babys;
    LinearLayout linearbabys;
    TabLayout tabLayout; //= findViewById(R.id.tabs);
    ViewPager2 viewPager2; //= findViewById(R.id.view_pager);
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
        viewPager2 = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //tab.setText("Tab " + (position + 1));
                        Log.e("POSITION",position+"");
                        if (position==0) {
                            tab.setText("Summary");
                            tab.setIcon(R.drawable.summary);
                        }
                         if (position==1) {
                             tab.setText("Monitoring");
                             tab.setIcon(R.drawable.monitoring);
                         }
                        if (position==2) {
                            tab.setText("Logs");
                            tab.setIcon(R.drawable.logs);
                        }
                        if(position ==3) {
                            tab.setText("Streaming");
                            tab.setIcon(R.drawable.streaming);
                        }
                        };


                }).attach();

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
            int finalI = i;
            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedBaby(finalI);
                }
            });
        }
    }

    public void selectedBaby(int position){
        Log.e("SELECCIONADO",position+"");
    }
}