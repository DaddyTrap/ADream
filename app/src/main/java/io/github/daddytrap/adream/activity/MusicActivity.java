package io.github.daddytrap.adream.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.service.MusicService;
import io.github.daddytrap.adream.util.ADUtil;

public class MusicActivity extends AppCompatActivity {

    private TextView musicTitle;
    private TextView musicLyric;
    private ImageView locationIcon;
    private ImageView playIcon;
    private ImageView nextIcon;
    private SeekBar seekBar;
    private TextView timeText;
    private TextView maxTimeText;

    private Handler handler;
    public static final int REFRESH_REQ = 110;

    private IBinder mBinder;
    private ServiceConnection mConnection;

    private Thread updateThread;
    private boolean threadRunning = true;

    private ADApplication app;

    Intent musicServiceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_music);

        app = ADApplication.getInstance();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_REQ:
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        try {
                            mBinder.transact(MusicService.REFRESH_REQ, data, reply, 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        int dataArr[] = new int[3];
                        reply.readIntArray(dataArr);
                        seekBar.setMax(dataArr[1]);
                        seekBar.setProgress(dataArr[0]);

                        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
                        String curPos = format.format(new Date(dataArr[0]));
                        String maxPos = format.format(new Date(dataArr[1]));
                        timeText.setText(curPos);
                        maxTimeText.setText(maxPos);

                        if (dataArr[2] == 0) {
                            playIcon.setImageResource(R.drawable.ic_play_arrow);
                        } else {
                            playIcon.setImageResource(R.drawable.ic_pause);
                        }
                        break;
                }
            }
        };

        // Open service
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mBinder = service;
                // TODO: 随机歌曲
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        String soundUrl = "http://123.207.93.25/test.mp3";
                        String soundPath = "/ADream/music/test.mp3";
                        setSound(soundUrl, soundPath);
                    }
                };
                thread.start();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mConnection = null;
            }
        };

        musicServiceIntent = new Intent(this, MusicService.class);

        // If service is not running, start it
        if (!ADUtil.isMyServiceRunning(MusicService.class, this)) {
            startService(musicServiceIntent);
        }
        bindService(musicServiceIntent, mConnection, 0);

        setViews();

        updateThread = new Thread() {
            @Override
            public void run() {
                while (threadRunning) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.obtainMessage(MusicActivity.REFRESH_REQ).sendToTarget();
                }
            }
        };

        updateThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(mConnection);
        threadRunning = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        bindService(musicServiceIntent, mConnection, 0);
        threadRunning = true;
        updateThread.start();
    }

    private void setViews() {
        musicTitle = (TextView) findViewById(R.id.activity_music_title);
        musicLyric = (TextView) findViewById(R.id.activity_music_lyric);

        musicTitle.setTypeface(app.KAI_TI_FONT);
        musicLyric.setTypeface(app.KAI_TI_FONT);

        locationIcon = (ImageView)findViewById(R.id.activity_music_location_icon);
        playIcon = (ImageView)findViewById(R.id.activity_music_play_icon);
        nextIcon = (ImageView)findViewById(R.id.activity_music_next_icon);
        seekBar = (SeekBar)findViewById(R.id.activity_music_seekbar);

        timeText = (TextView)findViewById(R.id.activity_music_time);
        maxTimeText = (TextView)findViewById(R.id.activity_music_max_time);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) return;
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                data.writeInt(progress);
                try {
                    mBinder.transact(MusicService.SEEK_REQ, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(MusicService.PLAY_REQ, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void setSound(String soundUrl, String soundPath) {
        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeStringArray(new String[] {soundUrl, soundPath});
        try {
            mBinder.transact(MusicService.SET_REQ, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
