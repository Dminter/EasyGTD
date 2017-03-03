package com.zncm.mxgtd.ui;

import android.os.Bundle;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.ft.LikeFragment;


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
