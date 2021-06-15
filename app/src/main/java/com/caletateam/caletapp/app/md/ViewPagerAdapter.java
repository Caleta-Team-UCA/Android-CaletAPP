package com.caletateam.caletapp.app.md;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.caletateam.caletapp.app.md.fragments.logs;
import com.caletateam.caletapp.app.md.fragments.monitoring;
import com.caletateam.caletapp.app.md.fragments.streamming;
import com.caletateam.caletapp.app.md.fragments.summary;

public class ViewPagerAdapter extends FragmentStateAdapter {
    summary summary;
    monitoring monitoring;
    streamming streaming;
    logs logs;

    public com.caletateam.caletapp.app.md.fragments.summary getSummary() {
        return summary;
    }

    public void setSummary(com.caletateam.caletapp.app.md.fragments.summary summary) {
        this.summary = summary;
    }

    public com.caletateam.caletapp.app.md.fragments.monitoring getMonitoring() {
        return monitoring;
    }

    public void setMonitoring(com.caletateam.caletapp.app.md.fragments.monitoring monitoring) {
        this.monitoring = monitoring;
    }

    public streamming getStreaming() {
        return streaming;
    }

    public void setStreaming(streamming streaming) {
        this.streaming = streaming;
    }

    public com.caletateam.caletapp.app.md.fragments.logs getLogs() {
        return logs;
    }

    public void setLogs(com.caletateam.caletapp.app.md.fragments.logs logs) {
        this.logs = logs;
    }

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                summary = new summary();
                return summary;
            case 1:
                monitoring= new monitoring();
                return monitoring;
            case 2:
                logs=  new logs();
                return logs;
            default:
                streaming= new streamming();
                return streaming;
        }
    }
    @Override
    public int getItemCount() {return 4; }
}