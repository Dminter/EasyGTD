package com.zncm.mxgtd.ui;

import android.content.Intent;
import android.os.Bundle;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.ft.ItemsDetailsFt;
import com.zncm.mxgtd.ft.LikeFragment;
import com.zncm.mxgtd.utils.XUtil;


public class ItemDetailsActivity extends BaseActivity {

    ItemsDetailsFt detailsFt;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        finish();
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackOn(false);
        detailsFt = new ItemsDetailsFt();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailsFt)
                .commit();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }
}
