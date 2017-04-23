package com.zncm.mxgtd.ui;

import android.content.Intent;
import android.os.Bundle;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.ft.TextFt;
import com.zncm.mxgtd.utils.PlayRingTone;
import com.zncm.mxgtd.utils.XUtil;

import java.io.Serializable;


public class TextActivity extends BaseActivity {
    TextFt textFt;
    String text;

    DetailsData data;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        text = intent.getStringExtra("text");
        initData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        textFt = new TextFt();
        Serializable dataParam = getIntent().getExtras().getSerializable(Constant.KEY_PARAM_DATA);
        if (dataParam != null && dataParam instanceof DetailsData) {
            data = (DetailsData) dataParam;
        }
        if (!XUtil.notEmptyOrNull(text)) {
            text = getIntent().getStringExtra("text");
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("text", text);
        textFt.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, textFt)
                .commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        PlayRingTone.stopRing();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }

}
