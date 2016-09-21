package com.zncm.js.ui;

import android.content.Intent;
import android.os.Bundle;

import com.zncm.js.R;
import com.zncm.js.data.Constant;
import com.zncm.js.data.EnumData;
import com.zncm.js.data.TaskData;
import com.zncm.js.ft.DetailsFragment;
import com.zncm.js.ft.RefreshEvent;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.XUtil;

import java.io.Serializable;

import de.greenrobot.event.EventBus;


public class TkDetailsActivity extends BaseActivity {
    public TaskData taskData;

    DetailsFragment detailsFragment;
    public String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailsFragment = new DetailsFragment();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailsFragment)
                .commit();
        Serializable dataParam = getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);

        query = getIntent().getStringExtra("query");

        taskData = (TaskData) dataParam;
        if (taskData == null) {
            int tk_id = getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            taskData = DbUtils.getTkById(tk_id);
        }
        if (taskData != null && XUtil.notEmptyOrNull(taskData.getTitle())) {
            toolbar.setTitle(taskData.getTitle());
        } else {
            if (XUtil.notEmptyOrNull(query)) {
                toolbar.setTitle(query);
            }
        }
        EventBus.getDefault().register(this);

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.TASK.getValue()) {
            detailsFragment.onRefresh();
        }
    }
}
