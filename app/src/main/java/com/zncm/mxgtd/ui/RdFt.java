package com.zncm.mxgtd.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.ft.LikeFragment;
import com.zncm.mxgtd.ft.RemindFragment;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.WrapContentHeightViewPager;


public class RdFt extends Fragment {
    private RemindFragment remindFragment;
    private RemindFragment remindOutFragment;

    LikeFragment likeFragment ;

    private WrapContentHeightViewPager mViewPager;
    private String TITLES[] = new String[]{"提醒","收藏"};
//    private String TITLES[] = new String[]{"将来", "周期", "过去"};

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pj, null);
        initView();
        return view;
    }

    private void initView() {
        mViewPager = (WrapContentHeightViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(TITLES.length);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);
        XUtil.initTabLayout(getActivity(), mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(MySp.getTheme());
        }
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
                    likeFragment = new LikeFragment();
                    fragment = likeFragment;
                    break;



//                case 1:
//                    remindOutFragment = new RemindFragment();
//                    bundle = new Bundle();
//                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.NONE.getValue());
//                    remindOutFragment.setArguments(bundle);
//                    fragment = remindOutFragment;
//                    break;
//                case 2:
//                    remindOutFragment = new RemindFragment();
//                    bundle = new Bundle();
//                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.OUTDATE.getValue());
//                    remindOutFragment.setArguments(bundle);
//                    fragment = remindOutFragment;
//                    break;

            }

            return fragment;
        }

    }


}