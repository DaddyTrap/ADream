package io.github.daddytrap.adream.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.util.ADUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

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
    public static final int DOWNLOAD_REQ = 106;

    private static final int DOWNLOADING_NOTI_ID = 200;
    private static final int DOWNLOADED_NOTI_ID = 201;
    private Notification.Builder downloadingNotiBuilder;
    private Notification.Builder downloadedNotiBuilder;
    private NotificationManager notificationManager;

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

    @Override
    public void onCreate() {
        super.onCreate();

        Bitmap bm = BitmapFactory.decodeResource(this.getResources(), R.mipmap.logo);
        notificationManager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        downloadingNotiBuilder = new Notification.Builder(this);
        downloadingNotiBuilder.setSmallIcon(R.mipmap.logo).setLargeIcon(bm).setContentTitle("下载中...").setAutoCancel(true);

        downloadedNotiBuilder = new Notification.Builder(this);
        downloadedNotiBuilder.setSmallIcon(R.mipmap.logo).setLargeIcon(bm).setContentTitle("下载完成").setAutoCancel(true);
    }

    public class MyBinder extends Binder {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
//            Log.i(MusicService.class.getName(), "Dealing with code: " + code);
            String[] args;
            String sourceUrl;
            String mode;
            switch (code) {
                case SET_REQ:
//                    currentState = MusicState.WaitToSet;
                    args = new String[2];
                    data.readStringArray(args);
                    sourceUrl = args[0];
                    mode = args[1];
                    if (mode.equals(SET_SOFT) && currentState != MusicState.WaitToSet) {
                        reply.writeInt(-1);
                        break;
                    }
                    try {
                        currentState = MusicState.Setting;
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
                case DOWNLOAD_REQ:
                    args = new String[2];
                    data.readStringArray(args);
                    sourceUrl = args[0];
                    final String filename = args[1] + ".mp3";
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Request request = new Request.Builder().url(sourceUrl).build();

                    final String sourcePath = Environment.getExternalStorageDirectory() + "/ADream/music/" + filename;

                    Toast.makeText(MusicService.this, "歌曲 " + filename + " 已开始下载", Toast.LENGTH_SHORT).show();

                    // Notification
                    Notification downloadingNotification = downloadingNotiBuilder.setContentText(filename + " 下载中...").build();
                    notificationManager.notify(DOWNLOADING_NOTI_ID, downloadingNotification);

                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try (ResponseBody responseBody = response.body()) {
                                Notification notification;
                                if (!response.isSuccessful()) {
                                    Log.e(MusicService.class.getName(), "Download failed");
                                    notificationManager.cancel(DOWNLOADING_NOTI_ID);
                                    notification = downloadedNotiBuilder.setContentTitle(filename + " 下载失败！").build();
                                    notificationManager.notify(DOWNLOADED_NOTI_ID, notification);
                                    return;
                                }
                                boolean res = ADUtil.saveFile(sourcePath, responseBody.byteStream());
                                if (!res) {
                                    Log.e(MusicService.class.getName(), "Downloaded but write failed");
                                    notificationManager.cancel(DOWNLOADING_NOTI_ID);
                                    notification = downloadedNotiBuilder.setContentTitle(filename + " 下载完成但写入失败！").build();
                                    notificationManager.notify(DOWNLOADED_NOTI_ID, notification);
                                    return;
                                }
                                notificationManager.cancel(DOWNLOADING_NOTI_ID);
                                notification = downloadedNotiBuilder.setContentTitle(filename + " 下载完成！").build();
                                notificationManager.notify(DOWNLOADED_NOTI_ID, notification);
                            }
                        }
                    });
                    break;
            }

            return super.onTransact(code, data, reply, flags);
        }
    }
}
