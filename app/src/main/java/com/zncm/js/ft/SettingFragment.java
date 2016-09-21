package com.zncm.js.ft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.js.R;
import com.zncm.js.adapter.SettingAdapter;
import com.zncm.js.data.CheckListData;
import com.zncm.js.data.Constant;
import com.zncm.js.data.EnumData;
import com.zncm.js.data.ProgressData;
import com.zncm.js.data.RemindData;
import com.zncm.js.data.SettingData;
import com.zncm.js.data.SpConstant;
import com.zncm.js.data.TaskData;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.MyPath;
import com.zncm.js.utils.MySp;
import com.zncm.js.utils.XUtil;
import com.zncm.js.view.loadmore.MxItemClickListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class SettingFragment extends BaseListFragment {
    private SettingAdapter mAdapter;
    private Activity ctx;
    private List<SettingData> datas = null;
    private File[] files = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();

//        listView.setlo(false);
        swipeLayout.setEnabled(false);
        addButton.setVisibility(View.GONE);

        datas = new ArrayList<SettingData>();
        mAdapter = new SettingAdapter(ctx) {
            @Override
            public void setData(int position, SettingViewHolder holder) {
                final SettingData data = datas.get(position);
                if (data == null) {
                    return;
                }
                holder.tvStatus.setVisibility(View.GONE);
                if (data.getTitle() != null && XUtil.notEmptyOrNull(data.getTitle().toString())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(data.getTitle());
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }
                if (data.getSummary() != null && XUtil.notEmptyOrNull(data.getSummary().toString())) {
                    holder.tvSummary.setVisibility(View.VISIBLE);
                    holder.tvSummary.setText(data.getSummary());
                } else {
                    holder.tvSummary.setVisibility(View.GONE);
                }
                if (data.getStatus() != null && XUtil.notEmptyOrNull(data.getStatus().toString())) {
                    holder.tvStatus.setVisibility(View.VISIBLE);
                    holder.tvStatus.setText(data.getStatus());
                } else {
                    holder.tvStatus.setVisibility(View.GONE);
                }

                holder.setClickListener(new MxItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position, boolean isLongClick) {
                                                if (isLongClick) {

                                                } else {
                                                    int curPosition = position;
                                                    if (curPosition >= 0 && curPosition < datas.size()) {
                                                        SettingData data = datas.get(curPosition);
                                                        if (data == null) {
                                                            return;
                                                        }
                                                        int ITEM = data.getId();
                                                        if (ITEM == EnumData.SettingEnum.LIST_MODEL.getValue()) {
                                                            if (MySp.getSimpleModel()) {
                                                                MySp.setSimpleModel(false);
                                                            } else {
                                                                MySp.setSimpleModel(true);
                                                            }
                                                            System.exit(0);
                                                        } else if (ITEM == EnumData.SettingEnum.THEME.getValue()) {
                                                        } else if (ITEM == EnumData.SettingEnum.FUNCTION_INTRODUCTION.getValue()) {
                                                            XUtil.function(ctx);
                                                        } else if (ITEM == EnumData.SettingEnum.DONATE.getValue()) {
                                                            XUtil.donateDlg(ctx);
                                                        } else if (ITEM == EnumData.SettingEnum.FEED_BACK.getValue()) {
                                                        } else if (ITEM == EnumData.SettingEnum.CHANGE_LOG.getValue()) {

                                                        } else if (ITEM == EnumData.SettingEnum.OPEN_SOURCE.getValue()) {
                                                            openSource();
                                                        } else if (ITEM == EnumData.SettingEnum.LIST_ANIMATION.getValue()) {
                                                            if (MySp.getListAnimation()) {
                                                                MySp.setListAnimation(false);
                                                            } else {
                                                                MySp.setListAnimation(true);
                                                            }
                                                            System.exit(0);
                                                        } else if (ITEM == EnumData.SettingEnum.VIEWPAGER_ANIMATION.getValue()) {
                                                            initViewpagerAnimation();

                                                        } else if (ITEM == EnumData.SettingEnum.BACK_UP.getValue()) {
                                                            backUpDbDo();
                                                        } else if (ITEM == EnumData.SettingEnum.RECOVER.getValue()) {
                                                            recoverDbDo();
                                                        } else if (ITEM == EnumData.SettingEnum.ALBUM_NUMCOLUMNS.getValue()) {
                                                            initAlbumNum();
                                                        } else if (ITEM == EnumData.SettingEnum.SETTING_PWD.getValue()) {

                                                        } else if (ITEM == EnumData.SettingEnum.SHOW_LUNAR.getValue()) {
                                                            if (MySp.getShowLunar()) {
                                                                MySp.setShowLunar(false);
                                                            } else {
                                                                MySp.setShowLunar(true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.CLIPBOARD_LISTEN.getValue()) {
                                                            if (MySp.getClipboardListen()) {
                                                                MySp.setClipboardListen(false);
                                                            } else {
                                                                MySp.setClipboardListen(true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.SYS_TIP.getValue()) {
                                                            if (MySp.getSysTip()) {
                                                                MySp.setSysTip(false);
                                                            } else {
                                                                MySp.setSysTip(true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.SHOW_FINISH.getValue()) {
                                                            if (MySp.getShowFinish()) {
                                                                MySp.setShowFinish(false);
                                                            } else {
                                                                MySp.setShowFinish(true);
                                                            }
                                                            System.exit(0);
                                                        } else if (ITEM == EnumData.SettingEnum.IS_CLIPBOARDTIP.getValue()) {
                                                            if (MySp.get(SpConstant.isClipboardTip, Boolean.class, false)) {
                                                                MySp.put(SpConstant.isClipboardTip, false);
                                                            } else {
                                                                MySp.put(SpConstant.isClipboardTip, true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.IS_CELLOPTIONSHOW.getValue()) {
                                                            if (MySp.get(SpConstant.isCellOptionShow, Boolean.class, false)) {
                                                                MySp.put(SpConstant.isCellOptionShow, false);
                                                            } else {
                                                                MySp.put(SpConstant.isCellOptionShow, true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.IS_ENTERSAVE.getValue()) {
                                                            if (MySp.get(SpConstant.isEnterSave, Boolean.class, false)) {
                                                                MySp.put(SpConstant.isEnterSave, false);
                                                            } else {
                                                                MySp.put(SpConstant.isEnterSave, true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.IS_CHECKLISTENTER.getValue()) {
                                                            if (MySp.get(SpConstant.isCheckListEnter, Boolean.class, false)) {
                                                                MySp.put(SpConstant.isCheckListEnter, false);
                                                            } else {
                                                                MySp.put(SpConstant.isCheckListEnter, true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.IS_DELETECONFIRM.getValue()) {
                                                            if (MySp.get(SpConstant.isDeleteConfirm, Boolean.class, true)) {
                                                                MySp.put(SpConstant.isDeleteConfirm, false);
                                                            } else {
                                                                MySp.put(SpConstant.isDeleteConfirm, true);
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.NEW_TASK.getValue()) {

                                                        } else if (ITEM == EnumData.SettingEnum.IS_LISTEN_NOTIFY.getValue()) {
                                                            if (MySp.get(SpConstant.isListenNotify, Boolean.class, true)) {
                                                                MySp.put(SpConstant.isListenNotify, false);
                                                            } else {
                                                                MySp.put(SpConstant.isListenNotify, true);
                                                                XUtil.tShort("需要打开系统通知读取权限~");
                                                            }
                                                            getData();
                                                        } else if (ITEM == EnumData.SettingEnum.IMPORT_FILE.getValue()) {
                                                            File tmpDir = new File("/storage/sdcard0/MxGTD/imp");
                                                            if (tmpDir.exists() && tmpDir.length() > 0) {
                                                                files = tmpDir.listFiles();
                                                            } else {
                                                                XUtil.tShort("还没有备份,请先备份~");
                                                                return;
                                                            }
                                                            for (int i = 0; i < files.length; i++) {
                                                                XUtil.debug("files:" + files[i].getName());
                                                                importData(files[i].getPath());
                                                            }


                                                        } else if (ITEM == EnumData.SettingEnum.OUTPUT_TXT.getValue()) {
                                                            try {
                                                                QueryBuilder<TaskData, Integer> tkBuilder = taskDao.queryBuilder();
                                                                tkBuilder.where().notIn("status", EnumData.StatusEnum.DEL.getValue());
                                                                tkBuilder.orderBy("time", true);
                                                                List<TaskData> tkList = null;
                                                                tkList = taskDao.query(tkBuilder.prepare());
                                                                if (XUtil.listNotNull(tkList)) {
                                                                    for (int i = 0; i < tkList.size(); i++) {
                                                                        StringBuffer stringBuffer = new StringBuffer();
                                                                        TaskData taskData = tkList.get(i);
                                                                        stringBuffer.append(taskData.getTitle());
                                                                        stringBuffer.append("\n");
                                                                        stringBuffer.append(XUtil.getTimeYMDHM(taskData.getTime()));

                                                                        QueryBuilder<CheckListData, Integer> clBuilder = clDao.queryBuilder();
                                                                        clBuilder.where().like("tk_id", taskData.getId()).and().notIn("status", EnumData.StatusEnum.DEL.getValue());
                                                                        clBuilder.orderBy("time", false);
                                                                        List<CheckListData> clList = clDao.query(clBuilder.prepare());
                                                                        if (XUtil.listNotNull(clList)) {
                                                                            for (int j = 0; j < clList.size(); j++) {
                                                                                CheckListData checkListData = clList.get(j);
                                                                                stringBuffer.append("\n");
                                                                                stringBuffer.append(checkListData.getTitle());
                                                                                stringBuffer.append("\t");
                                                                                stringBuffer.append(XUtil.getTimeYMDHM(checkListData.getTime()));
                                                                            }
                                                                        }

                                                                        //
                                                                        QueryBuilder<ProgressData, Integer> pgBuilder = progressDao.queryBuilder();
                                                                        pgBuilder.where().like("tk_id", taskData.getId()).and().notIn("status", EnumData.ProgressStatusEnum.DEL.getValue());
                                                                        pgBuilder.orderBy("time", false);
                                                                        List<ProgressData> pgList = progressDao.query(pgBuilder.prepare());
                                                                        if (XUtil.listNotNull(pgList)) {
                                                                            for (int k = 0; k < pgList.size(); k++) {
                                                                                ProgressData progressData = pgList.get(k);
                                                                                stringBuffer.append("\n");
                                                                                stringBuffer.append(progressData.getContent());
                                                                                stringBuffer.append("\t");
                                                                                stringBuffer.append(XUtil.getTimeYMDHM(progressData.getTime()));
                                                                            }
                                                                        }

                                                                        //
                                                                        QueryBuilder<RemindData, Integer> rdBuilder = rdDao.queryBuilder();
                                                                        rdBuilder.where().like("tk_id", taskData.getId()).and().notIn("status", EnumData.StatusEnum.DEL.getValue());
                                                                        rdBuilder.orderBy("time", false);
                                                                        List<RemindData> rdList = rdDao.query(rdBuilder.prepare());
                                                                        if (XUtil.listNotNull(rdList)) {
                                                                            for (int m = 0; m < rdList.size(); m++) {
                                                                                RemindData remindData = rdList.get(m);
                                                                                stringBuffer.append("\n");
                                                                                stringBuffer.append(remindData.getContent());
                                                                                stringBuffer.append("\t");
                                                                                stringBuffer.append(XUtil.getTimeYMDHM(remindData.getTime()));
                                                                            }
                                                                        }
                                                                        String path = MyPath.getPathFile() + "/" + taskData.getTitle() + "_" + XUtil.getTimeYMDHM_(taskData.getTime()) + ".txt";
                                                                        writeTxtFile(path, stringBuffer.toString());
                                                                        XUtil.debug("output:" + stringBuffer.toString());
                                                                    }
                                                                }

                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }


                                                }

                                            }
                                        }

                );

            }
        };
//        XUtil.listViewRandomAnimation(listView, mAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @SuppressWarnings({"rawtypes", "unchecked"})
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int curPosition = position - listView.getHeaderViewsCount();
//                if (curPosition >= 0 && curPosition < datas.size()) {
//                    SettingData data = datas.get(curPosition);
//                    if (data == null) {
//                        return;
//                    }
//                    int ITEM = data.getId();
//                    if (ITEM == EnumData.SettingEnum.LIST_MODEL.getValue()) {
//                        if (MySp.getSimpleModel()) {
//                            MySp.setSimpleModel(false);
//                        } else {
//                            MySp.setSimpleModel(true);
//                        }
//                        System.exit(0);
//                    } else if (ITEM == EnumData.SettingEnum.THEME.getValue()) {
//                        theme2();
//                    } else if (ITEM == EnumData.SettingEnum.FUNCTION_INTRODUCTION.getValue()) {
//                        XUtil.function(ctx);
//                    } else if (ITEM == EnumData.SettingEnum.DONATE.getValue()) {
//                        XUtil.donateDlg(ctx);
//                    } else if (ITEM == EnumData.SettingEnum.FEED_BACK.getValue()) {
//                        XUtil.feedback(ctx);
//                    } else if (ITEM == EnumData.SettingEnum.CHANGE_LOG.getValue()) {
//
//                    }
//
////                    else if (ITEM == EnumData.SettingEnum.ERROR_LOG.getValue()) {
////                        XUtil.copyText(ctx, readLog(), "错误日志已复制到剪切板");
////                    }
//
//                    else if (ITEM == EnumData.SettingEnum.OPEN_SOURCE.getValue()) {
//                        openSource();
//                    } else if (ITEM == EnumData.SettingEnum.LIST_ANIMATION.getValue()) {
//                        if (MySp.getListAnimation()) {
//                            MySp.setListAnimation(false);
//                        } else {
//                            MySp.setListAnimation(true);
//                        }
//                        System.exit(0);
//                    } else if (ITEM == EnumData.SettingEnum.VIEWPAGER_ANIMATION.getValue()) {
//                        initViewpagerAnimation();
//
//                    } else if (ITEM == EnumData.SettingEnum.BACK_UP.getValue()) {
//                        backUpDbDo();
//                    } else if (ITEM == EnumData.SettingEnum.RECOVER.getValue()) {
//                        recoverDbDo();
//                    } else if (ITEM == EnumData.SettingEnum.ALBUM_NUMCOLUMNS.getValue()) {
//                        initAlbumNum();
//                    } else if (ITEM == EnumData.SettingEnum.SETTING_PWD.getValue()) {
//                        boolean flag = XUtil.notEmptyOrNull(MySp.getPwd());
//                        Intent intent = new Intent(ctx, PwdActivity.class);
//                        intent.putExtra(Constant.KEY_PARAM_TYPE, flag ? EnumData.PwdEnum.SETTING_CHECK.getValue() : EnumData.PwdEnum.SET.getValue());
//                        startActivity(intent);
//                    } else if (ITEM == EnumData.SettingEnum.SHOW_LUNAR.getValue()) {
//                        if (MySp.getShowLunar()) {
//                            MySp.setShowLunar(false);
//                        } else {
//                            MySp.setShowLunar(true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.CLIPBOARD_LISTEN.getValue()) {
//                        if (MySp.getClipboardListen()) {
//                            MySp.setClipboardListen(false);
//                        } else {
//                            MySp.setClipboardListen(true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.SYS_TIP.getValue()) {
//                        if (MySp.getSysTip()) {
//                            MySp.setSysTip(false);
//                        } else {
//                            MySp.setSysTip(true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.SHOW_FINISH.getValue()) {
//                        if (MySp.getShowFinish()) {
//                            MySp.setShowFinish(false);
//                        } else {
//                            MySp.setShowFinish(true);
//                        }
//                        System.exit(0);
//                    } else if (ITEM == EnumData.SettingEnum.DEFAULT_PJ.getValue()) {
//                        int pj_id = MySp.getDefaultPj();
//                        ProjectData pj = projectDao.queryForId(pj_id);
//                        opPjDlg(pj);
//                    } else if (ITEM == EnumData.SettingEnum.DEFAULT_TK.getValue()) {
//                        TaskData tk = taskDao.queryForId(MySp.getDefaultTk());
//                        opTkDlg(tk, ITEM);
//                    } else if (ITEM == EnumData.SettingEnum.CLIPBOARD_TK.getValue()) {
////                        TaskData tk = taskDao.queryForId(MySp.getClipboardTk());
////                        if (tk != null) {
////                            opTkDlg(tk, ITEM);
////                        } else {
////                            settingTk(EnumData.DefaultSettingEnum.CLIPBOARD_TK.getValue());
////                        }
//                    } else if (ITEM == EnumData.SettingEnum.IS_CLIPBOARDTIP.getValue()) {
//                        if (MySp.get(SpConstant.isClipboardTip, Boolean.class, false)) {
//                            MySp.put(SpConstant.isClipboardTip, false);
//                        } else {
//                            MySp.put(SpConstant.isClipboardTip, true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.IS_CELLOPTIONSHOW.getValue()) {
//                        if (MySp.get(SpConstant.isCellOptionShow, Boolean.class, false)) {
//                            MySp.put(SpConstant.isCellOptionShow, false);
//                        } else {
//                            MySp.put(SpConstant.isCellOptionShow, true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.IS_ENTERSAVE.getValue()) {
//                        if (MySp.get(SpConstant.isEnterSave, Boolean.class, false)) {
//                            MySp.put(SpConstant.isEnterSave, false);
//                        } else {
//                            MySp.put(SpConstant.isEnterSave, true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.IS_CHECKLISTENTER.getValue()) {
//                        if (MySp.get(SpConstant.isCheckListEnter, Boolean.class, false)) {
//                            MySp.put(SpConstant.isCheckListEnter, false);
//                        } else {
//                            MySp.put(SpConstant.isCheckListEnter, true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.IS_DELETECONFIRM.getValue()) {
//                        if (MySp.get(SpConstant.isDeleteConfirm, Boolean.class, true)) {
//                            MySp.put(SpConstant.isDeleteConfirm, false);
//                        } else {
//                            MySp.put(SpConstant.isDeleteConfirm, true);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.IS_ADDTASKBYNOTIFY.getValue()) {
//                        if (MySp.get(SpConstant.isAddTaskByNotify, Boolean.class, true)) {
//                            MySp.put(SpConstant.isAddTaskByNotify, false);
//                            NotifyHelper.clearNotificationById(ctx, NotifyHelper.n_add_tk);
//                        } else {
//                            MySp.put(SpConstant.isAddTaskByNotify, true);
//                            NotifyHelper.showAddTk(ctx);
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.NEW_TASK.getValue()) {
//                        XUtil.sendToDesktop(ctx, TkAddActivity.class, "添加日记本",
//                                R.drawable.ic_add_tk);
//                    } else if (ITEM == EnumData.SettingEnum.IS_LISTEN_NOTIFY.getValue()) {
//                        if (MySp.get(SpConstant.isListenNotify, Boolean.class, true)) {
//                            MySp.put(SpConstant.isListenNotify, false);
//                        } else {
//                            MySp.put(SpConstant.isListenNotify, true);
//                            XUtil.tShort("需要打开系统通知读取权限~");
//                        }
//                        getData();
//                    } else if (ITEM == EnumData.SettingEnum.IMPORT_FILE.getValue()) {
////                        showFileChooser();
//                        File tmpDir = new File("/storage/sdcard0/MxGTD/imp");
//                        if (tmpDir.exists() && tmpDir.length() > 0) {
//                            files = tmpDir.listFiles();
//                        } else {
//                            XUtil.tShort("还没有备份,请先备份~");
//                            return;
//                        }
//                        for (int i = 0; i < files.length; i++) {
//                            XUtil.debug("files:" + files[i].getName());
//                            importData(files[i].getPath());
//                        }
//
//
//                    } else if (ITEM == EnumData.SettingEnum.OUTPUT_TXT.getValue()) {
//                        try {
//                            QueryBuilder<TaskData, Integer> tkBuilder = taskDao.queryBuilder();
//                            tkBuilder.where().notIn("status", EnumData.StatusEnum.DEL.getValue());
//                            tkBuilder.orderBy("time", true);
//                            List<TaskData> tkList = null;
//                            tkList = taskDao.query(tkBuilder.prepare());
//                            if (XUtil.listNotNull(tkList)) {
//                                for (int i = 0; i < tkList.size(); i++) {
//                                    StringBuffer stringBuffer = new StringBuffer();
//                                    TaskData taskData = tkList.get(i);
//                                    stringBuffer.append(taskData.getTitle());
//                                    stringBuffer.append("\n");
//                                    stringBuffer.append(XUtil.getTimeYMDHM(taskData.getTime()));
//
//                                    QueryBuilder<CheckListData, Integer> clBuilder = clDao.queryBuilder();
//                                    clBuilder.where().like("tk_id", taskData.getId()).and().notIn("status", EnumData.StatusEnum.DEL.getValue());
//                                    clBuilder.orderBy("time", false);
//                                    List<CheckListData> clList = clDao.query(clBuilder.prepare());
//                                    if (XUtil.listNotNull(clList)) {
//                                        for (int j = 0; j < clList.size(); j++) {
//                                            CheckListData checkListData = clList.get(j);
//                                            stringBuffer.append("\n");
//                                            stringBuffer.append(checkListData.getTitle());
//                                            stringBuffer.append("\t");
//                                            stringBuffer.append(XUtil.getTimeYMDHM(checkListData.getTime()));
//                                        }
//                                    }
//
//                                    //
//                                    QueryBuilder<ProgressData, Integer> pgBuilder = progressDao.queryBuilder();
//                                    pgBuilder.where().like("tk_id", taskData.getId()).and().notIn("status", EnumData.ProgressStatusEnum.DEL.getValue());
//                                    pgBuilder.orderBy("time", false);
//                                    List<ProgressData> pgList = progressDao.query(pgBuilder.prepare());
//                                    if (XUtil.listNotNull(pgList)) {
//                                        for (int k = 0; k < pgList.size(); k++) {
//                                            ProgressData progressData = pgList.get(k);
//                                            stringBuffer.append("\n");
//                                            stringBuffer.append(progressData.getContent());
//                                            stringBuffer.append("\t");
//                                            stringBuffer.append(XUtil.getTimeYMDHM(progressData.getTime()));
//                                        }
//                                    }
//
//                                    //
//                                    QueryBuilder<RemindData, Integer> rdBuilder = rdDao.queryBuilder();
//                                    rdBuilder.where().like("tk_id", taskData.getId()).and().notIn("status", EnumData.StatusEnum.DEL.getValue());
//                                    rdBuilder.orderBy("time", false);
//                                    List<RemindData> rdList = rdDao.query(rdBuilder.prepare());
//                                    if (XUtil.listNotNull(rdList)) {
//                                        for (int m = 0; m < rdList.size(); m++) {
//                                            RemindData remindData = rdList.get(m);
//                                            stringBuffer.append("\n");
//                                            stringBuffer.append(remindData.getContent());
//                                            stringBuffer.append("\t");
//                                            stringBuffer.append(XUtil.getTimeYMDHM(remindData.getTime()));
//                                        }
//                                    }
//                                    String path = MyPath.getPathFile() + "/" + taskData.getTitle() + "_" + XUtil.getTimeYMDHM_(taskData.getTime()) + ".txt";
//                                    writeTxtFile(path, stringBuffer.toString());
//                                    XUtil.debug("output:" + stringBuffer.toString());
//                                }
//                            }
//
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    }
//
//
////                    else if (ITEM == EnumData.SettingEnum.FILE_MOVE.getValue()) {
////                        boolean flag = false;
////                        try {
////                            flag = XUtil.copyFilesTo(new File(MyPath.getOldAlbum()), new File(MyPath.getPathAlbum()));
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                        if (flag) {
////                            XUtil.tShort("搬家成功~");
////                        } else {
////                            XUtil.tShort("搬家出错了~");
////                        }
////                    }
//
//
//                }
//            }
//        });
        listView.setAdapter(mAdapter);
        getData();
        return view;
    }


    public static void writeTxtFile(String path, String newStr) {
        FileOutputStream fos = null;
        try {
            byte[] bom = {(byte) 0xef, (byte) 0xbb, (byte) 0xbf};
//            FileOutputStream fos = new FileOutputStream(new File(mainPath));
            fos = new FileOutputStream(new File(path));
//            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"));
            fos.write(bom);
            fos.write(newStr.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "选择文件导入/支持txt，xml，csv，db"), 103);
        } catch (android.content.ActivityNotFoundException ex) {
            XUtil.tShort("没有找到文件管理器");
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 103:
                if (resultCode == Activity.RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
                    String path = XUtil.getPathFromUri(ctx, uri);
                    importData(path);
                }
                break;
        }
    }

    private void importData(final String path) {
        XUtil.debug("path=>" + path);
        boolean canImport = false;
        File file = new File(path);
        String fileName = "";
        if (file.exists()) {
            fileName = file.getName();
            if (fileName.endsWith(".txt") || fileName.endsWith(".xml") ||
                    fileName.endsWith(".csv") || fileName.endsWith(".db")) {
                canImport = true;
            }
        }
        if (canImport) {
            if (fileName.endsWith(".db")) {
                new MaterialDialog.Builder(ctx)
                        .title("注意!!!")
                        .content("恢复到之前版本,当前版本数据将会丢失,请先备份!")
                        .theme(Theme.LIGHT)
                        .positiveText("仍然恢复")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                recoverMath(path);
                            }
                        })
                        .show();
            } else {
                DbUtils.insertProgressForCsv(path);
                XUtil.tShort(fileName + " 导入成功~");
            }

//            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.TASK.getValue()));
        } else {
            XUtil.tShort("文件格式非法~");
        }
    }


    private void openSource() {
        String libs = " listviewanimations \n material-dialogs \n supertoasts \n materialtabstrip \n floatingactionbutton \n fastjson \n ormlite \n ckchangelog \n datetimepicker \n eventbus \n materialicons \n universal-image-loader";
        new MaterialDialog.Builder(ctx)
                .title("向以下开源分组致谢:")
                .content(libs)
                .theme(Theme.LIGHT)
                .positiveText("OK!!")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {

                    }
                })
                .show();
    }


//    private void themeChange() {
//        ColorSelector colorSelector = new ColorSelector(ctx,
//                MySp.getTheme(), new ColorSelector.OnColorSelectedListener() {
//            @Override
//            public void onColorSelected(int color) {
//                MySp.setTheme(color);
//                System.exit(0);
//            }
//        });
//        colorSelector.show();
//    }


    private void getData() {
        datas = new ArrayList<SettingData>();
        datas.add(new SettingData(EnumData.SettingEnum.CLIPBOARD_LISTEN.getValue(), "监听剪切板", "", MySp.getClipboardListen() ? "是" : "否"));
        datas.add(new SettingData(EnumData.SettingEnum.BACK_UP.getValue(), "数据备份", ""));
        datas.add(new SettingData(EnumData.SettingEnum.RECOVER.getValue(), "数据恢复", ""));
        mAdapter.setItems(datas, listView);
    }


    void initAlbumNum() {
        new MaterialDialog.Builder(ctx)
                .title("相册图片列数-列数越多可能会影响加载速度")
                .items(new String[]{"1", "2", "3", "4"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int tmp = (which + 1);
                        MySp.setAlbumNumColumns(tmp);
                        getData();
                    }
                })
                .show();

    }

    void initViewpagerAnimation() {
        new MaterialDialog.Builder(ctx)
                .title("页面动画")
                .items(new String[]{EnumData.ViewPagerAnimationEnum.NONE.getStrName(), EnumData.ViewPagerAnimationEnum.DefaultTransformer.getStrName(), EnumData.ViewPagerAnimationEnum.BackgroundToForegroundTransformer.getStrName(),
                        EnumData.ViewPagerAnimationEnum.TabletTransformer.getStrName(), EnumData.ViewPagerAnimationEnum.ZoomOutSlideTransformer.getStrName(),
                        EnumData.ViewPagerAnimationEnum.DepthPageTransformer.getStrName(), EnumData.ViewPagerAnimationEnum.StackTransformer.getStrName(), EnumData.ViewPagerAnimationEnum.RotateUpTransformer.getStrName(), EnumData.ViewPagerAnimationEnum.ForegroundToBackgroundTransformer.getStrName(), EnumData.ViewPagerAnimationEnum.RotateDownTransformer.getStrName()})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int level = (which - 1);
                        MySp.setViewpagerAnimation(level);
                        System.exit(0);
                    }
                })
                .show();

    }

//    private static String readLog() {
//        String res = "no crash_log !";
//        try {
//            FileInputStream fin = new FileInputStream(CrashHandler.CRASH_LOG);
//            int length = fin.available();
//            byte[] buffer = new byte[length];
//            fin.read(buffer);
//            res = EncodingUtils.getString(buffer, "UTF-8");
//            fin.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return res;
//    }


    private void backUpDbDo() {
        try {
            String newPath = MyPath.getPathDb() + File.separator + XUtil.getDateY_M_D() + File.separator + Constant.DB_NAME;
            boolean flag = XUtil.copyFileTo(new File(Constant.DB_PATH), new File(newPath));
            if (flag) {
                XUtil.tLong("已备份~ ");
            } else {
                XUtil.tShort("数据备份失败~");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 对文件进行排序
    private void sortFilesByDirectory(File[] files) {
        try {
            Arrays.sort(files, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    if (f1.isDirectory() && f2.isFile())
                        return -1;
                    if (f1.isFile() && f2.isDirectory())
                        return 1;
                    return f1.getName().compareToIgnoreCase(f2.getName());
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void recoverDbDo() {

        File tmpDir = new File(MyPath.getPathDb());
        if (tmpDir.exists() && tmpDir.length() > 0) {
            files = tmpDir.listFiles();
        } else {
            XUtil.tShort("还没有备份,请先备份~");
            return;
        }

        sortFilesByDirectory(files);
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ListView listView = new ListView(ctx);
        List<Map<String, String>> nameList = new ArrayList<Map<String, String>>();
        for (int i = 0; i < files.length; i++) {
            Map<String, String> nameMap = new HashMap<String, String>();
            nameMap.put("name", files[i].getName());
            nameList.add(nameMap);
        }
        SimpleAdapter adapter = new SimpleAdapter(ctx,
                nameList, R.layout.cell_pj,
                new String[]{"name"},
                new int[]{R.id.tvTitle});
        listView.setAdapter(adapter);
        view.addView(listView);


        String item[] = null;
        item = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            item[i] = files[i].getName();
        }


        MaterialDialog md = new MaterialDialog.Builder(ctx)
//                .customView(view)
                .title("数据恢复")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, final int pos, CharSequence charSequence) {
                        new MaterialDialog.Builder(ctx)
                                .title("注意!!!")
                                .content("恢复到之前版本,当前版本数据将会丢失,请先备份!")
                                .theme(Theme.LIGHT)
                                .positiveText("仍然恢复")
                                .negativeText("取消")
                                .callback(new MaterialDialog.ButtonCallback() {
                                    @Override
                                    public void onPositive(MaterialDialog materialDialog) {
                                        String path = files[pos].getAbsolutePath();
                                        recoverMath(path);
                                    }
                                })
                                .show();
                        materialDialog.dismiss();
                    }
                })
                .positiveText("保留最新备份")
                .negativeText("选择文件")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        for (int j = 0; j < files.length - 1; j++) {
                            String path = files[j].getPath();
                            XUtil.deleteFile(new File(path));
                        }
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        showFileChooser();
                        materialDialog.dismiss();
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }


    private void recoverMath(final String path) {
        int x = new Random().nextInt(50);
        int y = new Random().nextInt(50);
        final int result = x + y;
        final EditText editText = new EditText(ctx);
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setHint("输入结果...");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);
        new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .title("恢复确认(" + x + "+" + y + " =?)")
                .theme(Theme.LIGHT)
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        String res = editText.getText().toString();
                        if (XUtil.notEmptyOrNull(res) && res.equals(String.valueOf(result))) {

                            try {
                                String filePath = path;
                                if (!path.contains(Constant.DB_NAME)) {
                                    filePath = path + File.separator + Constant.DB_NAME;
                                }
                                boolean flag = XUtil.copyFileTo(new File(filePath), new File(Constant.DB_PATH));
                                if (flag) {
                                    XUtil.tShort("数据恢复成功~");
                                } else {
                                    XUtil.tShort("数据恢复失败~");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            XUtil.tShort("回答错误~");
                        }
                    }
                })
                .show();


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {
    }


}
