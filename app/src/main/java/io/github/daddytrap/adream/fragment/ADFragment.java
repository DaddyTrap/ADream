package io.github.daddytrap.adream.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.github.daddytrap.adream.R;

/**
 * Created by DaddyTrapC on 2018/1/5.
 */

public class ADFragment extends Fragment {
    public View fragView;

    protected View createView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState, int tag, int layout) {
        View ret = inflater.inflate(layout, container, false);
        ret.setTag(tag);
        ret.setVisibility(View.INVISIBLE);
        fragView = ret;
        return ret;
    }
}
