package com.zncm.mxgtd.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.jude.swipbackhelper.SwipeBackHelper;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.utils.XUtil;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseActivity extends AppCompatActivity {
    protected Context ctx;
    public Toolbar toolbar;
    public boolean swipeBackOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (swipeBackOn) {
            SwipeBackHelper.onCreate(this);
        }
        ctx = this;
        if (getLayoutResource() != -1) {
            setContentView(getLayoutResource());
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            toolbar.setTitleTextColor( ctx.getResources().getColor(R.color.white));
//            toolbar.setBackgroundColor(getResources().getColor(R.color.material_blue_400));
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.drawable.md_nav_back);
            XUtil.initBarTheme(this,toolbar);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (swipeBackOn) {

            SwipeBackHelper.onPostCreate(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (swipeBackOn) {

            SwipeBackHelper.onDestroy(this);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return false;
    }

    protected abstract int getLayoutResource();

    public boolean isSwipeBackOn() {
        return swipeBackOn;
    }

    public void setSwipeBackOn(boolean swipeBackOn) {
        this.swipeBackOn = swipeBackOn;
    }
}
