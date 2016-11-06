package com.zncm.mxgtd.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.ft.LikeFragment;
import com.zncm.mxgtd.ft.RemindFragment;
import com.zncm.mxgtd.ft.SettingFragment;
import com.zncm.mxgtd.ft.TaskFragment;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;

import java.util.HashMap;
import java.util.Map;


public class UIMainActivity extends BaseActivity {
    private Fragment fragment = null;

    private Map<Integer, Fragment> fragments = new HashMap<>();

    FragmentManager fragmentManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        TaskData taskData = DbUtils.getTkById(MySp.getDefaultTk());
        bundle.putSerializable(Constant.KEY_PARAM_DATA, taskData);
        detailsFragment.setArguments(bundle);
        fragments.put(R.id.tab_done, detailsFragment);
//        fragments.put(R.id.tab_done, new ProjectMainFt());
        fragments.put(R.id.tab_remind, new RdActivity());
        fragments.put(R.id.tab_like, new LikeFragment());
        fragments.put(R.id.tab_book, new ProjectMainFt());
        fragments.put(R.id.tab_setting, new SettingFragment());

        fragmentManager = getSupportFragmentManager();

        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

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
        });
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_uimain;
    }


    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
            backToDesk(this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

    public static void backToDesk(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(intent);
    }
}
