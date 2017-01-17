package com.zncm.mxgtd.ft;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.utils.db.DatabaseHelper;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseDbFragment extends Fragment {
    private DatabaseHelper databaseHelper = null;

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
