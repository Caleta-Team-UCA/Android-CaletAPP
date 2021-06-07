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

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity)
    {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position) {
            case 0:
                return  new summary();
            case 1:
                return  new monitoring();
            case 2:
                return  new logs();
            default:
                return new streamming();
        }
    }
    @Override
    public int getItemCount() {return 4; }
}