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
public class MiaobiFragment extends ADFragment {

    public static MiaobiFragment newInstance() {

        Bundle args = new Bundle();

        MiaobiFragment fragment = new MiaobiFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MiaobiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState, 2, R.layout.fragment_miaobi);
    }

}
