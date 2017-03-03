package com.zncm.mxgtd.ui;

import android.os.Bundle;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.ft.PjAddFragment;


public class PjAddActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("添加笔记本组");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new PjAddFragment())
                .commit();
    }
    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }
}
