package com.zncm.js.utils.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.zncm.js.data.CheckListData;
import com.zncm.js.data.LikeData;
import com.zncm.js.data.ProgressData;
import com.zncm.js.data.ProjectData;
import com.zncm.js.data.RemindData;
import com.zncm.js.data.TaskData;


public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String DATABASE_NAME = "MxGTD.db";
    private static final int DATABASE_VERSION = 11;
    private RuntimeExceptionDao<ProjectData, Integer> pjDao = null;
    private RuntimeExceptionDao<TaskData, Integer> tkDao = null;
    private RuntimeExceptionDao<ProgressData, Integer> progressDao = null;
    private RuntimeExceptionDao<CheckListData, Integer> clDao = null;
    private RuntimeExceptionDao<RemindData, Integer> rdDao = null;
    private RuntimeExceptionDao<LikeData, Integer> likeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onCreate");
            TableUtils.createTableIfNotExists(connectionSource, ProjectData.class);
            TableUtils.createTableIfNotExists(connectionSource, TaskData.class);
            TableUtils.createTableIfNotExists(connectionSource, ProgressData.class);
            TableUtils.createTableIfNotExists(connectionSource, CheckListData.class);
            TableUtils.createTableIfNotExists(connectionSource, RemindData.class);
            TableUtils.createTableIfNotExists(connectionSource, LikeData.class);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            onCreate(db, connectionSource);
        } catch (Exception e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }


    public RuntimeExceptionDao<ProjectData, Integer> getPjDao() {
        if (pjDao == null) {
            pjDao = getRuntimeExceptionDao(ProjectData.class);
        }
        return pjDao;
    }

    public RuntimeExceptionDao<TaskData, Integer> getTkDao() {
        if (tkDao == null) {
            tkDao = getRuntimeExceptionDao(TaskData.class);
        }
        return tkDao;
    }

    public RuntimeExceptionDao<ProgressData, Integer> getProgressDao() {
        if (progressDao == null) {
            progressDao = getRuntimeExceptionDao(ProgressData.class);
        }
        return progressDao;
    }


    public RuntimeExceptionDao<CheckListData, Integer> getClDao() {
        if (clDao == null) {
            clDao = getRuntimeExceptionDao(CheckListData.class);
        }
        return clDao;
    }

    public RuntimeExceptionDao<RemindData, Integer> getRdDao() {
        if (rdDao == null) {
            rdDao = getRuntimeExceptionDao(RemindData.class);
        }
        return rdDao;
    }


    public RuntimeExceptionDao<LikeData, Integer> getLikeDao() {
        if (likeDao == null) {
            likeDao = getRuntimeExceptionDao(LikeData.class);
        }
        return likeDao;
    }

    @Override
    public void close() {
        super.close();
        pjDao = null;
        tkDao = null;
        progressDao = null;
        clDao = null;
        rdDao = null;
        likeDao = null;
    }
}
