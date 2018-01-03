package io.github.daddytrap.adream.activity;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;
import io.github.daddytrap.adream.adapter.DemoPagerAdapter;
import io.github.daddytrap.adream.fragment.DemoFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private DemoPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    private Map<Integer, String> IndexToSectionName = new HashMap<>();
    private Map<Integer, Integer> IndexToTitleImageResID = new HashMap<>();

    private TextView toolbarTitleText;
    private TextView shiciText;
    private TextView jiuwuText;
    private TextView miaobiText;
    private ImageView selectLine;
    private ImageView titleImage;

    private ADApplication app;

    public enum ViewState {
        Main,
        Detail
    };

    ViewState currentViewState = ViewState.Main;

    float touchY1 = 0;
    float touchY2 = 0;
    private static final float MIN_SWIPE_DISTANCE = 400;

    @Override
    public void onBackPressed() {
        if (currentViewState == ViewState.Detail) {
            changeViewState(ViewState.Main);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                Log.i("INFO", "ACTION_DOWN");
                touchY1 = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("INFO", "ACTION_UP");
                touchY2 = event.getY();
                if (touchY1 - touchY2 > MIN_SWIPE_DISTANCE) {
                    changeViewState(ViewState.Detail);
                } /*else if (touchY2 - touchY1 > MIN_SWIPE_DISTANCE) {
                    changeViewState(ViewState.Main);
                }*/
                break;
            default:
        }
        return super.dispatchTouchEvent(event);
    }

    void changeViewState(ViewState state) {
        if (state == currentViewState) return;
        Log.i("INFO", "Change state to " + state.name());
        currentViewState = state;
        int curIndex = mViewPager.getCurrentItem();
        DemoFragment curFragment = (DemoFragment) mSectionsPagerAdapter.getItem(curIndex);
        Log.i("INFO", "Cur index: " + curIndex);
        if (currentViewState == ViewState.Main) {
            Animation animation = new TranslateAnimation(0, 0, 0, curFragment.fragView.getHeight());
            animation.setDuration(300);
            curFragment.fragView.startAnimation(animation);
            curFragment.fragView.setVisibility(View.INVISIBLE);
            toolbarTitleText.startAnimation(animation);
            toolbarTitleText.setText("");
        } else {
            Animation animation = new TranslateAnimation(0, 0, curFragment.fragView.getHeight(), 0);
            animation.setDuration(300);
            curFragment.fragView.startAnimation(animation);
            curFragment.fragView.setVisibility(View.VISIBLE);
            toolbarTitleText.startAnimation(animation);
            toolbarTitleText.setText(IndexToSectionName.get(curIndex));
        }
        titleImage.setImageResource(IndexToTitleImageResID.get(curIndex));
    }

    void onChangePage(int position) {
        DemoFragment curFragment = (DemoFragment) mSectionsPagerAdapter.getItem(position);

        if (currentViewState == ViewState.Main) {
            curFragment.fragView.setVisibility(View.INVISIBLE);
            toolbarTitleText.setText("");
        } else if (currentViewState == ViewState.Detail) {
            curFragment.fragView.setVisibility(View.VISIBLE);
            toolbarTitleText.setText(IndexToSectionName.get(position));
        }

        Animation animation = new AlphaAnimation(0, 1);
        animation.setDuration(300);
        titleImage.setImageResource(IndexToTitleImageResID.get(position));
        titleImage.startAnimation(animation);
    }

    void setViews() {
        IndexToSectionName.put(0, "诗词");
        IndexToSectionName.put(1, "旧物");
        IndexToSectionName.put(2, "妙笔");
        IndexToTitleImageResID.put(0, R.mipmap.shici_title_image);
        IndexToTitleImageResID.put(1, R.mipmap.jiuwu_title_image);
        IndexToTitleImageResID.put(2, R.mipmap.miaobi_title_image);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new DemoPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setPageTransformer(true, new SectionTransformer());

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                MainActivity.this.onChangePage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        toolbarTitleText = (TextView)findViewById(R.id.activity_main_toolbar_title);
        shiciText = (TextView)findViewById(R.id.activity_main_shici_text);
        jiuwuText = (TextView)findViewById(R.id.activity_main_jiuwu_text);
        miaobiText = (TextView)findViewById(R.id.activity_main_miaobi_text);

        toolbarTitleText.setTypeface(app.KAI_TI_FONT);
        shiciText.setTypeface(app.KAI_TI_FONT);
        jiuwuText.setTypeface(app.KAI_TI_FONT);
        miaobiText.setTypeface(app.KAI_TI_FONT);

        selectLine = (ImageView)findViewById(R.id.activity_main_select_line);
        titleImage = (ImageView)findViewById(R.id.activity_main_title_image);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = ADApplication.getInstance();

        setViews();
    }



    public class SectionTransformer implements ViewPager.PageTransformer {
        @Override
        public void transformPage(View page, float position) {
            int this_page_num = (int)page.getTag();
            float translation = 0;
            translation = (this_page_num - position) * 280;
            selectLine.setTranslationX(translation);
        }
    }
}
