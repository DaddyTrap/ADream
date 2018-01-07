package io.github.daddytrap.adream.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.fragment.JiuwuFragment;

public class JiuwuDetailActivity extends AppCompatActivity {

    ImageView backIcon;
    ImageView image;
    TextView title;
    TextView content;
    ImageView zanIcon;

    ADApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiuwu_detail);

        app = ADApplication.getInstance();

        setViews();
    }

    void setViews() {
        backIcon = (ImageView)findViewById(R.id.activity_jiuwu_detail_back_icon);
        image = (ImageView)findViewById(R.id.activity_jiuwu_detail_image);
        title = (TextView)findViewById(R.id.activity_jiuwu_detail_title);
        content = (TextView)findViewById(R.id.activity_jiuwu_detail_content);
        zanIcon = (ImageView)findViewById(R.id.activity_jiuwu_detail_zan_icon);

        title.setTypeface(app.KAI_TI_FONT);
        content.setTypeface(app.KAI_TI_FONT);

        // TODO: 填数据

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JiuwuDetailActivity.this.finish();
            }
        });

        // TODO: 点赞
    }
}
