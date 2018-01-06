package io.github.daddytrap.adream;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DaddyTrapC on 2018/1/3.
 */

public class ADApplication extends Application {
    private static ADApplication _ADApplication;

    public Typeface KAI_TI_FONT;
    public Typeface SIM_KAI_FONT;

    private Map<String, Bitmap> bitmapCache = null;

    @Override
    public void onCreate() {
        super.onCreate();
        if (_ADApplication == null)
            _ADApplication = this;
        else
            return;

        KAI_TI_FONT = Typeface.createFromAsset(getAssets(), "fonts/kai_ti.ttf");
        SIM_KAI_FONT = Typeface.createFromAsset(getAssets(), "fonts/simkai.ttf");

        File file = new File(Environment.getExternalStorageDirectory() + "/ADream/music");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(ADApplication.class.getName(), "Create dir failed");
            }
        }

        bitmapCache = new HashMap<>();
    }

    public static ADApplication getInstance() {
        return _ADApplication;
    }

    public Bitmap getBitmap(String strBase64) {
        if (bitmapCache.containsKey(strBase64)) return bitmapCache.get(strBase64);
        byte[] bytes = Base64.decode(strBase64, Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        bitmapCache.put(strBase64, bm);
        return bm;
    }
}
