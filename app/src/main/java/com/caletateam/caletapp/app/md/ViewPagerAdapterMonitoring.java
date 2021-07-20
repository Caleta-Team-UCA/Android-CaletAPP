package com.caletateam.caletapp.app.md;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.caletateam.caletapp.app.md.fragments.LogMonitoring;
import com.caletateam.caletapp.app.md.fragments.RealTimeMonitoring;
import com.caletateam.caletapp.app.md.fragments.logs;
import com.caletateam.caletapp.app.md.fragments.monitoring;
import com.caletateam.caletapp.app.md.fragments.streamming;
import com.caletateam.caletapp.app.md.fragments.summary;

public class ViewPagerAdapterMonitoring extends FragmentStateAdapter {
    LogMonitoring logmonitoring;
    RealTimeMonitoring realTimeMonitoring;
    String event;
    public void setImage(byte[] buffer){

    }

    public LogMonitoring getLogmonitoring() {
        return logmonitoring;
    }

    public void setLogmonitoring(LogMonitoring logmonitoring) {
        this.logmonitoring = logmonitoring;
    }

    public RealTimeMonitoring getRealTimeMonitoring() {
        return realTimeMonitoring;
    }

    public void setRealTimeMonitoring(RealTimeMonitoring realTimeMonitoring) {
        this.realTimeMonitoring = realTimeMonitoring;
    }

    public ViewPagerAdapterMonitoring(@NonNull FragmentActivity fragmentActivity,String event)
    {
        super(fragmentActivity);
        this.event = event;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                realTimeMonitoring =  RealTimeMonitoring.newInstance(event);
                return realTimeMonitoring;
            default:
                logmonitoring = LogMonitoring.newInstance(event);
                return logmonitoring;
        }
    }
    @Override
    public int getItemCount() {return 2; }

}