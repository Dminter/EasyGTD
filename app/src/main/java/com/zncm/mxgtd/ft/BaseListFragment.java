package com.zncm.mxgtd.ft;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.malinskiy.materialicons.Iconify;
import com.melnykov.fab.FloatingActionButton;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.CheckListData;
import com.zncm.mxgtd.data.LikeData;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.utils.db.DatabaseHelper;
import com.zncm.mxgtd.view.MyLinearLayoutManager;
import com.zncm.mxgtd.view.loadmore.LoadMoreRecyclerView;

import java.util.Random;


public abstract class BaseListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, LoadMoreRecyclerView.LoadMoreListener {
    protected SwipeRefreshLayout swipeLayout;
    protected LoadMoreRecyclerView listView;
    protected View view;
    protected FloatingActionButton addButton;
    protected LayoutInflater mInflater;
    private DatabaseHelper databaseHelper = null;
    protected RuntimeExceptionDao<ProgressData, Integer> progressDao;
    protected RuntimeExceptionDao<CheckListData, Integer> clDao;
    protected RuntimeExceptionDao<TaskData, Integer> taskDao;
    protected RuntimeExceptionDao<ProjectData, Integer> projectDao;
    protected RuntimeExceptionDao<RemindData, Integer> rdDao;
    protected RuntimeExceptionDao<LikeData, Integer> likeDao = null;

    public DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInflater = inflater;
        view = inflater.inflate(R.layout.fragment_base, null);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(
                MySp.getTheme()
        );
        swipeLayout.setBackgroundColor(getResources().getColor(R.color.white));
        listView = (LoadMoreRecyclerView) view.findViewById(R.id.listView);
        listView.setHasFixedSize(true);
        addButton = (FloatingActionButton) view.findViewById(R.id.button_floating_action);
        addButton.setImageDrawable(XUtil.initIconWhite(Iconify.IconValue.md_add));
        addButton.setColorNormal(MySp.getTheme());
        addButton.setColorPressed(MySp.getTheme() + new Random().nextInt(10000));
        listView.setLayoutManager(new StaggeredGridLayoutManager(MySp.getSpanCount(), StaggeredGridLayoutManager.VERTICAL));
        listView.setAutoLoadMoreEnable(true);
        listView.setLoadMoreListener(this);
        if (progressDao == null) {
            progressDao = getHelper().getProgressDao();
        }
        if (taskDao == null) {
            taskDao = getHelper().getTkDao();
        }
        if (projectDao == null) {
            projectDao = getHelper().getPjDao();
        }
        if (clDao == null) {
            clDao = getHelper().getClDao();
        }
        if (rdDao == null) {
            rdDao = getHelper().getRdDao();
        }
        if (likeDao == null) {
            likeDao = getHelper().getLikeDao();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


}
