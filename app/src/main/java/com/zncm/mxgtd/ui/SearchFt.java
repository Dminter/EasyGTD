package com.zncm.mxgtd.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.WrapContentHeightViewPager;


public class SearchFt extends Fragment {
    private DetailsFragment searchFt;
    private WrapContentHeightViewPager mViewPager;
    private String TITLES[] = new String[]{"搜索"};

    View view;

    String query;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pj, null);
        initView();
        return view;
    }

    private void initView() {

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            query = bundle.getString("query");


        }

        mViewPager = (WrapContentHeightViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));
        mViewPager.setOffscreenPageLimit(TITLES.length);
        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.mTabLayout);
        XUtil.initTabLayout(getActivity(), mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setVisibility(View.GONE);
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
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    searchFt = new DetailsFragment();
                    bundle.putString("query", "ok");
                    searchFt.setArguments(bundle);
                    fragment = searchFt;
                    break;

            }

            return fragment;
        }

    }


}