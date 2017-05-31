package com.zncm.mxgtd.ft;

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
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.utils.ColorGenerator;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.WrapContentHeightViewPager;

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
        if (!XUtil.listNotNull(datas)){
            /**
             *导入数据后默认项目无法对应问题
             * 获取最后正常任务为默认任务，对应的项目为默认项目
             */
            TaskData taskData = DbUtils.getTkById(DbUtils.getMaxTk());
            if (taskData!=null){
                MySp.setDefaultTk(taskData.getId());
                MySp.setDefaultPj(taskData.getPj_id());
                datas = DbUtils.getTaskDataByDefaultPj();
            }
        }
        count = datas.size();
        mViewPager = (WrapContentHeightViewPager) view.findViewById(R.id.pager);
        adapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(0);

        /**
         *使用support TabLayout 代替 PagerSlidingTabStrip
         */
        TabLayout mTabLayout = (TabLayout)view.findViewById(R.id.mTabLayout);
        XUtil.initTabLayout(getActivity(),mTabLayout);
        mTabLayout.setupWithViewPager(mViewPager);

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


//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(RefreshEvent event) {
//        int type = event.type;
//        if (type == EnumData.RefreshEnum.MAIN_ITEM.getValue()) {
//            XUtil.debug("MAIN_ITEM=====MAIN_ITEMMAIN_ITEM");
//            fragments.get(mViewPager.getCurrentItem()).onRefresh();
//        }
//    }
//
//
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        EventBus.getDefault().unregister(this);
//    }
}

