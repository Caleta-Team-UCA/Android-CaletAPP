package com.caletateam.caletapp.app.md;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.caletateam.caletapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Monitoring extends AppCompatActivity {
    TabLayout tabLayout; //= findViewById(R.id.tabs);
    ViewPager2 viewPager2; //= findViewById(R.id.view_pager);
    ViewPagerAdapterMonitoring adapter;
    String event;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.appcolor)));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.appcolor));
        }
        try{
            event = getIntent().getStringExtra("event");
        }catch(Exception e){

        }
        viewPager2 = findViewById(R.id.pager);
        tabLayout = findViewById(R.id.tab_layout);
        adapter = new ViewPagerAdapterMonitoring(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        //tab.setText("Tab " + (position + 1));
                        //Log.e("POSITION",position+"");
                        if (position==0) {
                            tab.setText("Real Time");
                            tab.setIcon(R.drawable.realtime);
                        }
                        if (position==1) {
                            tab.setText("Log");
                            tab.setIcon(R.drawable.log);
                        }

                    };


                }).attach();
    }
}