package io.github.daddytrap.adream.adapter;

/**
 * Created by DaddyTrapC on 2018/1/4.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.LinkedList;
import java.util.List;

import io.github.daddytrap.adream.activity.MainActivity;
import io.github.daddytrap.adream.fragment.DemoFragment;

/**
 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class DemoPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;

    public DemoPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new LinkedList<>();
        for (int i = 0; i < 3; ++i) {
            fragments.add(DemoFragment.newInstance(i + 1));
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DemoFragment (defined as a static inner class below).
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "SECTION 1";
            case 1:
                return "SECTION 2";
            case 2:
                return "SECTION 3";
        }
        return null;
    }
}