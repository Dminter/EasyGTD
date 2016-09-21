package com.zncm.js.ui;

import android.os.Bundle;

import com.zncm.js.R;
import com.zncm.js.ft.LikeFragment;


public class LikeActivity extends BaseActivity {
    LikeFragment likeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setTitle("收藏");
        likeFragment = new LikeFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, likeFragment)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }
}
