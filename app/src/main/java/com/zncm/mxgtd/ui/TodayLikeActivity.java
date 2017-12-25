package com.zncm.mxgtd.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.ft.LikeFragment;
import com.zncm.mxgtd.ft.RemindFragment;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.WrapContentHeightViewPager;


public class TodayLikeActivity extends Fragment {
    private DetailsFragment detailsFragment;
    private LikeFragment likeFragment;
    private WrapContentHeightViewPager mViewPager;
    private String TITLES[] = new String[]{"今天", "收藏"};


    //        DetailsFragment detailsFragment = new DetailsFragment();
//        Bundle bundle = new Bundle();
//        TaskData taskData = DbUtils.getTkById(MySp.getDefaultTk());
//        bundle.putSerializable(Constant.KEY_PARAM_DATA, taskData);
//        detailsFragment.setArguments(bundle);
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
                    detailsFragment = new DetailsFragment();
                    bundle = new Bundle();
                    bundle.putString("query", EnumData.queryEnum._TODAY.getValue());
                    detailsFragment.setArguments(bundle);
                    fragment = detailsFragment;
                    break;
                case 1:
                    likeFragment = new LikeFragment();
                    fragment = likeFragment;
                    break;

            }

            return fragment;
        }

    }


}