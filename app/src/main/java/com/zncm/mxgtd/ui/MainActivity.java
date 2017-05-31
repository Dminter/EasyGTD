package com.zncm.mxgtd.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.color.ColorChooserDialog;
import com.malinskiy.materialicons.Iconify;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.ft.RefreshEvent;
import com.zncm.mxgtd.ft.TasksFt;
import com.zncm.mxgtd.utils.AlipayZeroSdk;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.DoubleClickImp;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.PlayRingTone;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.utils.statusbar.StatusBarCompat;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends BaseActivity implements ColorChooserDialog.ColorCallback {
    private Fragment fragment = null;
    private Map<Integer, Fragment> fragments = new HashMap<>();
    FragmentManager fragmentManager;
    MaterialSearchView searchView;
    BottomBar bottomBar;


    RelativeLayout rlFingerprint;
    private boolean mIsCalledStartIdentify = false;
    private FingerprintIdentify mFingerprintIdentify;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackOn(false);
        MyApplication.updateNightMode(MySp.getIsNight());
        initTitle();
        fragments.put(R.id.tab_done, new TasksFt());
        fragments.put(R.id.tab_remind, new RdFt());
        fragments.put(R.id.tab_book, new ProjectMainFt());
        fragmentManager = getSupportFragmentManager();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        rlFingerprint = (RelativeLayout) findViewById(R.id.rlFingerprint);
        if (MySp.getIsNight()){
            bottomBar.setActiveTabColor(getResources().getColor(R.color.material_light_black));
        }else {
            bottomBar.setActiveTabColor(MySp.getTheme());
        }
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                tabSelected(tabId);
            }
        });
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setHint("搜索笔记");
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (XUtil.notEmptyOrNull(query)) {
                    Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
                    newIntent.putExtra("query", query);
                    startActivity(newIntent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
            }
        });
        ArrayList<ProjectData> pjList = DbUtils.getProjectDataAll();
        if (!XUtil.listNotNull(pjList)) {
            initNoData();
        }
    }

    private void initNoData() {
        DbUtils.savePj("生活");
        DbUtils.savePj("工作");
        DbUtils.savePj("学习");
        DbUtils.savePj("积累");
        DbUtils.savePj("健身");
        DbUtils.savePj("旅游");
        DbUtils.saveTk("便签");
        DbUtils.saveTk("记录");
        DbUtils.saveTk("电影");
        DbUtils.saveTk("书籍");
        DbUtils.saveTk("购物");
        DbUtils.saveTk("旅行");
        DbUtils.saveTk("清单");
        DbUtils.saveTk("日记");
        DbUtils.saveTk("跑步");
        DbUtils.saveTk("日记");
        DbUtils.saveTk("娱乐");
        DbUtils.saveRd(1);
        DbUtils.saveRd(5);
        DbUtils.saveRd(30);
        DbUtils.saveRd(60);
        DbUtils.saveRd(1000);
        DbUtils.saveRd(2000);
        XUtil.tShort("数据初始化完毕~");
        EventBus.getDefault().post(EnumData.RefreshEnum.MAIN.getValue());
    }


    @Override
    protected void onResume() {
        super.onResume();
        PlayRingTone.stopRing();


        if (MySp.getIsAutoNight()) {
            int hour = XUtil.getHour();
            boolean flag = false;
            boolean isNightMode = MySp.getIsNight();

            if (hour >= 6 && hour < 18 ) {
                if (isNightMode){
                    flag = true;
                }
                MySp.setIsNight( false);
            } else {
                if (!isNightMode){
                    flag = true;
                }
                MySp.setIsNight( true);
            }

            if (flag){
                initActicity();
            }

        }


        lock();



    }

    private void lock() {
        if (!MySp.getIsLock()){
            rlFingerprint.setVisibility(View.GONE);
            return;
        }


        if (rlFingerprint.getVisibility()==View.VISIBLE){
            StatusBarCompat.setStatusBarColor(this, getResources().getColor(R.color.white));
        }

        if (mIsCalledStartIdentify) {
            mFingerprintIdentify.resumeIdentify();
            return;
        }
//
        mIsCalledStartIdentify = true;
        mFingerprintIdentify = new FingerprintIdentify(this, new BaseFingerprint.FingerprintIdentifyExceptionListener() {
            @Override
            public void onCatchException(Throwable exception) {

            }
        });


        if (!mFingerprintIdentify.isFingerprintEnable()) {
            XUtil.tShort("Sorry →_→");
//            finish();
            return;
        }
        mFingerprintIdentify.resumeIdentify();
        mFingerprintIdentify.startIdentify(3, new BaseFingerprint.FingerprintIdentifyListener() {
            @Override
            public void onSucceed() {
                rlFingerprint.setVisibility(View.GONE);
                StatusBarCompat.setStatusBarColor(MainActivity.this, MySp.getTheme());

            }

            @Override
            public void onNotMatch(int availableTimes) {
                XUtil.tShort("Sorry →_→ onNotMatch");
            }

            @Override
            public void onFailed() {
                XUtil.tShort("Sorry →_→ onFailed");
//                finish();
            }
        });
    }

    private void initTitle() {

        toolbar.setTitle(getResources().getString(R.string.app_name));
        toolbar.setNavigationIcon(null);
        DoubleClickImp.registerDoubleClickListener(toolbar,
                new DoubleClickImp.OnDoubleClickListener() {
                    @Override
                    public void OnSingleClick(View v) {
                        XUtil.tShort("双击刷新主页~");
                    }

                    @Override
                    public void OnDoubleClick(View v) {
                        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.MAIN.getValue()));
                    }
                });
    }


    private void tabSelected(@IdRes int tabId) {
        fragment = fragmentManager.findFragmentByTag(String.valueOf(tabId));
        if (fragment != null) {
            fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
        } else {
            fragment = fragments.get(tabId);
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment, String.valueOf(tabId))
                    .commit();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_uimain;
    }


