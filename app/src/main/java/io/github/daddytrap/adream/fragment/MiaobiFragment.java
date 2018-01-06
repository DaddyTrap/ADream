package io.github.daddytrap.adream.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.activity.EditMiaobiActivity;

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

    private ImageView miaobiNewIcon;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        miaobiNewIcon = (ImageView) view.findViewById(R.id.fragment_miaobi_new_icon);
        miaobiNewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiaobiFragment.this.getActivity(), EditMiaobiActivity.class);
                startActivity(intent);
            }
        });
    }
}
