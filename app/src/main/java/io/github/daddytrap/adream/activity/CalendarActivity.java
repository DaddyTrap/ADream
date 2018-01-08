package io.github.daddytrap.adream.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xhinliang.lunarcalendar.LunarCalendar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.ADSQLiteOpenHelper;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.model.Qian;
import io.github.daddytrap.adream.util.ADUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.Util;
import okio.BufferedSource;

public class CalendarActivity extends AppCompatActivity {
    private TextView mMonth, mDay, mLunarDate;
    private TextView mSuggestIcon, mNotSuggestIcon;
    private List<TextView> mSuggest = new ArrayList<>(),
            mNotSuggest = new ArrayList<>();
    private TextView mQian;
    private ImageView mBack;
    final private static String URL_TMPELATE = "http://www.laohuangli.net/wannianli/%d/%d-%d-%d.html";

    private ADSQLiteOpenHelper helper;
    private ADApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        helper = new ADSQLiteOpenHelper(this);
        app = ADApplication.getInstance();
        findView();
        setView();
    }

    private void findView() {
        this.mMonth = (TextView)findViewById(R.id.month);
        this.mDay = (TextView)findViewById(R.id.day);
        this.mLunarDate = (TextView)findViewById(R.id.lunar_calendar);
        this.mNotSuggestIcon = (TextView)findViewById(R.id.not_suggest_icon);
        this.mSuggestIcon = (TextView)findViewById(R.id.suggest_icon);
        this.mQian = (TextView)findViewById(R.id.qian_text);
        this.mBack = (ImageView)findViewById(R.id.activity_calendar_back_icon);
        int suggest_ids[] = new int[]{R.id.suggest_left, R.id.suggest_middle, R.id.suggest_right};
        for (int id : suggest_ids) {
            this.mSuggest.add((TextView) findViewById(id));
        }
        int not_suggest_ids[] = new int[]{R.id.not_suggest_left, R.id.not_suggest_middle, R.id.not_suggest_right};
        for (int id : not_suggest_ids) {
            this.mNotSuggest.add((TextView) findViewById(id));
        }
    }

    private void setView() {
        ADApplication app = ADApplication.getInstance();
        this.mMonth.setTypeface(app.KAI_TI_FONT);
        this.mDay.setTypeface(app.FZ_SHUITI_FONT);
        this.mLunarDate.setTypeface(app.KAI_TI_FONT);
        this.mSuggestIcon.setTypeface(app.KAI_TI_FONT);
        this.mNotSuggestIcon.setTypeface(app.KAI_TI_FONT);
        for (TextView view : mSuggest) {
            view.setTypeface(app.KAI_TI_FONT);
        }
        for (TextView view : mNotSuggest) {
            view.setTypeface(app.KAI_TI_FONT);
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        LunarCalendar lunarCalendar = LunarCalendar.obtainCalendar(year, month, day);

        mMonth.setText(ADUtil.IntToMonth[month]);
        mDay.setText(String.valueOf(day));
        mLunarDate.setText(lunarCalendar.getLunarMonth() + "月" + lunarCalendar.getLunarDay());
        String url = String.format(URL_TMPELATE, year, year, month, day);
        requestHuangli(url);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalendarActivity.this.finish();
            }
        });
        mQian.setTypeface(app.KAI_TI_FONT);
        Qian qian = queryQian();
        if (qian == null) {
            mQian.setText("今天还未摇签，可重启应用看看");
        } else {
            String title = qian.getTitle();
            String content = qian.getContent();
            String wholeQian = "";
            wholeQian += "[" + title + "]\n\n";
            content = content.replace("\n", "");
            wholeQian += content.replaceAll("。|，", "\n");
            mQian.setText(wholeQian.trim());
        }
    }

    private void requestHuangli(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                handelError();
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                BufferedSource source = response.body().source();
                String html = null;
                try {
                    Charset charset = Util.bomAwareCharset(source, Charset.forName("gb2312"));
                    html = source.readString(charset);
                } finally {
                    Util.closeQuietly(source);
                }
                CalendarActivity.this.parseHtml(html);
            }
        });
    }
    private void parseHtml(String html) {
        if (html == null) {
            return;
        }
        final Handler main = new Handler(Looper.getMainLooper());
        Document doc = Jsoup.parse(html, "gb2312");
        String suggest = doc.select(".rl_txt_2").text();
        String not_suggest = doc.select(".rl_txt_3").text();
        final List<String> suggests = topThree(suggest);
        final List<String> not_suggests = topThree(not_suggest);
        main.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 3; ++i) {
                    mSuggest.get(i).setText(suggests.get(i));
                }
                for (int i = 0; i < 3; ++i) {
                    mNotSuggest.get(i).setText(not_suggests.get(i));
                }
                CalendarActivity.this.removeMask();
            }
        });
    }

    private void handelError() {
        final Handler main = new Handler(Looper.getMainLooper());
        main.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(CalendarActivity.this, R.string.calendar_request_error, Toast.LENGTH_SHORT);
                CalendarActivity.this.removeMask();
            }
        });
    }

    private List<String> topThree(String str) {
        String[] str_list = str.split("　");
        List<String> ret = new ArrayList<>();
        for (String s : str_list) {
            ret.add(s);
            if (ret.size() == 3) {
                return ret;
            }
        }
        while (ret.size() < 3) {
            ret.add(CalendarActivity.this.getResources().getString(R.string.calendar_empty));
        }
        return ret;
    }

    private void removeMask() {

    }

    private Qian queryQian() {
        Date date = Calendar.getInstance().getTime();
        return helper.getQianByUserIdAndDate(app.currentUserId, date);
    }
}
