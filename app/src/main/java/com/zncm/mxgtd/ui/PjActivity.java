package com.zncm.mxgtd.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.ft.ProjectFragment;
import com.zncm.mxgtd.ft.RefreshEvent;
import com.zncm.mxgtd.utils.XUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class PjActivity extends BaseActivity {
    private ProjectFragment pjFinishFragment;
    private ProjectFragment pjFragment;
    private ViewPager mViewPager;
    private String TITLES[] = new String[]{"进行中", "已完成"};


//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.activity_pj, null);
//        initView();
//        return view;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(TITLES.length);
        TabLayout mTabLayout = (TabLayout)findViewById(R.id.mTabLayout);
        XUtil.initTabLayout(this,mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
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


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
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