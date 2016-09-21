package com.zncm.js.ui;

import android.os.Bundle;

import com.zncm.js.R;
import com.zncm.js.ft.SettingFragment;


public class MySettingActivity extends BaseActivity {
    SettingFragment settingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("设置");
        settingFragment = new SettingFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, settingFragment)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }
}
