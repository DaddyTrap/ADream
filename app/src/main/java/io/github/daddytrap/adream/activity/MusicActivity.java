package io.github.daddytrap.adream.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;

public class MusicActivity extends AppCompatActivity {

    private TextView musicTitle;
    private TextView musicLyric;
    private ImageView locationIcon;
    private ImageView playIcon;
    private ImageView nextIcon;

    private ADApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_music);

        app = ADApplication.getInstance();

        setViews();
    }

    private void setViews() {
        musicTitle = (TextView) findViewById(R.id.activity_music_title);
        musicLyric = (TextView) findViewById(R.id.activity_music_lyric);

        musicTitle.setTypeface(app.KAI_TI_FONT);
        musicLyric.setTypeface(app.KAI_TI_FONT);

        locationIcon = (ImageView)findViewById(R.id.activity_music_location_icon);
        playIcon = (ImageView)findViewById(R.id.activity_music_play_icon);
        nextIcon = (ImageView)findViewById(R.id.activity_music_next_icon);
    }
}
