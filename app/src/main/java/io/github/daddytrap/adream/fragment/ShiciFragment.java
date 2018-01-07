package io.github.daddytrap.adream.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.activity.ShiciDetailActivity;
import io.github.daddytrap.adream.adapter.CommonAdapter;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.viewholder.ViewHolder;

import static android.app.Activity.RESULT_OK;

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

    RecyclerView recyclerView;
    CommonAdapter<Passage> adapter;
    List<Passage> shicis;
    ADApplication app;

    ADSQLiteOpenHelper helper;

    public static final int SHICI_DETAIL_REQ = 100;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = ADApplication.getInstance();
        helper = new ADSQLiteOpenHelper(this.getActivity());

        shicis = helper.getPassageByType("shici");

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_shici_recyclerview);
        adapter = new CommonAdapter<Passage>(this.getActivity(), R.layout.recyclerview_item_shici, shicis) {
            @Override
            public void convert(ViewHolder holder, Passage object) {
                TextView title = holder.getView(R.id.item_shici_title);
                TextView author = holder.getView(R.id.item_shici_author);
                TextView content = holder.getView(R.id.item_shici_content);

                title.setTypeface(app.KAI_TI_FONT);
                author.setTypeface(app.KAI_TI_FONT);
                content.setTypeface(app.KAI_TI_FONT);

                title.setText(object.getTitle());
                author.setText(object.getAuthor());

                // Cut
                String contentText = object.getContent();
                String res = contentText.split("。")[0] + "。";

                content.setText(res);
            }
        };
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(ShiciFragment.this.getActivity(), ShiciDetailActivity.class);
                Passage shici = shicis.get(position);
                intent.putExtra("passage_id", shici.getPassageId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {}
        });
    }
}
