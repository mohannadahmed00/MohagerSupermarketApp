package com.example.mysupermarket.adapters;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.mysupermarket.fragments.CategoryFragment;

import java.util.ArrayList;

public class PagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<String> fragments;

    public PagerAdapter(FragmentManager fm, ArrayList<String> fragments) {
        super(fm);
        this.fragments=fragments;
    }


    @NonNull
    public Fragment getItem(int position) {
        return new CategoryFragment(fragments.get(position));
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

