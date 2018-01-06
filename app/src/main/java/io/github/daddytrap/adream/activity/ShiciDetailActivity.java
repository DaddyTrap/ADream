package io.github.daddytrap.adream.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;

public class ShiciDetailActivity extends AppCompatActivity {

    TextView titleTextView;
    TextView authorTextView;
    TextView contentTextView;
    ImageView zanImageView;

    ImageView backIcon;

    int id;
    boolean zanned = false;

    ADApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shici_detail);

        app = ADApplication.getInstance();

        setViews();
    }

    void setViews() {
        titleTextView = (TextView)findViewById(R.id.activity_shici_title);
        authorTextView = (TextView)findViewById(R.id.activity_shici_author);
        contentTextView = (TextView)findViewById(R.id.activity_shici_content);
        zanImageView = (ImageView)findViewById(R.id.activity_shici_zan);
        backIcon = (ImageView)findViewById(R.id.activity_shici_detail_back_icon);

        titleTextView.setTypeface(app.KAI_TI_FONT);
        authorTextView.setTypeface(app.KAI_TI_FONT);
        contentTextView.setTypeface(app.KAI_TI_FONT);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getInt("id", -1);
        titleTextView.setText(extras.getString("title", ""));
        authorTextView.setText(extras.getString("author", ""));
        contentTextView.setText(extras.getString("content", ""));
        zanned = extras.getBoolean("zan", false);
        zanImageView.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);

        zanImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zanned = !zanned;
                zanImageView.setImageResource(zanned ? R.mipmap.zanned_icon : R.mipmap.zan_icon);
                Intent resIntent = new Intent();
                resIntent.putExtra("zanned", zanned);
                ShiciDetailActivity.this.setResult(RESULT_OK, resIntent);
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
