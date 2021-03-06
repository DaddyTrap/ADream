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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.nio.channels.SelectionKey;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.model.Music;
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
    private ImageView backIcon;
    private ImageView background;
    private ImageView downloadIcon;

    private Handler handler;
    public static final int REFRESH_REQ = 110;

    private IBinder mBinder;
    private ServiceConnection mConnection;

    private Thread updateThread;
    private boolean threadRunning = true;

    private Music currentMusic;

    private ADApplication app;

    Intent musicServiceIntent;

    ADSQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_music);

        app = ADApplication.getInstance();

        helper = new ADSQLiteOpenHelper(this);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case REFRESH_REQ:
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        try {
                            if (mBinder == null) return;
                            mBinder.transact(MusicService.REFRESH_REQ, data, reply, 0);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        int dataArr[] = new int[3];
                        reply.readIntArray(dataArr);
                        seekBar.setMax(dataArr[1]);
                        seekBar.setProgress(dataArr[0]);

                        SimpleDateFormat format = new SimpleDateFormat("mm:ss");

                        int curPosInt = dataArr[0];
                        int maxPosInt = dataArr[1];

                        String curPos;
                        String maxPos;
                        if (curPosInt == -1 || maxPosInt == -1) {
                            curPos = "正在加载...";
                            maxPos = "稍安勿躁";
                        } else {
                            curPos = format.format(new Date(curPosInt));
                            maxPos = format.format(new Date(maxPosInt));
                        }

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
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    mBinder.transact(MusicService.REFRESH_REQ, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    return;
                }
                int[] rep = new int[3];
                reply.readIntArray(rep);
                // A Thread is needed, otherwise it will stuck the UI
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        Music music = getRecommendMusic();
                        if (music != null)
                            setSound(music, MusicService.SET_SOFT);
                        else
                            Toast.makeText(MusicActivity.this, "找不到推荐音乐", Toast.LENGTH_SHORT).show();
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

    Music getRandomMusic() {
        List<Music> musicList = helper.getMusics();
        if (musicList == null || musicList.isEmpty()) return null;
        int rand = Math.abs(new Random().nextInt()) % musicList.size();

        return musicList.get(rand);
    }

    Music getRecommendMusic() {
        Date date = Calendar.getInstance().getTime();
        Music music = helper.getMusicByDate(date);

        if (music != null) {
            Log.i(MusicActivity.class.getName(), "推荐" + music.getTitle());
        } else {
            Log.i(MusicActivity.class.getName(), "找不到推荐");
        }

        if (music == null) {
            // If there is no music, generate one
            music = getRandomMusic();

            // Insert today's recommend music
            helper.insertRecommend(date, music.getId());
        }

        return music;
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

        backIcon = (ImageView)findViewById(R.id.activity_music_back_icon);

        timeText = (TextView)findViewById(R.id.activity_music_time);
        maxTimeText = (TextView)findViewById(R.id.activity_music_max_time);

        background = (ImageView)findViewById(R.id.activity_music_background);
        downloadIcon = (ImageView)findViewById(R.id.activity_music_download);

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

        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MusicActivity.this.finish();
            }
        });

        nextIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSound(getRandomMusic(), MusicService.SET_HARD);
            }
        });

        locationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSound(getRecommendMusic(), MusicService.SET_HARD);
            }
        });

        downloadIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Parcel data = Parcel.obtain();
                Parcel reply = Parcel.obtain();
                try {
                    data.writeStringArray(new String[] {currentMusic.getHref(), currentMusic.getTitle()});
                    mBinder.transact(MusicService.DOWNLOAD_REQ, data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                    Toast.makeText(MusicActivity.this, "下载调用失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void setSound(final Music music, String mode) {
        // Change UI
        handler.post(new Runnable() {
            @Override
            public void run() {
                musicTitle.setText(music.getTitle());
                musicLyric.setText(music.getLyric());

                // Random background
                int index = Math.abs(new Random().nextInt()) % backgroundList.length;
                background.setImageResource(backgroundList[index]);

                timeText.setText("正在加载...");
                maxTimeText.setText("稍安勿躁");
            }
        });

        currentMusic = music;

        Parcel data = Parcel.obtain();
        Parcel reply = Parcel.obtain();
        data.writeStringArray(new String[] {music.getHref(), mode});
        try {
            mBinder.transact(MusicService.SET_REQ, data, reply, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (mode.equals(MusicService.SET_HARD)) {
            data.recycle();
            reply.recycle();
            data = Parcel.obtain();
            reply = Parcel.obtain();

            try {
                mBinder.transact(MusicService.PLAY_REQ, data, reply, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int[] backgroundList = new int[] {R.mipmap.music_background_0, R.mipmap.music_background_1, R.mipmap.music_background_2, R.mipmap.music_background_3, R.mipmap.music_background_4};
}