//    public boolean dispatchKeyEvent(KeyEvent event) {
//        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
////            backToDesk(this);
//            finish();
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
        sub.add(0, 5, 0, "回顾今天");
        sub.add(0, 6, 0, "收藏");
        sub.add(0, 1, 0, "设置");
        sub.add(0, 2, 0, "白天/夜间");
        sub.add(0, 7, 0, "主题色");
        sub.add(0, 4, 0, "首页刷新");
        sub.add(0, 3, 0, "打赏");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }
    private void initActicity() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == null || item.getTitle() == null) {
            return false;
        }
        switch (item.getItemId()) {
            case 1:
                startActivity(new Intent(ctx, SettingNew.class));
                break;
            case 2:
                MySp.setIsNight(!MySp.getIsNight());
                initActicity();
                break;
            case 3:
                if (AlipayZeroSdk.hasInstalledAlipayClient(ctx)) {
                    AlipayZeroSdk.startAlipayClient(this, "aex02461t5uptlcygocfsbc");
                } else {
                    XUtil.tShort("请先安装支付宝~");
                }
                break;
            case 4:
                finish();
                startActivity(new Intent(ctx, MainActivity.class));
                break;
            case 5:
                Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
                newIntent.putExtra("query", "@" + XUtil.getDateYMDNoDiv(new Date().getTime()));
                startActivity(newIntent);
                break;
            case 6:
                startActivity(new Intent(ctx, LikeActivity.class));
                break;
            case 7:
                new ColorChooserDialog.Builder(this, R.string.color_palette)
                        .accentMode(false)  // when true, will display accent palette instead of primary palette
                        .doneButton(R.string.md_done_label)  // changes label of the done button
                        .cancelButton(R.string.md_cancel_label)  // changes label of the cancel button
                        .backButton(R.string.md_back_label)  // changes label of the back button
                        .preselect(MySp.getTheme())  // optionally preselects a color
                        .dynamicButtonColor(true)  // defaults to true, false will disable changing action buttons' color to currently selected color
                        .show();
                break;
        }

        return false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.MAIN.getValue()) {
            finish();
            Intent intent = new Intent(ctx, MainActivity.class);
            startActivity(intent);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (mFingerprintIdentify!=null){
        mFingerprintIdentify.cancelIdentify();
        }
    }



    @Override
    public void onColorSelection(@NonNull ColorChooserDialog dialog, @ColorInt int selectedColor) {
        MySp.setTheme(selectedColor);
        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.MAIN.getValue()));
    }
}
