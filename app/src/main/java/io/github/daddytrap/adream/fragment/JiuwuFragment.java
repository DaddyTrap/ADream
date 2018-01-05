package io.github.daddytrap.adream.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.daddytrap.adream.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class JiuwuFragment extends ADFragment {

    public static JiuwuFragment newInstance() {

        Bundle args = new Bundle();

        JiuwuFragment fragment = new JiuwuFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public JiuwuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState, 1, R.layout.fragment_jiuwu);
    }

}
