package io.github.daddytrap.adream.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.adapter.CommonAdapter;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.viewholder.ViewHolder;
import io.github.daddytrap.adream.activity.EditMiaobiActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MiaobiFragment extends ADFragment {

    private RecyclerView miaobiList = null;
    private List<Passage> miaobiData;
    private CommonAdapter<Passage> miaobiAdapter = null;
    private static ADApplication application = ADApplication.getInstance();
    private ImageView miaobiNewIcon;


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

    @Override
    public void onViewCreated(View view, @NonNull Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        miaobiList = (RecyclerView) fragView.findViewById(R.id.recycler);
        // TODO : 获取数据

        miaobiAdapter = new CommonAdapter<Passage>(getContext(), R.layout.miaobi_item, miaobiData) {
            @Override
            public void convert(ViewHolder holder, Passage object) {
                CircleImageView image = holder.getView(R.id.image);
                image.setImageBitmap(application.getBitmap(object.getAvatarBase64()));
                TextView title = holder.getView(R.id.title);
                title.setText(object.getTitle());
                TextView content = holder.getView(R.id.content);
                content.setText(object.getContent());
            }
        };
        miaobiAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                // TODO : 填写Activity
                Intent intent = new Intent();
                intent.putExtra("id", miaobiData.get(position).getPassageId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
        miaobiList.setAdapter(miaobiAdapter);
        miaobiList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        miaobiNewIcon = (ImageView) view.findViewById(R.id.fragment_miaobi_new_icon);
        miaobiNewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MiaobiFragment.this.getActivity(), EditMiaobiActivity.class);
                startActivity(intent);
            }
        });

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
