package io.github.daddytrap.adream.fragment;

/**
 * Created by DaddyTrapC on 2018/1/4.
 */


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import io.github.daddytrap.adream.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class DemoFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public int section_num;

    public View fragView;

    public DemoFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DemoFragment newInstance(int sectionNumber) {
        DemoFragment fragment = new DemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        fragment.section_num = sectionNumber;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        rootView.setTag(getArguments().getInt(ARG_SECTION_NUMBER) - 1);
        rootView.setVisibility(View.INVISIBLE);
        fragView = rootView;
        Log.i("INFO", "Created View " + section_num);
        return rootView;
    }
}