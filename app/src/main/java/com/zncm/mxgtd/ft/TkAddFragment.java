package com.zncm.mxgtd.ft;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.TkAddActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TkAddFragment extends BaseDbFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private TkAddActivity ctx;
    protected View view;
    private EditText etTitle, etDescribe;
    //rlTag,
    private RelativeLayout rlStartTime, rlStopTime, rlLevel, rlProject, rlCreateTime;
    private LinearLayout llDays;
    //tvTag,
    private TextView tvStartTime, tvStopTime, tvLevel, tvProject, tvCreateTime;
    private TextView tvDay0, tvDay1, tvDay2, tvDay3, tvDay4;
    private RuntimeExceptionDao<TaskData, Integer> dao;
    private RuntimeExceptionDao<RemindData, Integer> rdDao;
    private int level = Constant.sort_no;
    private int pj_id = 0;
    private List<ProjectData> pjList;
    private Long start_time;
    private Long stop_time;
    private String tag;
    private TaskData taskData;
    private boolean bUpadte = false;
    private boolean bProjectFinish = false;
    private Calendar calendar = Calendar.getInstance();
    private boolean bSetStartTime = false;

    boolean bNotify = false;

    public TkAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = (TkAddActivity) getActivity();
        view = inflater.inflate(R.layout.activity_tk_add, null);
        initView();
        initData();
        return view;
    }

    private void initData() {
        if (dao == null) {
            dao = getHelper().getTkDao();
        }
        if (rdDao == null) {
            rdDao = getHelper().getRdDao();
        }

        //属于某个笔记本组
        pj_id = ctx.getIntent().getIntExtra(Constant.KEY_PARAM_ID, 0);
        bNotify = ctx.getIntent().getBooleanExtra(Constant.KEY_PARAM_BOOLEAN, false);


        ProjectData pj = DbUtils.getPjById(pj_id);
        if (pj != null && XUtil.notEmptyOrNull(pj.getTitle())) {
            tag = pj.getTitle();
        }


        start_time = ctx.getIntent().getLongExtra(Constant.KEY_PARAM_LONG, 0);


        if (start_time != null && start_time != 0) {
            start_time += Constant.HOUR * 10;
            tvStartTime.setText(XUtil.getTimeYMDE(start_time));
        }


//        tvProject.setText("#" + pj_id);
        Serializable dataParam = ctx.getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
        taskData = (TaskData) dataParam;
        if (taskData == null) {
            int tk_id = ctx.getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            taskData = DbUtils.getTkById(tk_id);
        }
        if (taskData != null) {

            ctx.toolbar.setTitle("修改");


            bUpadte = true;
            etTitle.setFocusable(false);
            etDescribe.setFocusable(false);
            etTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEtDlg(taskData, 1);
                }
            });
            etDescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEtDlg(taskData, 2);
                }
            });

            if (XUtil.notEmptyOrNull(taskData.getTitle())) {
                etTitle.setVisibility(View.VISIBLE);
                etTitle.setText(taskData.getTitle());
            } else {
                etTitle.setVisibility(View.INVISIBLE);
            }

            if (XUtil.notEmptyOrNull(taskData.getDescribe())) {
                etDescribe.setVisibility(View.VISIBLE);
                etDescribe.setText(taskData.getDescribe());
            } else {
                etDescribe.setVisibility(View.VISIBLE);
            }
//            if (XUtil.notEmptyOrNull(taskData.getTag())) {
//                tvTag.setVisibility(View.VISIBLE);
//                tvTag.setText(taskData.getTag());
//            } else {
//                tvTag.setVisibility(View.VISIBLE);
//            }
            if (taskData.getStart_time() != null) {
                tvStartTime.setVisibility(View.VISIBLE);
                tvStartTime.setText(XUtil.getTimeYMDE(taskData.getStart_time()));
            } else {
                tvStartTime.setVisibility(View.INVISIBLE);
            }


            if (taskData.getTime() != null) {
                tvCreateTime.setVisibility(View.VISIBLE);
                tvCreateTime.setText(XUtil.getTimeYMDHM(taskData.getTime()));
            } else {
                tvCreateTime.setVisibility(View.INVISIBLE);
            }


            if (taskData.getStop_time() != null) {
                tvStopTime.setVisibility(View.VISIBLE);
                tvStopTime.setText(XUtil.getTimeYMDE(taskData.getStop_time()));
            } else {
                tvStopTime.setVisibility(View.INVISIBLE);
            }
