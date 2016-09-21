package com.zncm.js.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;
import com.zncm.js.R;
import com.zncm.js.data.Constant;
import com.zncm.js.data.EnumData;
import com.zncm.js.ft.RemindFragment;
import com.zncm.js.utils.XUtil;


public class RdActivity extends BaseActivity {
    private RemindFragment remindFragment;
    private RemindFragment remindOutFragment;
    private ViewPager mViewPager;
    private String TITLES[] = new String[]{"将来", "周期", "过去"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(TITLES.length);
        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        XUtil.viewPagerRandomAnimation(mViewPager);
        XUtil.initIndicatorTheme(indicator);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return TITLES[position];
        }

        @Override
        public int getCount() {
            return TITLES.length;
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            Bundle bundle = null;
            switch (position) {
                case 0:
                    remindFragment = new RemindFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.ON.getValue());
                    remindFragment.setArguments(bundle);
                    fragment = remindFragment;
                    break;
                case 1:
                    remindOutFragment = new RemindFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.NONE.getValue());
                    remindOutFragment.setArguments(bundle);
                    fragment = remindOutFragment;
                    break;
                case 2:
                    remindOutFragment = new RemindFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.OUTDATE.getValue());
                    remindOutFragment.setArguments(bundle);
                    fragment = remindOutFragment;
                    break;

            }

            return fragment;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pj;
    }

}