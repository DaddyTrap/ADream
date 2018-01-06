package io.github.daddytrap.adream.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.xhinliang.lunarcalendar.LunarCalendar;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.util.ADUtil;

public class StartupActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 110;
    private AlertDialog.Builder permissionWarnDialogBuilder;
    private static final String[] permissionsToRequestArr  = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private TextView publicDateView;
    private TextView traditionDateView;
    private ADApplication app;

    void setViews() {
        publicDateView = (TextView)findViewById(R.id.activity_startup_public_date);
        traditionDateView = (TextView)findViewById(R.id.activity_startup_tradition_date);

        publicDateView.setTypeface(app.KAI_TI_FONT);
        traditionDateView.setTypeface(app.KAI_TI_FONT);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int date = calendar.get(Calendar.DATE);
        LunarCalendar lunarCalendar = LunarCalendar.obtainCalendar(year, month, date);

        String lunarMonth = lunarCalendar.getLunarMonth();
        String lunarDate = lunarCalendar.getLunarDay();

        traditionDateView.setText(lunarMonth + lunarDate);

        publicDateView.setText(ADUtil.IntToMonth[month] + ADUtil.IntToDate[date]);

        // After 2 secs, jump to MainActivity
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(StartupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }, 2000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        app = ADApplication.getInstance();

        permissionWarnDialogBuilder = new AlertDialog.Builder(this);
        permissionWarnDialogBuilder.setMessage(R.string.permission_warn_message).setPositiveButton(R.string.alertdialog_yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermission();
            }
        }).setNegativeButton(R.string.alertdialog_no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setViews();
            }
        });

        requestPermission();
    }

    void requestPermission() {
        LinkedList<String> permissionsToRequest = new LinkedList<>(Arrays.asList(permissionsToRequestArr));

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.remove(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            permissionsToRequest.remove(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        String[] permissionsToRequestArg = new String[permissionsToRequest.size()];
        permissionsToRequest.toArray(permissionsToRequestArg);

        if (permissionsToRequestArg.length > 0) {
            ActivityCompat.requestPermissions(this, permissionsToRequestArr, PERMISSION_REQ_CODE);
        } else {
            setViews();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean somethingNotGranted = false;
        if (requestCode == PERMISSION_REQ_CODE) {
            for (int i = 0; i < permissions.length; ++i) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    somethingNotGranted = true;
                    Log.w("PERMISSION NOT GRANTED", permissions[i]);
                }
            }
        }
        if (somethingNotGranted) {
            permissionWarnDialogBuilder.create().show();
        } else {
            setViews();
        }
    }
}
