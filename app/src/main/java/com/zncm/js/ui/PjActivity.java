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
import com.zncm.js.ft.ProjectFragment;
import com.zncm.js.ft.RefreshEvent;
import com.zncm.js.utils.XUtil;

import de.greenrobot.event.EventBus;


public class PjActivity extends BaseActivity {
    private ProjectFragment pjFinishFragment;
    private ProjectFragment pjFragment;
    private ViewPager mViewPager;
    private String TITLES[] = new String[]{"进行中", "已完成"};

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
        EventBus.getDefault().register(this);
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
                    pjFragment = new ProjectFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.ON.getValue());
                    pjFragment.setArguments(bundle);
                    fragment = pjFragment;
                    break;
                case 1:
                    pjFinishFragment = new ProjectFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.OFF.getValue());
                    pjFinishFragment.setArguments(bundle);
                    fragment = pjFinishFragment;
                    break;
            }

            return fragment;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PjInfo.getValue()));

    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.PROJECT.getValue()) {
            pjFragment.onRefresh();
            pjFinishFragment.onRefresh();
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_pj;
    }


}