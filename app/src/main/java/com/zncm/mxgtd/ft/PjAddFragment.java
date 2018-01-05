package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.XUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.Calendar;


public class PjAddFragment extends BaseDbFragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    private Activity ctx;
    protected View view;
    private EditText etTitle, etDescribe;
    private RelativeLayout rlStartTime, rlStopTime, rlCreateTime;
    private TextView tvStartTime, tvStopTime, tvCreateTime;
    private RuntimeExceptionDao<ProjectData, Integer> dao;
    private ProjectData projectData;
    private boolean bUpadte = false;
    private boolean bSetStartTime = false;
    private Calendar calendar = Calendar.getInstance();
    private Long start_time;
    private Long stop_time;

    public PjAddFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ctx = getActivity();
        view = inflater.inflate(R.layout.activity_pj_add, null);
        initView();
        if (dao == null) {
            dao = getHelper().getPjDao();
        }
        return view;
    }

    private void initView() {
        etTitle = (EditText) view.findViewById(R.id.etTitle);
        etDescribe = (EditText) view.findViewById(R.id.etDescribe);
        rlStartTime = (RelativeLayout) view.findViewById(R.id.rlStartTime);
        rlStopTime = (RelativeLayout) view.findViewById(R.id.rlStopTime);
        rlStopTime = (RelativeLayout) view.findViewById(R.id.rlStopTime);
        rlCreateTime = (RelativeLayout) view.findViewById(R.id.rlCreateTime);
        tvStartTime = (TextView) view.findViewById(R.id.tvContent);
        tvStopTime = (TextView) view.findViewById(R.id.tvTag);
        tvCreateTime = (TextView) view.findViewById(R.id.tvCreateTime);

        rlStartTime.setOnClickListener(this);
        rlStopTime.setOnClickListener(this);


        Serializable dataParam = ctx.getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
        projectData = (ProjectData) dataParam;
        if (projectData == null) {
            int tk_id = ctx.getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            projectData = DbUtils.getPjById(tk_id);
        }
        if (projectData != null) {
            bUpadte = true;
            etTitle.setFocusable(false);
            etDescribe.setFocusable(false);
            etTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEtDlg(projectData, true);
                }
            });
            etDescribe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initEtDlg(projectData, false);
                }
            });
            if (XUtil.notEmptyOrNull(projectData.getTitle())) {
                etTitle.setVisibility(View.VISIBLE);
                etTitle.setText(projectData.getTitle());
            } else {
                etTitle.setVisibility(View.INVISIBLE);
            }
            if (XUtil.notEmptyOrNull(projectData.getDescribe())) {
                etDescribe.setVisibility(View.VISIBLE);
                etDescribe.setText(projectData.getDescribe());
            } else {
                etDescribe.setVisibility(View.VISIBLE);
            }

            if (projectData.getStart_time() != null) {
                tvStartTime.setVisibility(View.VISIBLE);
                tvStartTime.setText(XUtil.getTimeYMDE(projectData.getStart_time()));
            } else {
                tvStartTime.setVisibility(View.INVISIBLE);
            }


            if (projectData.getStop_time() != null) {
                tvStopTime.setVisibility(View.VISIBLE);
                tvStopTime.setText(XUtil.getTimeYMDE(projectData.getStop_time()));
            } else {
                tvStopTime.setVisibility(View.INVISIBLE);
            }


            if (projectData.getTime() != null) {
                tvCreateTime.setVisibility(View.VISIBLE);
                tvCreateTime.setText(XUtil.getTimeYMDHM(projectData.getTime()));
            } else {
                tvCreateTime.setVisibility(View.INVISIBLE);
            }
            rlCreateTime.setVisibility(View.VISIBLE);
        } else {
            rlCreateTime.setVisibility(View.GONE);
        }

        XUtil.autoKeyBoardShow(etTitle);
    }


    void initEtDlg(final ProjectData data, final boolean bTitle) {
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setLines(5);
        editText.setHint("输入...");
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        if (bTitle) {
            editText.setText(data.getTitle());
            editText.setSelection(data.getTitle().length());
        } else {
            editText.setText(data.getDescribe());
            editText.setSelection(data.getDescribe().length());
        }
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);


        MaterialDialog md =    XUtil.themeMaterialDialog(ctx)
                .customView(view, true)
                .positiveText("修改")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        try {

                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            if (bTitle) {
                                data.setTitle(content);
                                etTitle.setText(content);
                                //连带修改笔记本组下笔记本的笔记本组
                                DbUtils.updateTaskByPjTitle(content, projectData.getId());
                            } else {
                                data.setDescribe(content);
                                etDescribe.setText(content);
                            }
                            data.setModify_time(XUtil.getLongTime());
                            dao.update(data);
                            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PROJECT.getValue()));
                        } catch (Exception e) {
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {

                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        XUtil.dismissShowDialog(materialDialog, true);
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!bUpadte) {
            menu.add("save").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_save)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item == null || item.getTitle() == null) {
            return false;
        }


        if (item.getTitle().equals("save")) {
            String title = etTitle.getText().toString().toString();
            if (!XUtil.notEmptyOrNull(title)) {
                XUtil.tShort("填入标题!");
                return false;
            }
            String describe = etDescribe.getText().toString().toString();
            ProjectData pj = new ProjectData(title, describe, start_time, stop_time);
            dao.create(pj);
            ctx.finish();
            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PROJECT.getValue()));

        }
        return false;
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
        }
    }

    private void initDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(1930, 2030);
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "datepicker");
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        if (bSetStartTime) {
            start_time = XUtil.getTimeLong(year, month, day);
            if (bUpadte) {
                projectData.setStart_time(start_time);
                projectData.setModify_time(XUtil.getLongTime());
                dao.update(projectData);
            }
            tvStartTime.setVisibility(View.VISIBLE);
            tvStartTime.setText(year + "-" + (month + 1) + "-" + day);
        } else {
            stop_time = XUtil.getTimeLong(year, month, day);
            if (bUpadte) {
                projectData.setStop_time(stop_time);
                projectData.setModify_time(XUtil.getLongTime());
                dao.update(projectData);
            }
            tvStopTime.setVisibility(View.VISIBLE);
            tvStopTime.setText(year + "-" + (month + 1) + "-" + day);
        }
    }


}
