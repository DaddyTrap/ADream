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
public class CommonPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<String> fragmentTitles;

    public CommonPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new LinkedList<>();
        fragmentTitles = new LinkedList<>();
    }

    public void addFragment(Fragment fragment, String name) {
        fragments.add(fragment);
        fragmentTitles.add(name);
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
        return fragmentTitles.get(position);
    }
}