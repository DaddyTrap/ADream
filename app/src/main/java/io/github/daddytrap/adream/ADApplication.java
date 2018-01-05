package io.github.daddytrap.adream;

import android.app.Application;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by DaddyTrapC on 2018/1/3.
 */

public class ADApplication extends Application {
    private static ADApplication _ADApplication;

    public Typeface KAI_TI_FONT;

    @Override
    public void onCreate() {
        super.onCreate();
        if (_ADApplication == null)
            _ADApplication = this;
        else
            return;

        KAI_TI_FONT = Typeface.createFromAsset(getAssets(), "fonts/kai_ti.ttf");

        File file = new File(Environment.getExternalStorageDirectory() + "/ADream/music");
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e(ADApplication.class.getName(), "Create dir failed");
            }
        }
    }

    public static ADApplication getInstance() {
        return _ADApplication;
    }
}
