package io.github.daddytrap.adream.activities;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import io.github.daddytrap.adream.ADApplication;
import io.github.daddytrap.adream.R;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

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
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class DemoFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public int section_num;

        public View fragView;

        public DemoFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static DemoFragment newInstance(int sectionNumber) {
            DemoFragment fragment = new DemoFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            fragment.section_num = sectionNumber;
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

            rootView.setTag(getArguments().getInt(ARG_SECTION_NUMBER) - 1);
            rootView.setVisibility(View.INVISIBLE);
            fragView = rootView;
            Log.i("INFO", "Created View " + section_num);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new LinkedList<>();
            for (int i = 0; i < 3; ++i) {
                fragments.add(DemoFragment.newInstance(i + 1));
            }
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a DemoFragment (defined as a static inner class below).
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "SECTION 1";
                case 1:
                    return "SECTION 2";
                case 2:
                    return "SECTION 3";
            }
            return null;
        }
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
