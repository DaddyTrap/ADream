package io.github.daddytrap.adream.fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.activity.JiuwuDetailActivity;
import io.github.daddytrap.adream.adapter.CommonAdapter;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.viewholder.ViewHolder;

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

    private RecyclerView recyclerView;
    private CommonAdapter<Passage> adapter;
    private List<Passage> jiuwus;

    private ADApplication app;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        app = ADApplication.getInstance();

        jiuwus = new LinkedList<>();
        jiuwus.add(new Passage(1, "测试", "测试", "测试", new Date(), ""));

        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_jiuwu_recyclerview);
        adapter = new CommonAdapter<Passage>(this.getActivity(), R.layout.recyclerview_item_jiuwu, jiuwus) {
            @Override
            public void convert(ViewHolder holder, Passage object) {
                TextView title = holder.getView(R.id.item_jiuwu_title);
                TextView content = holder.getView(R.id.item_jiuwu_content);
                ImageView image = holder.getView(R.id.item_jiuwu_image);

                title.setTypeface(app.KAI_TI_FONT);
                content.setTypeface(app.KAI_TI_FONT);

                Bitmap bm = app.getBitmap(object.getAvatarBase64());

                title.setText(object.getTitle());
                content.setText(object.getContent());
                image.setImageBitmap(bm);
            }
        };
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // TODO: 真正获取数据
                Passage jiuwu = jiuwus.get(position);
                Intent intent = new Intent(JiuwuFragment.this.getActivity(), JiuwuDetailActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {}
        });
    }
}
