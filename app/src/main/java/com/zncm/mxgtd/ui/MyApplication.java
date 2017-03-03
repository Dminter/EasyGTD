package com.zncm.mxgtd.ui;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.v7.app.AppCompatDelegate;

import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;

public class MyApplication extends Application {
    public Context ctx;
    public static MyApplication instance;
    ClipboardManager cb;
    private String myLastClipboard = "";

    @Override
    public void onCreate() {
        super.onCreate();
        this.ctx = this.getApplicationContext();
        instance = this;
        cb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cb.setPrimaryClip(ClipData.newPlainText("", ""));
        cb.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
            @Override
            public void onPrimaryClipChanged() {
                String content = cb.getText().toString();
                if (MySp.getClipboardListen() && XUtil.notEmptyOrNull(content)) {
                    if (!XUtil.notEmptyOrNull(myLastClipboard) || !myLastClipboard.equals(content)) {
                        myLastClipboard = content;
                        boolean flag = DbUtils.insertProgress(content);
                        if (flag) {
                            XUtil.tShort("mxgtd got!");
                        }
                    }
                }
            }
        });
    }

    public static void updateNightMode(boolean on) {
        if (on) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
