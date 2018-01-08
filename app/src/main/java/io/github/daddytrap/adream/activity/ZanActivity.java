package io.github.daddytrap.adream.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.adapter.CommonAdapter;
import io.github.daddytrap.adream.model.Passage;
import io.github.daddytrap.adream.util.ADUtil;
import io.github.daddytrap.adream.viewholder.ViewHolder;

public class ZanActivity extends AppCompatActivity {

    private TextView header = null;
    private ImageView backIcon = null;
    private RecyclerView zanList = null;

    private List<Passage> zanData = null;
    private CommonAdapter<Passage> zanAdapter = null;

    private ADApplication app = ADApplication.getInstance();
    private ADSQLiteOpenHelper helper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zan);

        helper = new ADSQLiteOpenHelper(ZanActivity.this, null);

        getViews();

        header.setTypeface(app.KAI_TI_FONT);

        zanData = helper.getPraisedPassagesByUserId(app.currentUserId);

        zanList.setLayoutManager(new LinearLayoutManager(ZanActivity.this));

        zanAdapter = new CommonAdapter<Passage>(ZanActivity.this, R.layout.activity_zan_list_item, zanData) {
            @Override
            public void convert(ViewHolder holder, Passage object) {
                System.out.println("Hello convert");
                TextView date = holder.getView(R.id.date);
                int month = object.getDate().getMonth() + 1;
                int day = object.getDate().getDay();
                date.setText(ADUtil.IntToMonth[month] + ADUtil.IntToDate[day]);
                date.setTypeface(app.KAI_TI_FONT);

                TextView title = holder.getView(R.id.title);
                title.setText(object.getTitle());
                title.setTypeface(app.KAI_TI_FONT);

                TextView author = holder.getView(R.id.author);
                author.setText(object.getAuthor());
                author.setTypeface(app.KAI_TI_FONT);

                TextView content = holder.getView(R.id.content);
                content.setText(object.getContent());
                content.setTypeface(app.KAI_TI_FONT);

                ImageView image = holder.getView(R.id.image);
                if (object.getAvatarBase64() == null || object.getAvatarBase64().isEmpty()) {
                    image.setImageResource(R.mipmap.shici_background);
                } else {
                    image.setImageBitmap(app.getBitmap(object.getAvatarBase64()));
                }
            }
        };

        zanList.setAdapter(zanAdapter);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getViews() {
        header = (TextView) findViewById(R.id.activity_zan_header);
        backIcon = (ImageView) findViewById(R.id.activity_zan_back_icon);
        zanList = (RecyclerView) findViewById(R.id.zan_list);
    }
}
