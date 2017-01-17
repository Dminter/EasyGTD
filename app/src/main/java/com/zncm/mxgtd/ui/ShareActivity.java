package com.zncm.mxgtd.ui;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ft.RemindFragment;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;

import java.util.Random;

/**
 * Created by dminter on 2016/11/21.
 */

public class ShareActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEND)) {
                Bundle extras = intent.getExtras();
                String content = String.valueOf(extras.getString(Intent.EXTRA_TEXT));
                if (XUtil.notEmptyOrNull(content)) {
                    boolean flag = DbUtils.insertProgress(content);
                    if (flag) {
                        XUtil.tShort("mxgtd got!");
                    }
                }
                finish();
            }
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_quickadd;
    }
}
