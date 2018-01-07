package io.github.daddytrap.adream.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.model.Passage;

public class ShiciDetailActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView authorTextView;
    TextView contentTextView;
    ImageView zanImageView;

    ImageView backIcon;

    Passage thisPassage;
    boolean zanned = false;
    ADSQLiteOpenHelper helper;

    ADApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shici_detail);

        app = ADApplication.getInstance();

        setViews();
    }

    void setViews() {
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        final int passageId = extras.getInt("passage_id", -1);

        helper = new ADSQLiteOpenHelper(this);
        thisPassage = helper.getPassageById(passageId);

        if (thisPassage == null) {
            finish();
            return;
        }

        titleTextView = (TextView)findViewById(R.id.activity_shici_title);
        authorTextView = (TextView)findViewById(R.id.activity_shici_author);
        contentTextView = (TextView)findViewById(R.id.activity_shici_content);
        zanImageView = (ImageView)findViewById(R.id.activity_shici_zan);
        backIcon = (ImageView)findViewById(R.id.activity_shici_detail_back_icon);

        titleTextView.setTypeface(app.KAI_TI_FONT);
        authorTextView.setTypeface(app.KAI_TI_FONT);
        contentTextView.setTypeface(app.KAI_TI_FONT);

        titleTextView.setText(thisPassage.getTitle());
        authorTextView.setText(thisPassage.getAuthor());
        contentTextView.setText(thisPassage.getContent());
        zanned = helper.getPraiseByUserIdAndPassageId(app.currentUserId, thisPassage.getPassageId());

        zanImageView.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);

        zanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanned = !zanned;
                if (zanned) {
                    helper.insertPraise(app.currentUserId, thisPassage.getPassageId());
                } else {
                    helper.deletePraise(app.currentUserId, thisPassage.getPassageId());
                }
                zanImageView.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);
            }
        });

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShiciDetailActivity.this.finish();
            }
        });
    }
}
