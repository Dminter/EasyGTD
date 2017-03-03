//package com.zncm.mxgtd.ui;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//
//import com.astuetz.PagerSlidingTabStrip;
//import com.miguelcatalan.materialsearchview.MaterialSearchView;
//import com.mikepenz.materialdrawer.Drawer;
//import com.mikepenz.materialdrawer.DrawerBuilder;
//import com.mikepenz.materialdrawer.model.DividerDrawerItem;
//import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
//import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
//import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
//import com.zncm.mxgtd.R;
//import com.zncm.mxgtd.data.Constant;
//import com.zncm.mxgtd.data.EnumData;
//import com.zncm.mxgtd.data.ProjectData;
//import com.zncm.mxgtd.ft.RefreshEvent;
//import com.zncm.mxgtd.ft.TaskFragment;
//import com.zncm.mxgtd.utils.ColorGenerator;
//import com.zncm.mxgtd.utils.DbUtils;
//import com.zncm.mxgtd.utils.XUtil;
//
//import java.util.ArrayList;
//
//import de.greenrobot.event.EventBus;
//
//
//public class ProjectMainActivity extends BaseActivity {
//    private ViewPager mViewPager;
//    ArrayList<ProjectData> datas = new ArrayList<>();
//    ColorGenerator generator;
//    int count = 0;
//    public ArrayList<TaskFragment> fragments = new ArrayList<>();
//    MyPagerAdapter adapter;
//    MaterialSearchView searchView;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        datas = DbUtils.getProjectDataAll();
//        count = datas.size();
//        mViewPager = (ViewPager) findViewById(R.id.pager);
//        adapter = new MyPagerAdapter(getSupportFragmentManager());
//        mViewPager.setAdapter(adapter);
//        mViewPager.setOffscreenPageLimit(3);
//        mViewPager.setCurrentItem(0);
//        PagerSlidingTabStrip indicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
//        indicator.setViewPager(mViewPager);
//        XUtil.viewPagerRandomAnimation(mViewPager);
//        XUtil.initIndicatorTheme(indicator);
//        EventBus.getDefault().register(this);
//        toolbar.setTitle(getString(R.string.app_name));
//        toolbar.setNavigationIcon(null);
//        searchView = (MaterialSearchView) findViewById(R.id.search_view);
//        searchView.setHint("搜索");
//        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                if (XUtil.notEmptyOrNull(query)) {
//                    Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
//                    newIntent.putExtra("query", query);
//                    startActivity(newIntent);
//                }
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                return false;
//            }
//        });
//
//        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
//            @Override
//            public void onSearchViewShown() {
//            }
//
//            @Override
//            public void onSearchViewClosed() {
//            }
//        });
//
//        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(0).withName("记录你的生活！");
//        new DrawerBuilder()
//                .withActivity(this)
//                .withToolbar(toolbar)
//                .addDrawerItems(
//                        item1,
//                        new DividerDrawerItem(),
//                        new SecondaryDrawerItem().withName("笔记本组"),
//                        new SecondaryDrawerItem().withName("提醒"),
//                        new SecondaryDrawerItem().withName("收藏"),
//                        new SecondaryDrawerItem().withName("设置")
//                )
//                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
//                    @Override
//                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
//                        // do something with the clicked item :D
//                        switch (position) {
//                            case 2:
//                                Intent intent = new Intent(ctx, PjActivity.class);
//                                startActivity(intent);
//                                break;
//                            case 3:
//                                intent = new Intent(ctx, RdActivity.class);
//                                startActivity(intent);
//                                break;
//                            case 4:
//                                intent = new Intent(ctx, LikeActivity.class);
//                                startActivity(intent);
//
//                                break;
//                            case 5:
//                                intent = new Intent(ctx, MySettingActivity.class);
//                                startActivity(intent);
//                                break;
//                        }
//
//                        return true;
//                    }
//                })
//                .build();
//    }
//
//
//    public class MyPagerAdapter extends FragmentPagerAdapter {
//
//
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            String title = datas.get(position).getTitle();
//
//            return title;
//        }
//
//        @Override
//        public int getCount() {
//            return count;
//        }
//
//
//        @Override
//        public Fragment getItem(int position) {
//            TaskFragment fragment = new TaskFragment();
//            Bundle bundle = new Bundle();
//            bundle.putSerializable(Constant.KEY_PARAM_DATA, datas.get(position));
//            fragment.setArguments(bundle);
//            fragments.add(fragment);
//            return fragment;
//        }
//
//        @Override
//        public int getItemPosition(Object object) {
//            return POSITION_NONE;
//        }
//
//
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//
//    @Override
//    protected int getLayoutResource() {
//        return R.layout.activity_toolbar_tabs;
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem item = menu.findItem(R.id.action_search);
//        searchView.setMenuItem(item);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        EventBus.getDefault().unregister(this);
//    }
//
//    public void onEvent(RefreshEvent event) {
//        int type = event.type;
//        if (type == EnumData.RefreshEnum.PjInfo.getValue()) {
//            Intent intent = new Intent(ctx, ProjectMainActivity.class);
//            startActivity(intent);
//            finish();
//        }
//
//        if (type == EnumData.RefreshEnum.TASK.getValue()) {
//            TaskFragment tmp = (TaskFragment) fragments.get(mViewPager.getCurrentItem());
//            tmp.onRefresh();
//        }
//    }
//
//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
//            backToDesk(this);
//            return true;
//        }
//        return super.dispatchKeyEvent(event);
//    }
//
//    public static void backToDesk(Activity activity) {
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        activity.startActivity(intent);
//    }
//}
//
