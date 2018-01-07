package io.github.daddytrap.adream.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.fragment.JiuwuFragment;
import io.github.daddytrap.adream.model.Passage;

public class JiuwuDetailActivity extends AppCompatActivity {

    ImageView backIcon;
    ImageView image;
    TextView title;
    TextView content;
    ImageView zanIcon;

    ADApplication app;

    Passage thisPassage;
    boolean zanned;
    ADSQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jiuwu_detail);

        app = ADApplication.getInstance();

        setViews();
    }

    void setViews() {
        Intent dataIntent = getIntent();
        final int passageId = dataIntent.getIntExtra("passage_id", -1);
        helper = new ADSQLiteOpenHelper(this);
        thisPassage = helper.getPassageById(passageId);

        if (thisPassage == null) {
            finish();
            return;
        }

        backIcon = (ImageView)findViewById(R.id.activity_jiuwu_detail_back_icon);
        image = (ImageView)findViewById(R.id.activity_jiuwu_detail_image);
        title = (TextView)findViewById(R.id.activity_jiuwu_detail_title);
        content = (TextView)findViewById(R.id.activity_jiuwu_detail_content);
        zanIcon = (ImageView)findViewById(R.id.activity_jiuwu_detail_zan_icon);

        title.setTypeface(app.KAI_TI_FONT);
        content.setTypeface(app.KAI_TI_FONT);

        title.setText(thisPassage.getTitle());
        content.setText(thisPassage.getContent());
        image.setImageBitmap(app.getBitmap(thisPassage.getAvatarBase64()));
        // Check if it is zanned
        zanned = helper.getPraiseByUserIdAndPassageId(app.currentUserId, passageId);
        zanIcon.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JiuwuDetailActivity.this.finish();
            }
        });

        zanIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanned = !zanned;
                try {
                    if (zanned) {
                        helper.insertPraise(app.currentUserId, thisPassage.getPassageId());
                    } else {
                        helper.deletePraise(app.currentUserId, thisPassage.getPassageId());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                zanIcon.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);
            }
        });
    }
}
