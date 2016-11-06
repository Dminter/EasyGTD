package com.zncm.mxgtd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.ft.RefreshEvent;
import com.zncm.mxgtd.ft.TaskFragment;
import com.zncm.mxgtd.utils.ColorGenerator;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.XUtil;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;


public class ProjectMainFt extends Fragment {
    private ViewPager mViewPager;
    ArrayList<ProjectData> datas = new ArrayList<>();
    ColorGenerator generator;
    int count = 0;
    public ArrayList<TaskFragment> fragments = new ArrayList<>();
    MyPagerAdapter adapter;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_pj, null);
        initView();
        return view;
    }


    private void initView() {
        datas = DbUtils.getProjectDataAll();
        count = datas.size();
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);
        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) view.findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        XUtil.viewPagerRandomAnimation(mViewPager);
        XUtil.initIndicatorTheme(indicator);
        EventBus.getDefault().register(this);

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


        @Override
        public Fragment getItem(int position) {
            TaskFragment fragment = new TaskFragment();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.PjInfo.getValue()) {
//            Intent intent = new Intent(ctx, ProjectMainFt.class);
//            startActivity(intent);
//            finish();
        }

        if (type == EnumData.RefreshEnum.TASK.getValue()) {
            TaskFragment tmp = (TaskFragment) fragments.get(mViewPager.getCurrentItem());
            tmp.onRefresh();
        }
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