//            if (taskData.getLevel() > 0) {
//                tvLevel.setVisibility(View.VISIBLE);
//                tvLevel.setText(EnumData.TaskLevelEnum.valueOf(taskData.getLevel()).getStrName());
//            } else {
//                tvLevel.setVisibility(View.INVISIBLE);
//            }
            tag = taskData.getTag();
            rlCreateTime.setVisibility(View.VISIBLE);
        } else {

            ctx.toolbar.setTitle("添加");

            rlCreateTime.setVisibility(View.GONE);
        }
        if (XUtil.notEmptyOrNull(tag)) {
            tvProject.setVisibility(View.VISIBLE);
            tvProject.setText(tag);
        } else {
            tvProject.setVisibility(View.INVISIBLE);
        }
        XUtil.autoKeyBoardShow(etTitle);
    }


    void initEtDlg(final TaskData data, final int etId) {

        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setLines(5);
        editText.setHint("输入...");
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        if (etId == 1) {
            editText.setText(data.getTitle());
            editText.setSelection(data.getTitle().length());
        } else if (etId == 2) {
            editText.setText(data.getDescribe());
            editText.setSelection(data.getDescribe().length());
        } else if (etId == 3) {
            if (data != null && XUtil.notEmptyOrNull(data.getTag())) {
                editText.setText(data.getTag());
                editText.setSelection(data.getTag().length());
            }
        }
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);


        MaterialDialog md =  XUtil.themeMaterialDialog(ctx)
                .customView(view, true)
                .positiveText("修改")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        try {
                            if (dao == null) {
                                dao = getHelper().getTkDao();
                            }
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            if (etId == 1) {
                                data.setTitle(content);
                                etTitle.setText(content);
                            } else if (etId == 2) {
                                data.setDescribe(content);
                                etDescribe.setText(content);
                            }
//                            else if (etId == 3) {
//                                tag = content;
//                                if (bUpadte) {
//                                    data.setTag(content);
//                                }
//                                tvTag.setText(content);
//                            }
                            if (bUpadte) {
                                data.setModify_time(XUtil.getLongTime());
                                dao.update(data);
                            }
                            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.TASK.getValue()));


                        } catch (Exception e) {
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        materialDialog.cancel();
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }


    private void initView() {
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescribe = (EditText) view.findViewById(R.id.etDescribe);
        rlStartTime = (RelativeLayout) view.findViewById(R.id.rlStartTime);
        rlStopTime = (RelativeLayout) view.findViewById(R.id.rlStopTime);
        rlLevel = (RelativeLayout) view.findViewById(R.id.rlLevel);
//        rlTag = (RelativeLayout) view.findViewById(R.id.rlTag);
        rlProject = (RelativeLayout) view.findViewById(R.id.rlProject);
        rlCreateTime = (RelativeLayout) view.findViewById(R.id.rlCreateTime);
        llDays = (LinearLayout) view.findViewById(R.id.llDays);

        tvStartTime = (TextView) view.findViewById(R.id.tvContent);
        tvStopTime = (TextView) view.findViewById(R.id.tvTag);
        tvCreateTime = (TextView) view.findViewById(R.id.tvCreateTime);

        tvLevel = (TextView) view.findViewById(R.id.tvLevel);
//        tvTag = (TextView) view.findViewById(R.id.tvTag);
        tvProject = (TextView) view.findViewById(R.id.tvProject);

        tvDay0 = (TextView) view.findViewById(R.id.tvDay0);
        tvDay1 = (TextView) view.findViewById(R.id.tvDay1);
        tvDay2 = (TextView) view.findViewById(R.id.tvDay2);
        tvDay3 = (TextView) view.findViewById(R.id.tvDay3);
        tvDay4 = (TextView) view.findViewById(R.id.tvDay4);

        rlStartTime.setOnClickListener(this);
        rlStopTime.setOnClickListener(this);
        rlLevel.setOnClickListener(this);
//        rlTag.setOnClickListener(this);
        rlProject.setOnClickListener(this);
        rlProject.setOnClickListener(this);


        tvDay0.setOnClickListener(this);
        tvDay1.setOnClickListener(this);
        tvDay2.setOnClickListener(this);
        tvDay3.setOnClickListener(this);
        tvDay4.setOnClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
        if (!bUpadte) {
            sub.add(0, 1, 0, "保存");
            sub.add(0, 2, 0, "继续添加");
        } else {
            sub.add(0, 3, 0, "删除");
            sub.add(0, 4, 0, "设为默认笔记本");
        }
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item == null || item.getTitle() == null) {
            return false;
        }

        switch (item.getItemId()) {
            case 1:
                saveTk();
                ctx.finish();
                if (bNotify) {
                    ctx.moveTaskToBack(true);
                }
                break;
            case 2:
                saveTk();
                etTitle.setText("");
                etDescribe.setText("");
                XUtil.tShort("笔记本添加成功~~");
                break;
            case 3:
                 XUtil.themeMaterialDialog(ctx)
                        .title("删除笔记本!")
                        .positiveText("删除")
                        .negativeText("取消")
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog materialDialog) {
                                delTask();
                            }


                        })
                        .show();
                break;
            case 4:
                MySp.setDefaultTk(taskData.getId());
                XUtil.tShort("设为默认笔记本成功~~");
                break;
            case 5:
                initProject();
                break;
        }
        return false;
    }

    private void delTask() {
        if (dao == null) {
            dao = getHelper().getTkDao();
        }
        taskData.setStatus(EnumData.StatusEnum.DEL.getValue());
        taskData.setModify_time(System.currentTimeMillis());
        dao.update(taskData);
        DbUtils.finishCheckList(taskData);
        ctx.finish();
    }

    private void saveTk() {
        if (dao == null) {
            dao = getHelper().getTkDao();
        }
        String title = etTitle.getText().toString().toString();
        if (!XUtil.notEmptyOrNull(title)) {
//                XUtil.tShort("填入标题!");
//                return false;
            if (start_time == null || start_time == 0) {
                title = XUtil.getDateYMDE(System.currentTimeMillis());
            } else {
                title = XUtil.getDateYMDE(start_time);
            }
        }
        if (pj_id == 0) {
            pj_id = MySp.getDefaultPj();
            ProjectData pj = DbUtils.getPjById(pj_id);
            if (pj != null && XUtil.notEmptyOrNull(pj.getTitle())) {
                tag = pj.getTitle();
            }
        }
        String describe = etDescribe.getText().toString().toString();


        ArrayList<Long> rds = new ArrayList<>();
        Long now = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();

        if (title.contains("今天")) {
            cal.set(Calendar.HOUR_OF_DAY, 10);
            setRd(rds, now, cal, 0L);
        }
        if (title.contains("明天")) {
            cal.set(Calendar.HOUR_OF_DAY, 10);
            setRd(rds, now, cal, Constant.DAY);
        }
        if (title.contains("后天")) {
            cal.set(Calendar.HOUR_OF_DAY, 10);
            setRd(rds, now, cal, 2 * Constant.DAY);
        }

        if (title.contains("今天")) {
            cal.set(Calendar.HOUR_OF_DAY, 10);
            setRd(rds, now, cal, 0L);
        }
        if (title.contains("早上")) {
            cal.set(Calendar.HOUR_OF_DAY, 8);
            setRd(rds, now, cal, 0L);
        }
        if (title.contains("上午")) {
            cal.set(Calendar.HOUR_OF_DAY, 10);
            setRd(rds, now, cal, 0L);
        }
        if (title.contains("中午")) {
            cal.set(Calendar.HOUR_OF_DAY, 12);
            setRd(rds, now, cal, 0L);
        }
        if (title.contains("下午")) {
            cal.set(Calendar.HOUR_OF_DAY, 15);
            setRd(rds, now, cal, 0L);
        }
        if (title.contains("傍晚")) {
            cal.set(Calendar.HOUR_OF_DAY, 17);
            setRd(rds, now, cal, 0L);
        }

        if (title.contains("晚上")) {
            cal.set(Calendar.HOUR_OF_DAY, 20);
            setRd(rds, now, cal, 0L);
        }


        TaskData tk = new TaskData(title, describe, start_time, stop_time, level, pj_id, null, tag);
        if (start_time == null || start_time == 0) {
            tk.setStart_time(System.currentTimeMillis());
        }
        dao.create(tk);
//            if (remind_time != null) {
        int _id = 0;
        try {
            ArrayList<TaskData> tkDatas = new ArrayList<TaskData>();
            QueryBuilder<TaskData, Integer> builder = dao.queryBuilder();
            builder.orderBy("id", false).limit(1l);
            tkDatas = (ArrayList<TaskData>) dao.query(builder.prepare());
            TaskData tkData = tkDatas.get(0);
            if (tkData != null) {
                _id = tkData.getId();
                //系统进展
                int tk_id = 0;
                int pj_id = 0;
                if (tkData != null) {
                    tk_id = tkData.getId();
                    pj_id = tkData.getPj_id();
                }
                ProgressData progressData = new ProgressData(tkData.getTitle(), EnumData.ProgressTypeEnum.SYS.getValue(),
                        EnumData.ProgressBusinessEnum.REPLY.getValue(), EnumData.ProgressStatusEnum.NORMAL.getValue(),
                        pj_id, tk_id, EnumData.ProgressActionEnum.ADD.getValue()
                );
                DbUtils.insertProgress(progressData);

                //如果设置结束时间，自动设置当日10点的闹钟
                if (stop_time != null) {
                    RemindData remindData = new RemindData(title, EnumData.StatusEnum.ON.getValue(), _id,
                            stop_time
                    );
                    rdDao.create(remindData);
                }
                //开始提醒
                if (start_time != null && start_time > System.currentTimeMillis()) {
                    RemindData remindData = new RemindData(title, EnumData.StatusEnum.ON.getValue(), _id,
                            start_time
                    );
                    rdDao.create(remindData);
                }


                if (XUtil.listNotNull(rds)) {
                    for (Long tmp : rds) {
                        RemindData remindData = new RemindData(XUtil.getDateMDEHM(tmp) + "_" + title, EnumData.StatusEnum.ON.getValue(), _id, tmp);
                        rdDao.create(remindData);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.TASK.getValue()));
    }

    private void setRd(ArrayList<Long> rds, Long now, Calendar cal, Long time_offset) {
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        if (now < cal.getTime().getTime() + time_offset) {
            rds.add(cal.getTime().getTime() + time_offset);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlStartTime:
                bSetStartTime = true;
                initDatePicker();
                break;
            case R.id.rlStopTime:
                bSetStartTime = false;
                initDatePicker();
                break;
            case R.id.rlLevel:
//                initLevel();
                break;
            case R.id.rlTag:
                initEtDlg(taskData, 3);
                break;
            case R.id.rlProject:
                initProject();
                break;
            case R.id.tvDay0:
                initDays(0);
                break;
            case R.id.tvDay1:
                initDays(1);
                break;
            case R.id.tvDay2:
                initDays(2);
                break;
            case R.id.tvDay3:
                initDays(3);
                break;
            case R.id.tvDay4:
                initDays(7);
                break;

        }

    }

    public void initDays(int day) {
        start_time = XUtil.getDayTenStart() + day * Constant.DAY;
        tvStartTime.setText(XUtil.getTimeYMDE(start_time));
        if (bUpadte) {
            if (dao == null) {
                dao = getHelper().getTkDao();
            }
            taskData.setStart_time(start_time);
            taskData.setModify_time(XUtil.getLongTime());
            dao.update(taskData);
        }
    }


    private void initDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(2000, 2025);
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "datepicker");
    }

    private void initLevel() {
         XUtil.themeMaterialDialog(ctx)
                .title("重要性")
                .items(new String[]{EnumData.TaskLevelEnum.NO.getStrName(), EnumData.TaskLevelEnum.LOW.getStrName(), EnumData.TaskLevelEnum.MIDDLE.getStrName(), EnumData.TaskLevelEnum.HIGH.getStrName()})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        level = (which + 1);
                        tvLevel.setVisibility(View.VISIBLE);
                        tvLevel.setText(EnumData.TaskLevelEnum.valueOf(which + 1).getStrName());
                        if (bUpadte) {
                            if (dao == null) {
                                dao = getHelper().getTkDao();
                            }
                            taskData.setLevel(level);
                            taskData.setModify_time(XUtil.getLongTime());
                            dao.update(taskData);
                        }
                    }
                })
                .show();
    }


    void initProject() {
        RuntimeExceptionDao<ProjectData, Integer> pjDao = null;
        try {
            if (pjDao == null) {
                pjDao = getHelper().getPjDao();
            }
            QueryBuilder<ProjectData, Integer> builder = pjDao.queryBuilder();
            if (bProjectFinish) {
                builder.where().eq("status", EnumData.StatusEnum.OFF.getValue());
            } else {
                builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
            }
            builder.orderBy("id", false).limit(Constant.MAX_DB_QUERY);
            pjList = pjDao.query(builder.prepare());
        } catch (Exception e) {
        }
        if (!XUtil.listNotNull(pjList)) {
            XUtil.tShort("请先创建笔记本组~");
            return;
        }
        String item[] = new String[pjList.size()];
        for (int i = 0; i < pjList.size(); i++) {
            item[i] = "#" + pjList.get(i).getId() + " " + pjList.get(i).getTitle();
        }


        //bProjectFinish
        final MaterialDialog md =  XUtil.themeMaterialDialog(ctx)
                .positiveText("取消")
                .neutralText(bProjectFinish ? "进行中的笔记本组" : "已完的笔记本组")
                .items(item)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog materialDialog, View view, int pos, CharSequence charSequence) {
                        ProjectData pj = pjList.get(pos);
                        if (pj != null && XUtil.notEmptyOrNull(pj.getTitle())) {
                            tvProject.setVisibility(View.VISIBLE);
                            tag = pj.getTitle();
                            tvProject.setText(tag);
                            pj_id = pj.getId();
                            if (bUpadte) {
                                if (dao == null) {
                                    dao = getHelper().getTkDao();
                                }
                                taskData.setPj_id(pj_id);
                                taskData.setModify_time(XUtil.getLongTime());
                                if (XUtil.notEmptyOrNull(tag)) {
                                    taskData.setTag(tag);
                                }
                                dao.update(taskData);
                            }
                        }
                        materialDialog.cancel();
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        materialDialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                        if (bProjectFinish) {
                            bProjectFinish = false;
                        } else {
                            bProjectFinish = true;
                        }
                        materialDialog.dismiss();
                        initProject();
                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {

                    }
                })
                .autoDismiss(false)
                .build();
        md.show();
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (bSetStartTime) {
            start_time = XUtil.getTimeLongTenHours(year, month, day);
            if (bUpadte) {
                taskData.setStart_time(start_time);
                taskData.setModify_time(XUtil.getLongTime());
                dao.update(taskData);
                //开始提醒
                if (start_time != null && start_time > System.currentTimeMillis()) {
                    RemindData remindData = new RemindData(taskData.getTitle(), EnumData.StatusEnum.ON.getValue(), taskData.getId(),
                            start_time
                    );
                    rdDao.create(remindData);
                }
            }
            tvStartTime.setVisibility(View.VISIBLE);
            tvStartTime.setText(year + "-" + (month + 1) + "-" + day);
        } else {
            stop_time = XUtil.getTimeLongTenHours(year, month, day);
            if (bUpadte) {
                taskData.setStop_time(stop_time);
                taskData.setModify_time(XUtil.getLongTime());
                dao.update(taskData);
                if (stop_time != null) {
                    RemindData remindData = new RemindData(taskData.getTitle() + "到期", EnumData.StatusEnum.ON.getValue(), taskData.getId(),
                            stop_time
                    );
                    rdDao.create(remindData);
                }
            }
            tvStopTime.setVisibility(View.VISIBLE);
            tvStopTime.setText(year + "-" + (month + 1) + "-" + day);
        }
    }


}
