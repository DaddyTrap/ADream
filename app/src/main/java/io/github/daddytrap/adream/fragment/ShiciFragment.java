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
public class ShiciFragment extends ADFragment {

    public static ShiciFragment newInstance() {

        Bundle args = new Bundle();

        ShiciFragment fragment = new ShiciFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public ShiciFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return createView(inflater, container, savedInstanceState, 0, R.layout.fragment_shici);
    }

}
