package io.github.daddytrap.adream.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
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

    public static final String SET_HARD = "HARD";
    public static final String SET_SOFT = "SOFT";

    public enum MusicState {
        WaitToSet,
        Setting,
        Ready
    };

    MusicState currentState = MusicState.WaitToSet;

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
//            Log.i(MusicService.class.getName(), "Dealing with code: " + code);
            switch (code) {
                case SET_REQ:
//                    currentState = MusicState.WaitToSet;
                    String[] args = new String[2];
                    data.readStringArray(args);
                    String sourceUrl = args[0];
                    String mode = args[1];
                    if (mode.equals(SET_SOFT) && currentState != MusicState.WaitToSet) {
                        reply.writeInt(-1);
                        break;
                    }
                    try {
                        Log.i(MusicService.class.getName(), "Trying to read " + sourceUrl);
                        mediaPlayer.stop();
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(sourceUrl);
                        mediaPlayer.prepare();
                        currentState = MusicState.Ready;
                        reply.writeInt(2);
                    } catch (IOException e) {
                        e.printStackTrace();
                        currentState = MusicState.WaitToSet;
                        reply.writeInt(0);
                    }
                    break;
                case PLAY_REQ:
                    // Play Button
                    if (currentState != MusicState.Ready) break;
                    if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                    else mediaPlayer.start();
                    break;
                case STOP_REQ:
                    // Stop Button
                    if (currentState != MusicState.Ready) break;
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
                    stopSelf();
                    break;
                case REFRESH_REQ:
                    // Refresh Button
                    if (currentState == MusicState.Ready) {
                        reply.writeIntArray(new int[]{mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration(), mediaPlayer.isPlaying() ? 1 : 0});
                    } else {
                        reply.writeIntArray(new int[]{-1, -1, 0});
                    }
                    break;
                case SEEK_REQ:
                    // Seekbar Action
                    if (currentState != MusicState.Ready) break;
                    int pos = data.readInt();
                    mediaPlayer.seekTo(pos);
                    break;
            }

            return super.onTransact(code, data, reply, flags);
        }
    }
}
