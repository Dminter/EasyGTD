package com.zncm.mxgtd.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.malinskiy.materialicons.Iconify;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ft.DetailsFragment;
import com.zncm.mxgtd.ft.RefreshEvent;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.XUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;


public class TkDetailsActivity extends BaseActivity {
    private TaskData taskData;

    DetailsFragment detailsFragment;
    public String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


        detailsFragment = new DetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_PARAM_DATA, taskData);
        if (XUtil.notEmptyOrNull(query)) {
            bundle.putString("query", query);
        }
        detailsFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailsFragment)
                .commit();

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_add;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(RefreshEvent event) {
        int type = event.type;
        if (type == EnumData.RefreshEnum.TASK.getValue()) {
            detailsFragment.onRefresh();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);


        if (item == null || item.getTitle() == null) {
            return false;
        }

        if (item.getTitle().equals("previous")) {
            dayChange(false);
        } else if (item.getTitle().equals("next")) {
            dayChange(true);
        } else if (item.getTitle().equals("edit")) {
            Intent newIntent = null;
            newIntent = new Intent(ctx, TkAddActivity.class);
            newIntent.putExtra(Constant.KEY_PARAM_DATA, taskData);
            startActivity(newIntent);
        }


        return true;
    }

    private void dayChange(boolean dayAdd) {
        Long dayStart = XUtil.dateToLong(query.replace("@", ""));
        if (dayAdd) {
            dayStart += Constant.DAY;
        } else {
            dayStart -= Constant.DAY;
        }
        query = "@" + XUtil.getDateYMDNoDiv(dayStart);
        toolbar.setTitle(query);
        detailsFragment.onRefresh();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (XUtil.notEmptyOrNull(query) && query.startsWith("@")) {
            menu.add("previous").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_navigate_before)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.add("next").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_navigate_next)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        } else if (!XUtil.notEmptyOrNull(query)) {
            menu.add("edit").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_mode_edit)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }


}
