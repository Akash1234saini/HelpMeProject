package com.proapps.akashsaini.helpme;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class NumbersFragmentPagerAdapter extends FragmentPagerAdapter {

    private final int PAGE_COUNT = 2;
    Context mContext;
    private final String[] tabTittle = new String[] {"Government", "Public"};

    NumbersFragmentPagerAdapter(FragmentManager fm, AppCompatActivity context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new GovernmentNumbersFragment();
        else
            return new PublicNumbersFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTittle[position];
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}