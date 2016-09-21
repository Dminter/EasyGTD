package com.zncm.js.ui;

import android.os.Bundle;

import com.zncm.js.R;
import com.zncm.js.ft.PjAddFragment;


public class PjAddActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("添加分组");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PjAddFragment())
                .commit();
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }
}
