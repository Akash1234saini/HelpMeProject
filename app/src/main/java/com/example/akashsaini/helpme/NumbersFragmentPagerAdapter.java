package com.example.akashsaini.helpme;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class NumbersFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    Context mContext;
    private final String[] tabTittle = new String[] {"Quick Access", "Recent"};

    NumbersFragmentPagerAdapter(FragmentManager fm, Activity context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new QuickAccessFragment();
        else
            return new RecentFragment();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return tabTittle[position];
//    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
