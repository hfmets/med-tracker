package com.example.medtracker.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.medtracker.fragments.PageObjectMedsFragment;

public class PageObjectMedsFragmentAdapter extends FragmentStateAdapter {
    public PageObjectMedsFragmentAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = new PageObjectMedsFragment();
        Bundle args = new Bundle();
        args.putInt(PageObjectMedsFragment.ARG_OBJECT, position + 1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}