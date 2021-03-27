package com.toni.gwftest.view;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.buffer.adaptablebottomnavigation.adapter.FragmentStateAdapter;

/**
 * Adapter for Bottom Navigation Bar
 */
public class ViewSwapperAdapter extends FragmentStateAdapter {

    private static final int INDEX_METERS = 0;
    private static final int INDEX_MAPS = 1;
    private static final int INDEX_PROFILE = 2;

    public ViewSwapperAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case INDEX_METERS:
                return MetersFragment.newInstance();
            case INDEX_MAPS:
                return MapFragment.newInstance();
            case INDEX_PROFILE:
                return ProfileFragment.newInstance();
        }
        return MetersFragment.newInstance();
    }

    @Override
    public int getCount() {
        return 3;
    }
}