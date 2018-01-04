package io.github.daddytrap.adream.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by DaddyTrapC on 2018/1/4.
 */

public class MusicService extends Service {
    private IBinder mBinder = new MyBinder();
    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public static final int SET_REQ = 100;
    public static final int PLAY_REQ = 101;
    public static final int STOP_REQ = 102;
    public static final int QUIT_REQ = 103;
    public static final int REFRESH_REQ = 104;
    public static final int SEEK_REQ = 105;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public MusicService() {
        mediaPlayer.setLooping(true);
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
            switch (code) {
                case SET_REQ:
                    String sourcePath = Environment.getExternalStorageDirectory() + data.readString();
                    try {
                        mediaPlayer.setDataSource(sourcePath);
                        mediaPlayer.prepare();
                        reply.writeBooleanArray(new boolean[]{true});
                    } catch (IOException e) {
                        e.printStackTrace();
                        reply.writeBooleanArray(new boolean[]{false});
                    }
                    break;
                case PLAY_REQ:
                    // Play Button
                    if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                    else mediaPlayer.start();
                    break;
                case STOP_REQ:
                    // Stop Button
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                        try {
                            mediaPlayer.prepare();
                            mediaPlayer.seekTo(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case QUIT_REQ:
                    // Quit Button
                    mediaPlayer.release();
                    break;
                case REFRESH_REQ:
                    // Refresh Button
                    reply.writeIntArray(new int[] {mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration(), mediaPlayer.isPlaying() ? 1 : 0});
                    break;
                case SEEK_REQ:
                    // Seekbar Action
                    int pos = data.readInt();
                    mediaPlayer.seekTo(pos);
                    break;
            }

            return super.onTransact(code, data, reply, flags);
        }
    }
}
