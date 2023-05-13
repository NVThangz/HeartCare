package com.example.heartcare.tablayoutstatistics;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.heartcare.R;
import com.example.heartcare.activity.HeartRateStatistics;
import com.example.heartcare.activity.MeasureHeartRate;

public class TabLayoutStatisticsAdapter extends FragmentStatePagerAdapter {
    private Context activity;

    public TabLayoutStatisticsAdapter(@NonNull FragmentManager fm, int behavior, Context activity) {
        super(fm, behavior);
        this.activity = activity;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new DayFragment();
            case 1:
                return new WeekFragment();
            default:
                return new DayFragment();
        }
    }

    @Override
    public int getCount() {
        return 2; // Total fragments
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return activity.getResources().getString(R.string.day);
            case 1:
                return activity.getResources().getString(R.string.week);
            default:
                return activity.getResources().getString(R.string.day);
        }
    }
}
