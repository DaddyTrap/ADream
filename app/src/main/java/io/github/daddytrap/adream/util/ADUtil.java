package io.github.daddytrap.adream.util;

import android.app.ActivityManager;
import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by DaddyTrapC on 2018/1/4.
 */

public class ADUtil {
    public static final String[] IntToMonth = new String[] {
            "",
            "一月",
            "二月",
            "三月",
            "四月",
            "五月",
            "六月",
            "七月",
            "八月",
            "九月",
            "十月",
            "十一月",
            "十二月",
    };

    public static final String[] IntToDate = new String[] {
            "",
            "一日", "二日", "三日", "四日", "五日", "六日", "七日", "八日", "九日", "十日",
            "十一日", "十二日", "十三日", "十四日", "十五日", "十六日", "十七日", "十八日", "十九日", "二十日",
            "二十一日", "二十二日", "二十三日", "二十四日", "二十五日", "二十六日", "二十七日", "二十八日", "二十九日", "三十日",
            "三十一日"
    };

    public static boolean isMyServiceRunning(Class<?> serviceClass, Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static boolean saveFile(String path, InputStream inputStream) {
        File file = new File(path);
        try {
            OutputStream outputStream = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[1024 * 4];
                int read = 0;
                while ((read = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
                outputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
