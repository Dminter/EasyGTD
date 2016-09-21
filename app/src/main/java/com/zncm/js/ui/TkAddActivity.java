package com.zncm.js.ui;

import android.os.Bundle;

import com.zncm.js.R;
import com.zncm.js.ft.TkAddFragment;


public class TkAddActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, new TkAddFragment())
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }
}
