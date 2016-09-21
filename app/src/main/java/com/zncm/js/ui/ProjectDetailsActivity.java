package com.zncm.js.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.astuetz.PagerSlidingTabStrip;
import com.malinskiy.materialicons.Iconify;
import com.zncm.js.R;
import com.zncm.js.data.Constant;
import com.zncm.js.data.EnumData;
import com.zncm.js.data.ProjectData;
import com.zncm.js.ft.PjAddFragment;
import com.zncm.js.ft.RefreshEvent;
import com.zncm.js.ft.TaskFragment;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.MySp;
import com.zncm.js.utils.XUtil;

import java.io.Serializable;

import de.greenrobot.event.EventBus;


public class ProjectDetailsActivity extends BaseActivity {
    private TaskFragment taskFinishFragment;
    private TaskFragment taskFragment;
    private PjAddFragment detailsFragment;
    private ViewPager mViewPager;
    private String TITLES[] = new String[]{"进行中", "已完成", "详情"};
    private ProjectData projectData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mViewPager.setOffscreenPageLimit(TITLES.length);
        mViewPager.setCurrentItem(0);
        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        indicator.setViewPager(mViewPager);
        XUtil.viewPagerRandomAnimation(mViewPager);
        XUtil.initIndicatorTheme(indicator);
        EventBus.getDefault().register(this);
        Serializable dataParam = getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
        projectData = (ProjectData) dataParam;
        if (projectData == null) {
            int tk_id = getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            projectData = DbUtils.getPjById(tk_id);
        }
        if (projectData == null) {
            int tk_id = getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            projectData = DbUtils.getPjById(tk_id);
        }
        if (projectData != null && XUtil.notEmptyOrNull(projectData.getTitle())) {
            String status = "";
            if (EnumData.StatusEnum.OFF.getValue() == projectData.getStatus()) {
                status = "F";
            } else if (EnumData.StatusEnum.DEL.getValue() == projectData.getStatus()) {
                status = "D";
            }
            toolbar.setTitle("P" + status + " " + projectData.getTitle());
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
                    taskFragment = new TaskFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.ON.getValue());
                    taskFragment.setArguments(bundle);
                    bundle.putSerializable(Constant.KEY_PARAM_DATA, projectData);
                    fragment = taskFragment;
                    break;
                case 1:
                    taskFinishFragment = new TaskFragment();
                    bundle = new Bundle();
                    bundle.putInt(Constant.KEY_PARAM_TYPE, EnumData.StatusEnum.OFF.getValue());
                    taskFinishFragment.setArguments(bundle);
                    bundle.putSerializable(Constant.KEY_PARAM_DATA, projectData);
                    fragment = taskFinishFragment;
                    break;
                case 2:
                    detailsFragment = new PjAddFragment();
                    fragment = detailsFragment;
                    break;

            }

            return fragment;
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.TASK.getValue()) {
            taskFragment.onRefresh();
            taskFinishFragment.onRefresh();
        }
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_toolbar_tabs;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        switch (item.getItemId()) {
            case 2:
                MySp.setDefaultPj(projectData.getId());
                XUtil.tShort("设置成功~");
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
        sub.add(0, 2, 0, "设为默认分组");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
}
