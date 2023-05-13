package com.example.heartcare.tablayoutstatistics;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class TabLayoutStatisticsAdapter extends FragmentStatePagerAdapter {

    public TabLayoutStatisticsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
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
                return "Day";
            case 1:
                return "Week";
            default:
                return "Day";
        }
    }
}
