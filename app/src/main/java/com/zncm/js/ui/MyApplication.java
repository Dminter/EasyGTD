package com.zncm.js.ui;

import android.app.Application;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.zncm.js.data.SpConstant;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.MySp;
import com.zncm.js.utils.XUtil;

public class MyApplication extends Application {
    public Context ctx;
    public static MyApplication instance;
    ClipboardManager cb;

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
                    DbUtils.insertProgress(content);
                    boolean isClipboardTip = MySp.get(SpConstant.isClipboardTip, Boolean.class, false);
                    if (isClipboardTip) {
                        XUtil.tShort("mxgtd got!");
                    }
                }
            }
        });
    }

    public static MyApplication getInstance() {
        return instance;
    }

}
