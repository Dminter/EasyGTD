package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ft.BaseFragment;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.ft.RefreshEvent;
import com.zncm.mxgtd.utils.ColorGenerator;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.WrapContentHeightViewPager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


public class TasksFt extends BaseFragment {
    private WrapContentHeightViewPager mViewPager;
    ArrayList<TaskData> datas = new ArrayList<>();
    ColorGenerator generator;
    int count = 0;
    public ArrayList<DetailsFragment> fragments = new ArrayList<>();
    MyPagerAdapter adapter;
    View view;

    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pj, null);
        initView();
        return view;
    }


    private void initView() {
        datas = DbUtils.getTaskDataByDefaultPj();
        count = datas.size();
        mViewPager = (WrapContentHeightViewPager) view.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        XUtil.viewPagerRandomAnimation(mViewPager);
        XUtil.initIndicatorTheme(indicator);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(MySp.getTheme());
        }


//        Intent newIntent = new Intent(getActivity(), TkDetailsActivity.class);
//        newIntent.putExtra("query", query);
//        startActivity(newIntent);
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            String title = datas.get(position).getTitle();
            return title;
        }

        @Override
        public int getCount() {
            return count;
        }

//        Bundle bundle = new Bundle();
//        TaskData taskData = DbUtils.getTkById(MySp.getDefaultTk());
//        bundle.putSerializable(Constant.KEY_PARAM_DATA, taskData);


        @Override
        public Fragment getItem(int position) {
            DetailsFragment fragment = new DetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable(Constant.KEY_PARAM_DATA, datas.get(position));
            fragment.setArguments(bundle);
            fragments.add(fragment);
            return fragment;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


    }


//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//        return super.onCreateOptionsMenu(menu);
//    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.PjInfo.getValue()) {
//            Intent intent = new Intent(ctx, ProjectMainFt.class);
//            startActivity(intent);
//            finish();
        }

//        if (type == EnumData.RefreshEnum.TASK.getValue()) {
//            TaskFragment tmp = (TaskFragment) fragments.get(mViewPager.getCurrentItem());
//            tmp.onRefresh();
//        }
    }

//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
//            backToDesk(this);
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }

    public static void backToDesk(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }
}

