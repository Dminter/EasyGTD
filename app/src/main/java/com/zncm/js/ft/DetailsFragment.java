package com.zncm.js.ft;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.j256.ormlite.stmt.QueryBuilder;
import com.malinskiy.materialicons.Iconify;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.zncm.js.R;
import com.zncm.js.adapter.ProjectAdapter;
import com.zncm.js.data.CheckListData;
import com.zncm.js.data.Constant;
import com.zncm.js.data.DetailsData;
import com.zncm.js.data.EnumData;
import com.zncm.js.data.ProgressData;
import com.zncm.js.data.RemindData;
import com.zncm.js.data.SpConstant;
import com.zncm.js.ui.TkAddActivity;
import com.zncm.js.ui.TkDetailsActivity;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.MySp;
import com.zncm.js.utils.XUtil;
import com.zncm.js.view.loadmore.MxItemClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DetailsFragment extends BaseListFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ProjectAdapter mAdapter;
    private TkDetailsActivity ctx;
    public List<DetailsData> datas = null;
    private boolean onLoading = false;
    private boolean bUpadte = false;
    private boolean bOutDate = false;
    private String notRing = "已过期，将不会提醒！";
    private Calendar calendar = Calendar.getInstance();
    private Calendar remindCalendar = Calendar.getInstance();
    private TextView tvTime;
    private Spinner spTime;
    private Spinner spTimeRepeat;
    private int spChoose = 0;
    private int spRepeatChoose = 0;
    private Long remind_time = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (TkDetailsActivity) getActivity();


        datas = new ArrayList<DetailsData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(final int position, PjViewHolder holder) {
                final DetailsData data = datas.get(position);
                if (XUtil.notEmptyOrNull(data.getContent())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    String title = data.getContent();
                    holder.tvTitle.setText(title);

                    if (title.length() < Constant.MAX_LEN) {
                        holder.tvTitle.setTextSize(Constant.FS_1);
                    } else {
                        holder.tvTitle.setTextSize(Constant.FS_2);
                    }
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                    holder.tvTitle.setTextSize(Constant.FS_2);
                }
//                if (data.getTime() > 0) {
//                    holder.tvContent.setVisibility(View.VISIBLE);
//                    holder.tvContent.setText(XUtil.getDisplayDateTime(data.getTime()));
//                } else {
//                    holder.tvContent.setVisibility(View.GONE);
//                }
                holder.tvContent.setVisibility(View.GONE);


                if (EnumData.DetailBEnum.progress.getValue() == data.getBusiness_type()) {
                    holder.tvTag.setVisibility(View.GONE);
                    holder.llBg.setBackgroundColor(getResources().getColor(R.color.material_grey_100));


                } else if (EnumData.DetailBEnum.check.getValue() == data.getBusiness_type()) {
                    holder.tvTag.setVisibility(View.GONE);
                    if (data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        holder.llBg.setBackgroundColor(getResources().getColor(R.color.material_amber_200));
                    } else if (data.getStatus() == EnumData.StatusEnum.OFF.getValue()) {
                        holder.llBg.setBackgroundColor(getResources().getColor(R.color.material_amber_600));
                    }
                } else if (EnumData.DetailBEnum.remind.getValue() == data.getBusiness_type()) {
                    holder.llBg.setBackgroundColor(getResources().getColor(R.color.material_pink_100));


                    if (data.getRemind_time() != null) {
                        int diffDay = XUtil.diffNowDays(data.getRemind_time());
                        String show = "";
                        if (data.getType() > 0) {
                            EnumData.RemindRepeatTypeEnum repeatTypeEnum = EnumData.RemindRepeatTypeEnum.valueOf(data.getType());
                            show = repeatTypeEnum.getStrName();
                        } else {
                            if (diffDay == 0) {
                                if (data.getRemind_time() > System.currentTimeMillis()) {
                                    show = XUtil.diffTime(data.getRemind_time());
                                } else {
                                    if (data.getRemind_time() > System.currentTimeMillis()) {
                                        show = "今天";
                                    } else {
                                        show = XUtil.diffTime(data.getRemind_time());
                                    }

                                }
                            } else if (diffDay > 0) {
                                show = diffDay + "天后";
                            } else {
                                show = Math.abs(diffDay) + "天前";
                            }
                        }
                        holder.tvTag.setVisibility(View.VISIBLE);
                        holder.tvTag.setText(show);
//                        if (diffDay <= 0) {
//                            holder.tvTag.setBgColor(getResources().getColor(R.color.gray));
//                        } else {
//                            holder.tvTag.setBgColor(getResources().getColor(R.color.swipe_color3));
//                        }

//                        if (data.getStatus() == EnumData.StatusEnum.OUTDATE.getValue()) {
//                            holder.tvTag.setBackgroundColor(getResources().getColor(R.color.material_brown_500));
//                        } else {
//                            if (data.getType() == 0 && data.getRemind_time() > System.currentTimeMillis()) {
//                                holder.tvTag.setBackgroundColor(getResources().getColor(R.color.material_green_accent_200));
//                            } else {
//                                holder.tvTag.setBackgroundColor(getResources().getColor(R.color.gray));
//                            }
//                        }


                    } else {
                        holder.tvTag.setVisibility(View.GONE);
                    }

                    holder.tvContent.setVisibility(View.VISIBLE);
                    if (data.getRemind_time() > 0) {
                        String show = XUtil.getDateHM(data.getRemind_time());
                        if (data.getType() > 0) {
                            if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_DAY.getValue()) {
                                // 每天
                                show = XUtil.getDateHM(data.getRemind_time());
                            } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_WORK_WEEK.getValue()) {
                                // 每工作日
                                show = XUtil.getDateHM(data.getRemind_time());
                            } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_WEEK.getValue()) {
                                // 每周
                                show = XUtil.getDateEHM(data.getRemind_time());
                            } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_MONTH.getValue()) {
                                // 每月
                                show = XUtil.getDateDHM(data.getRemind_time());
                            } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_YEAR.getValue()) {
                                // 每年
                                show = XUtil.getDateMDEHM(data.getRemind_time());
                            }
                            holder.tvContent.setText(show);
                        } else {
                            holder.tvContent.setText(XUtil.getTimeMDHM(data.getRemind_time()));
                        }
                    } else {
                        holder.tvContent.setVisibility(View.GONE);
                    }

                }
                holder.setClickListener(new MxItemClickListener() {
                                            @Override
                                            public void onClick(View view, int position, boolean isLongClick) {
                                                if (isLongClick) {
                                                    scan(position);
                                                } else {
                                                    if (EnumData.DetailBEnum.remind.getValue() == data.getBusiness_type()) {
                                                        RemindData tmp = rdDao.queryForId(data.getId());
                                                        operateRemindData(tmp, position);
                                                    } else if (EnumData.DetailBEnum.progress.getValue() == data.getBusiness_type()) {
                                                        ProgressData tmp = progressDao.queryForId(data.getId());
                                                        operateProgressData(tmp, position);
                                                    } else if (EnumData.DetailBEnum.check.getValue() == data.getBusiness_type()) {
                                                        CheckListData tmp = clDao.queryForId(data.getId());
                                                        operateCheckListData(tmp, position);
                                                    }


                                                }

                                            }
                                        }

                );

            }


        }
        ;


        XUtil.listViewRandomAnimation(listView, mAdapter);
        getData(true);


        if (ctx.taskData == null && XUtil.notEmptyOrNull(ctx.query)) {
            addButton.setVisibility(View.GONE);

        } else {
            addButton.setVisibility(View.VISIBLE);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add1Dlg(null, -1);
            }
        });
        addButton.setOnLongClickListener(new View.OnLongClickListener() {
                                             @Override
                                             public boolean onLongClick(View v) {
                                                 new MaterialDialog.Builder(ctx)
                                                         .items(new String[]{"清单", "提醒"})
                                                         .itemsCallback(new MaterialDialog.ListCallback() {
                                                             @Override
                                                             public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                                 switch (which) {
                                                                     case 0:
                                                                         add3Dlg(null, -1);
                                                                         break;
                                                                     case 1:
                                                                         add2Dlg(null, -1);
                                                                         break;
                                                                     default:
                                                                         break;
                                                                 }

                                                             }
                                                         })
                                                         .show();
                                                 return true;
                                             }
                                         }

        );


        return view;
    }


    public void scan(int position) {

//        DetailsData tmp = datas.get(position);
//        final ArrayList<ScanData> contents = new ArrayList<ScanData>();
//        if (XUtil.listNotNull(datas)) {
//            for (DetailsData data : datas) {
//                contents.add(new ScanData(data.getId(), data.getId(), EnumData.BusinessEnum.PROGRESS.getValue(), data.getContent(), data.getTime()));
//            }
//        } else {
//            XUtil.tShort("无可查看的内容!");
//            return;
//        }
//        Intent intent = new Intent(ctx, ShowActivity.class);
//        intent.putExtra("current", position);
//        intent.putExtra("type", EnumData.BusinessEnum.PROGRESS.getValue());
//        intent.putExtra("tk_id", tmp.getTk_id());
//        ArrayList list = new ArrayList();
//        list.add(contents);
//        intent.putParcelableArrayListExtra(Constant.KEY_PARAM_LIST, list);
//        startActivity(intent);
    }

    public void operateCheckListData(final CheckListData data, final int pos) {
        final boolean isChecked = EnumData.StatusEnum.OFF.getValue() == data.getStatus();
        new MaterialDialog.Builder(ctx)
                .items(new String[]{isChecked ? "未完成" : "已完成", "编辑", "删除", "收藏", "复制"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                if (isChecked) {
                                    data.setStatus(EnumData.StatusEnum.ON.getValue());
                                } else {
                                    data.setStatus(EnumData.StatusEnum.OFF.getValue());
                                }
                                data.setModify_time(System.currentTimeMillis());
                                clDao.update(data);

                                DetailsData tmp = datas.get(pos);
                                tmp.setStatus(data.getStatus());
                                refreshCell(tmp, false);
                                break;

                            case 1:
                                add3Dlg(data, pos);
                                break;
                            case 2:
                                new MaterialDialog.Builder(ctx)
                                        .title("删除确认?")
                                        .content(data.getTitle())
                                        .theme(Theme.LIGHT)
                                        .positiveText("确定")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                data.setStatus(EnumData.StatusEnum.DEL.getValue());
                                                clDao.update(data);
                                                DetailsData tmp = datas.get(pos);
                                                refreshCell(tmp, true);
                                            }
                                        })
                                        .show();

                                break;
                            case 3:
                                DbUtils.like(data.getTitle(), EnumData.BusinessEnum.PROGRESS.getValue(), data.getId(), data.getTk_id());
                                break;
                            case 4:
                                XUtil.copyText(ctx, data.getTitle());
                                break;

                            default:
                                break;
                        }

                    }
                })
                .show();
    }

    public void operateProgressData(final ProgressData data, final int position) {
        //MyShowActivity
        new MaterialDialog.Builder(ctx)
                .items(new String[]{"编辑", "删除"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                add1Dlg(data, position);
                                break;
                            case 1:
                                new MaterialDialog.Builder(ctx)
                                        .title("删除确认?").content(data.getContent())
                                        .theme(Theme.LIGHT)
                                        .positiveText("确定")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                data.setStatus(EnumData.ProgressStatusEnum.DEL.getValue());
                                                progressDao.update(data);
                                                DetailsData tmp = datas.get(position);
                                                refreshCell(tmp, true);
                                            }
                                        })
                                        .show();
                                break;
                            default:
                                break;
                        }

                    }
                })
                .show();
    }


    public void operateRemindData(final RemindData data, final int pos) {
        final boolean isChecked = EnumData.StatusEnum.OUTDATE.getValue() == data.getStatus();
        new MaterialDialog.Builder(ctx)
                .items(new String[]{isChecked ? "提醒" : "不提醒", "复制", "编辑", "删除"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {

                            case 0:
                                if (!isChecked) {
                                    data.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
                                    XUtil.cancelRemind(ctx, data.getId());
                                } else {
                                    data.setStatus(EnumData.StatusEnum.ON.getValue());
                                    if (data.getRemind_time() < XUtil.getLongTime() && data.getType() == 0) {
                                        XUtil.tShort(notRing);
                                    } else {
                                        //提醒 未来一周以内的 and 重复提醒
                                        initRemind(data);
                                    }
                                }
                                data.setModify_time(System.currentTimeMillis());
                                rdDao.update(data);

                                DetailsData detailsData = datas.get(pos);
                                detailsData.setStatus(data.getStatus());
                                refreshCell(detailsData, false);
                                break;
                            case 1:
                                XUtil.copyText(ctx, data.getContent());
                                break;

                            case 2:
                                add2Dlg(data, pos);
                                break;
                            case 3:
                                new MaterialDialog.Builder(ctx)
                                        .title("删除确认?")
                                        .content(data.getContent())
                                        .theme(Theme.LIGHT)
                                        .positiveText("确定")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                delRemindData(data, pos);
                                            }
                                        })
                                        .show();
                                break;
                        }

                    }
                })
                .show();
    }

    private void delRemindData(RemindData data, int pos) {
        data.setStatus(EnumData.StatusEnum.DEL.getValue());
        rdDao.update(data);
        XUtil.cancelRemind(ctx, data.getId());
        DetailsData detailsData = datas.get(pos);
        refreshCell(detailsData, true);
    }

    private void getData(boolean bFirst) {
        GetData getData = new GetData();
        getData.execute(bFirst);
    }


    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        remindCalendar.set(year, month, day);
        TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false, false);
        timePickerDialog.show(getActivity().getSupportFragmentManager(), "timepicker");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        remindCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        remindCalendar.set(Calendar.MINUTE, minute);
        remind_time = remindCalendar.getTimeInMillis();
        if (tvTime != null) {
            tvTime.setText(XUtil.getTimeYMDHM(new Date(remindCalendar.getTimeInMillis())));
        }
    }


    class GetData extends AsyncTask<Boolean, Integer, Boolean> {

        protected Boolean doInBackground(Boolean... params) {

            boolean canLoadMore = true;
            try {
                if (params[0]) {
                    datas = new ArrayList<DetailsData>();
//                    datas.add(new DetailsData(ctx.taskData.getTitle()));
                }
                List<DetailsData> list = new ArrayList<>();
                if (ctx.taskData != null) {
                    list = DbUtils.getDetailsDatas(ctx.taskData.getId(), datas.size(), datas.size() + Constant.MAX_DB_QUERY);
                } else if (XUtil.notEmptyOrNull(ctx.query)) {
                    list = DbUtils.getDetailsDatasByContent(ctx.query, datas.size(), datas.size() + Constant.MAX_DB_QUERY);
                }


                if (XUtil.listNotNull(list)) {
                    canLoadMore = (list.size() == Constant.MAX_DB_QUERY);
                    datas.addAll(list);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return canLoadMore;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected void onPostExecute(Boolean canLoadMore) {
            super.onPostExecute(canLoadMore);
            mAdapter.setItems(datas, listView);
            swipeLayout.setRefreshing(false);
            onLoading = false;
            try {
                getActivity().invalidaxuteOptionsMenu();
            } catch (Exception e) {
            }
            listView.setCanLoadMore(canLoadMore);
        }
    }


    void add1Dlg(final ProgressData data, final int pos) {
        if (data != null) {
            bUpadte = true;
        } else {
            bUpadte = false;
        }
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setHint("输入进展...");
        editText.setLines(5);
        editText.setTextColor(getResources().getColor(R.color.black));
        if (bUpadte) {
            editText.setText(data.getContent());
            editText.setSelection(data.getContent().length());
        }
        editText.setBackgroundDrawable(new BitmapDrawable());
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    if (!MySp.get(SpConstant.isEnterSave, Boolean.class, false)) {
                        return false;
                    }
                    String content = editText.getText().toString().trim();
                    if (XUtil.notEmptyOrNull(content)) {
                        int tk_id = MySp.getDefaultTk();
                        int pj_id = 0;
                        if (ctx.taskData != null) {
                            tk_id = ctx.taskData.getId();
                            pj_id = ctx.taskData.getPj_id();
                        }
                        ProgressData progressData = new ProgressData(content, EnumData.ProgressTypeEnum.PEOPLE.getValue(),
                                EnumData.ProgressBusinessEnum.REPLY.getValue(), EnumData.ProgressStatusEnum.NORMAL.getValue(),
                                pj_id, tk_id, EnumData.ProgressActionEnum.ADD.getValue()
                        );
                        progressDao.create(progressData);
                        editText.setText("");
                    } else {
                        XUtil.tShort("输入内容~");
                    }
                }
                return false;
            }

        });
        view.addView(editText);
        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText("保存继续")
                .negativeText(bUpadte ? "修改" : "添加")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(materialDialog, false);
                                return;
                            }
                            int tk_id = MySp.getDefaultTk();
                            int pj_id = 0;

                            if (ctx.taskData != null) {
                                tk_id = ctx.taskData.getId();
                                pj_id = ctx.taskData.getPj_id();
                            }
                            ProgressData progressData = new ProgressData(content, EnumData.ProgressTypeEnum.PEOPLE.getValue(),
                                    EnumData.ProgressBusinessEnum.REPLY.getValue(), EnumData.ProgressStatusEnum.NORMAL.getValue(),
                                    pj_id, tk_id, EnumData.ProgressActionEnum.ADD.getValue()
                            );
                            progressDao.create(progressData);
                            editText.setText("");
                            XUtil.dismissShowDialog(materialDialog, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {
                        XUtil.dismissShowDialog(materialDialog, true);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            if (bUpadte) {
                                data.setContent(content);
                                data.setModify_time(XUtil.getLongTime());
                                progressDao.update(data);

                                DetailsData tmp = datas.get(pos);
                                tmp.setContent(content);
                                refreshCell(tmp, false);
                            } else {
                                int tk_id = MySp.getDefaultTk();
                                int pj_id = 0;

                                if (ctx.taskData != null) {
                                    tk_id = ctx.taskData.getId();
                                    pj_id = ctx.taskData.getPj_id();
                                }

                                ProgressData progressData = new ProgressData(content, EnumData.ProgressTypeEnum.PEOPLE.getValue(),
                                        EnumData.ProgressBusinessEnum.REPLY.getValue(), EnumData.ProgressStatusEnum.NORMAL.getValue(),
                                        pj_id, tk_id, EnumData.ProgressActionEnum.ADD.getValue()
                                );
                                progressDao.create(progressData);
                            }
                            updateTkTime();
                            if (!bUpadte) {
                                refresh();
                            }
                            XUtil.dismissShowDialog(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }


    void add2Dlg(final RemindData data, final int pos) {
        calendar = Calendar.getInstance();
        if (data != null) {
            bUpadte = true;
            calendar.setTimeInMillis(data.getRemind_time());
        } else {
            bUpadte = false;
        }
        LayoutInflater mInflater = LayoutInflater.from(ctx);
        View view = mInflater.inflate(R.layout.view_remind_dlg, null);
        final EditText editText = (EditText) view.findViewById(R.id.editText);
        XUtil.autoKeyBoardShow(editText);
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setLines(2);
        editText.setHint("提醒内容...");
        editText.setBackgroundDrawable(new BitmapDrawable());
        tvTime = (TextView) view.findViewById(R.id.tvTime);
        spTime = (Spinner) view.findViewById(R.id.spTime);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(ctx,
                R.array.reminds_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTime.setAdapter(adapter);
        spTime.setVisibility(View.VISIBLE);
        spTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spChoose = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spTimeRepeat = (Spinner) view.findViewById(R.id.spTimeRepeat);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(ctx,
                R.array.reminds_repeat_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTimeRepeat.setAdapter(adapter2);
        spTimeRepeat.setVisibility(View.VISIBLE);
        spTimeRepeat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spRepeatChoose = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        if (bUpadte) {
            remind_time = data.getRemind_time();
            editText.setText(data.getContent());
            editText.setSelection(data.getContent().length());
            tvTime.setText(XUtil.getTimeYMDHM(new Date(remind_time)));
            spTime.setVisibility(View.GONE);
            spTimeRepeat.setSelection(data.getType());
        }
        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText(bUpadte ? "修改" : "添加")
                .negativeText("提醒时间")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            String time = tvTime.getText().toString().trim();

                            Long between = 0l;
                            if (!XUtil.notEmptyOrNull(time)) {

                                switch (spChoose) {
                                    case 0:
                                        between = 10 * Constant.SEC;
                                        break;
                                    case 1:
                                        between = 30 * Constant.SEC;
                                        break;
                                    case 2:
                                        between = 1 * Constant.MIN;
                                        break;
                                    case 3:
                                        between = 2 * Constant.MIN;
                                        break;
                                    case 4:
                                        between = 3 * Constant.MIN;
                                        break;
                                    case 5:
                                        between = 5 * Constant.MIN;
                                        break;
                                    case 6:
                                        between = 10 * Constant.MIN;
                                        break;
                                    case 7:
                                        between = 15 * Constant.MIN;
                                        break;
                                    case 8:
                                        between = 20 * Constant.MIN;
                                        break;
                                    case 9:
                                        between = 25 * Constant.MIN;
                                        break;
                                    case 10:
                                        between = 30 * Constant.MIN;
                                        break;
                                    case 11:
                                        between = 40 * Constant.MIN;
                                        break;
                                    case 12:
                                        between = 45 * Constant.MIN;
                                        break;
                                    case 13:
                                        between = 50 * Constant.MIN;
                                        break;
                                    case 14:
                                        between = 1 * Constant.HOUR;
                                        break;
                                    case 15:
                                        between = 2 * Constant.HOUR;
                                        break;
                                    case 16:
                                        between = 3 * Constant.HOUR;
                                        break;
                                    case 17:
                                        between = 4 * Constant.HOUR;
                                        break;
                                    case 18:
                                        between = 1 * Constant.DAY;
                                        break;
                                    case 19:
                                        between = 2 * Constant.DAY;
                                        break;
                                    case 20:
                                        between = 3 * Constant.DAY;
                                        break;
                                    case 21:
                                        between = 4 * Constant.DAY;
                                        break;
                                    case 22:
                                        between = 5 * Constant.DAY;
                                        break;
                                    case 23:
                                        between = 6 * Constant.DAY;
                                        break;
                                    case 24:
                                        between = 7 * Constant.DAY;
                                        break;
                                    case 25:
                                        between = 15 * Constant.DAY;
                                        break;
                                    case 26:
                                        between = 30 * Constant.DAY;
                                        break;
                                    case 27:
                                        between = 45 * Constant.DAY;
                                        break;
                                    case 28:
                                        between = 60 * Constant.DAY;
                                        break;
                                    case 29:
                                        between = 90 * Constant.DAY;
                                        break;
                                    case 30:
                                        between = 365 * Constant.DAY;
                                        break;
                                }
                                remind_time = System.currentTimeMillis() + between;
                            }


                            if (remind_time == null) {
                                XUtil.tShort("选择提醒时间~");
                                XUtil.dismissShowDialog(materialDialog, false);
                                return;
                            }


                            if (!XUtil.notEmptyOrNull(content)) {
                                content = XUtil.getTimeYMDHM(remind_time);
                            }
                            if (bUpadte) {
                                data.setContent(content);
                                data.setModify_time(XUtil.getLongTime());
                                data.setRemind_time(remind_time);
                                data.setType(spRepeatChoose);
                                if (remind_time < XUtil.getLongTime() && data.getType() == 0) {
                                    data.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
                                } else {
                                    //提醒 未来一周以内的 and 重复提醒
                                    initRemind(data);
//                                    if (data.getType() == 0) {
//                                        XUtil.tLong(XUtil.diffTime(data.getRemind_time()) + "后提醒");
//                                    }
                                }
                                rdDao.update(data);

                                DetailsData tmp = datas.get(pos);
                                tmp.setContent(content);
                                tmp.setRemind_time(remind_time);
                                tmp.setType(spRepeatChoose);
                                tmp.setStatus(data.getStatus());
                                refreshCell(tmp, false);
                            } else {
                                int tk_id = MySp.getDefaultTk();
                                if (ctx.taskData != null) {
                                    tk_id = ctx.taskData.getId();
                                }
                                RemindData remindData = new RemindData(content, EnumData.StatusEnum.ON.getValue(), tk_id,
                                        remind_time, spRepeatChoose
                                );
                                rdDao.create(remindData);
                                int _id = 0;
                                try {
                                    ArrayList<RemindData> rdDatas = new ArrayList<RemindData>();
                                    QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
                                    builder.orderBy("id", false).limit(1l);
                                    rdDatas = (ArrayList<RemindData>) rdDao.query(builder.prepare());
                                    _id = rdDatas.get(0).getId();
                                } catch (Exception e) {
                                }
                                remindData.setId(_id);
                                if (remind_time < XUtil.getLongTime() && remindData.getType() == 0) {
//                                    XUtil.tShort(notRing);
                                    remindData.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
                                    rdDao.update(remindData);


                                    DetailsData tmp = datas.get(pos);
                                    tmp.setContent(content);
                                    tmp.setRemind_time(remind_time);
                                    tmp.setType(spRepeatChoose);
                                    tmp.setStatus(data.getStatus());
                                    refreshCell(tmp, false);
                                } else {
                                    //提醒 未来一周以内的 and 重复提醒
                                    initRemind(remindData);
//                                    if (remindData.getType() == 0) {
//                                        XUtil.tLong(XUtil.diffTime(remindData.getRemind_time()) + "后提醒");
//                                    }
                                }
                            }


                            updateTkTime();
                            if (!bUpadte) {
                                refresh();
                            }
                            XUtil.dismissShowDialog(materialDialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {


                        XUtil.dismissShowDialog(materialDialog, true);
                    }

                    @Override
                    public void onNegative(MaterialDialog materialDialog) {
                        initDatePicker();
                        spTime.setVisibility(View.GONE);
                        XUtil.dismissShowDialog(materialDialog, false);
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();


    }


    void add3Dlg(final CheckListData data, final int pos) {


        if (data != null) {
            bUpadte = true;
        } else {
            bUpadte = false;
        }
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setLines(5);
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setHint("输入安排...");
        if (bUpadte) {
            editText.setText(data.getTitle());
            editText.setSelection(data.getTitle().length());
        }
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);


        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText("保存继续")
                .negativeText(bUpadte ? "修改" : "添加")
                .neutralText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                return;
                            }
                            int tk_id = MySp.getDefaultTk();
                            if (ctx.taskData != null) {
                                tk_id = ctx.taskData.getId();
                            }
                            CheckListData checkListData = null;
                            if (MySp.get(SpConstant.isCheckListEnter, Boolean.class, false) && content.contains("\n")) {
                                String arr[] = content.split("\\n");
                                for (int i = 0; i < arr.length; i++) {
                                    checkListData = new CheckListData(arr[i], EnumData.StatusEnum.ON.getValue(), EnumData.TaskLevelEnum.NO.getValue(),
                                            tk_id
                                    );
                                    clDao.create(checkListData);
                                }
                            } else {
                                checkListData = new CheckListData(content, EnumData.StatusEnum.ON.getValue(), EnumData.TaskLevelEnum.NO.getValue(),
                                        tk_id
                                );
                                clDao.create(checkListData);
                            }


                            editText.setText("");
                            XUtil.dismissShowDialog(materialDialog, false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {
                        XUtil.dismissShowDialog(materialDialog, true);
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        try {
                            String content = editText.getText().toString().trim();
                            if (!XUtil.notEmptyOrNull(content)) {
                                XUtil.tShort("输入内容~");
                                XUtil.dismissShowDialog(dialog, false);
                                return;
                            }
                            if (bUpadte) {
                                data.setTitle(content);
                                data.setModify_time(XUtil.getLongTime());
                                clDao.update(data);

                                DetailsData tmp = datas.get(pos);
                                tmp.setContent(content);
                                refreshCell(tmp, false);
                            } else {
                                //String title, int status, int level, int tk_id
                                int tk_id = MySp.getDefaultTk();
                                if (ctx.taskData != null) {
                                    tk_id = ctx.taskData.getId();
                                }
                                CheckListData checkListData = null;
                                if (MySp.get(SpConstant.isCheckListEnter, Boolean.class, false) && content.contains("\n")) {
                                    String arr[] = content.split("\\n");
                                    for (int i = 0; i < arr.length; i++) {
                                        checkListData = new CheckListData(arr[i], EnumData.StatusEnum.ON.getValue(), EnumData.TaskLevelEnum.NO.getValue(),
                                                tk_id
                                        );
                                        clDao.create(checkListData);
                                    }
                                } else {
                                    checkListData = new CheckListData(content, EnumData.StatusEnum.ON.getValue(), EnumData.TaskLevelEnum.NO.getValue(),
                                            tk_id
                                    );
                                    clDao.create(checkListData);
                                }
                            }
                            updateTkTime();
                            if (!bUpadte) {
                                refresh();
                            }
                            XUtil.dismissShowDialog(dialog, true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();
    }


    private void initDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(2000, 2025);
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "datepicker");
    }

    public void initRemind(RemindData data) {
        if (data.getRemind_time() > XUtil.getLongTime() && data.getRemind_time() < XUtil.getLongTime() + Constant.DAY * 7) {
            XUtil.ring(ctx, data);
        } else if (data.getType() > 0 && data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
            XUtil.doRemind(ctx, data);
        }
    }

    private void add2QuickDlg() {
        final String items[] = new String[]{"10秒", "30秒", "1分钟", "2分钟", "3分钟", "5分钟", "10分钟", "15分钟", "20分钟", "25分钟", "30分钟",
                "40分钟", "45分钟", "50分钟", "1小时", "2小时", "3小时", "4小时", "1天", "2天", "3天", "4天", "5天", "6天", "7天", "15天", "30天", "45天", "60天", "90天", "一年"};
        new MaterialDialog.Builder(ctx)
                .title("倒计时")
                .items(items)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                Long between = 0l;
                                switch (which) {
                                    case 0:
                                        between = 10 * Constant.SEC;
                                        break;
                                    case 1:
                                        between = 30 * Constant.SEC;
                                        break;
                                    case 2:
                                        between = 1 * Constant.MIN;
                                        break;
                                    case 3:
                                        between = 2 * Constant.MIN;
                                        break;
                                    case 4:
                                        between = 3 * Constant.MIN;
                                        break;
                                    case 5:
                                        between = 5 * Constant.MIN;
                                        break;
                                    case 6:
                                        between = 10 * Constant.MIN;
                                        break;
                                    case 7:
                                        between = 15 * Constant.MIN;
                                        break;
                                    case 8:
                                        between = 20 * Constant.MIN;
                                        break;
                                    case 9:
                                        between = 25 * Constant.MIN;
                                        break;
                                    case 10:
                                        between = 30 * Constant.MIN;
                                        break;
                                    case 11:
                                        between = 40 * Constant.MIN;
                                        break;
                                    case 12:
                                        between = 45 * Constant.MIN;
                                        break;
                                    case 13:
                                        between = 50 * Constant.MIN;
                                        break;
                                    case 14:
                                        between = 1 * Constant.HOUR;
                                        break;
                                    case 15:
                                        between = 2 * Constant.HOUR;
                                        break;
                                    case 16:
                                        between = 3 * Constant.HOUR;
                                        break;
                                    case 17:
                                        between = 4 * Constant.HOUR;
                                        break;
                                    case 18:
                                        between = 1 * Constant.DAY;
                                        break;
                                    case 19:
                                        between = 2 * Constant.DAY;
                                        break;
                                    case 20:
                                        between = 3 * Constant.DAY;
                                        break;
                                    case 21:
                                        between = 4 * Constant.DAY;
                                        break;
                                    case 22:
                                        between = 5 * Constant.DAY;
                                        break;
                                    case 23:
                                        between = 6 * Constant.DAY;
                                        break;
                                    case 24:
                                        between = 7 * Constant.DAY;
                                        break;
                                    case 25:
                                        between = 15 * Constant.DAY;
                                        break;
                                    case 26:
                                        between = 30 * Constant.DAY;
                                        break;
                                    case 27:
                                        between = 45 * Constant.DAY;
                                        break;
                                    case 28:
                                        between = 60 * Constant.DAY;
                                        break;
                                    case 29:
                                        between = 90 * Constant.DAY;
                                        break;
                                    case 30:
                                        between = 365 * Constant.DAY;
                                        break;

                                }
                                remind_time = System.currentTimeMillis() + between;
                                int tk_id = MySp.getDefaultTk();
                                if (ctx.taskData != null) {
                                    tk_id = ctx.taskData.getId();
                                }
                                String content = items[which] + "_" + XUtil.getTimeYMDHM(remind_time);
                                RemindData
                                        remindData = new RemindData(content, EnumData.StatusEnum.ON.getValue(), tk_id,
                                        remind_time, 0
                                );
                                rdDao.create(remindData);
                                int _id = 0;
                                try {
                                    ArrayList<RemindData> rdDatas = new ArrayList<RemindData>();
                                    QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
                                    builder.orderBy("id", false).limit(1l);
                                    rdDatas = (ArrayList<RemindData>) rdDao.query(builder.prepare());
                                    _id = rdDatas.get(0).getId();
                                } catch (Exception e) {
                                }
                                remindData.setId(_id);
                                if (remind_time < XUtil.getLongTime() && remindData.getType() == 0) {
                                    remindData.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
                                    rdDao.update(remindData);
                                } else {
                                    //提醒 未来一周以内的 and 重复提醒
                                    initRemind(remindData);
//                                    if (remindData.getType() == 0) {
//                                        XUtil.tLong(XUtil.diffTime(remindData.getRemind_time()) + "后提醒");
//                                    }
                                }
                                updateTkTime();
                                if (!bUpadte) {
                                    refresh();
                                }
                                return true;
                            }
                        }

                )
                .show();
    }

    private void updateTkTime() {
        if (ctx.taskData != null) {
            ctx.taskData.setModify_time(XUtil.getLongTime());
            taskDao.update(ctx.taskData);
        }
    }

    @Override
    public void onRefresh() {
        if (onLoading) {
            return;
        }
        refresh();
    }

    private void refresh() {
        onLoading = true;
        swipeLayout.setRefreshing(true);
        getData(true);
    }


    @Override
    public void onLoadMore() {

        getData(false);

    }

    //更新CELL
    private void refreshCell(DetailsData data, boolean bDel) {
        if (bDel) {
            datas.remove(data);
        } else {
            for (int i = 0; i < datas.size(); i++) {
                if (data.getId() == datas.get(i).getId()) {
                    datas.set(i, data);
                    break;
                }
            }
        }
        mAdapter.setItems(datas, listView);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (!XUtil.notEmptyOrNull(ctx.query)) {
            menu.add("to_task").setIcon(XUtil.initIconWhite(Iconify.IconValue.md_subject)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item == null || item.getTitle() == null) {
            return false;
        }


        if (item.getTitle().equals("to_task")) {
            Intent newIntent = new Intent(ctx, TkAddActivity.class);
            newIntent.putExtra(Constant.KEY_PARAM_DATA, ctx.taskData);
            startActivity(newIntent);
        }
        return false;
    }
}
