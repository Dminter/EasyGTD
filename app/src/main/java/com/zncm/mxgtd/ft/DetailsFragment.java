package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.adapter.ProjectAdapter;
import com.zncm.mxgtd.data.CheckListData;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.ItemDetailsActivity;
import com.zncm.mxgtd.ui.TextActivity;
import com.zncm.mxgtd.ui.TkDetailsActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.loadmore.MxItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *详情界面，任务【进展，清单，提醒，合并显示】
 */
public class DetailsFragment extends BaseListFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ProjectAdapter mAdapter;
    private Activity ctx;
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
    private String query;
    private TaskData curTaskData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            Serializable dataParam = bundle.getSerializable(Constant.KEY_PARAM_DATA);
            if (dataParam != null && dataParam instanceof TaskData) {
                curTaskData = (TaskData) dataParam;
            }
            query = bundle.getString("query");
        }
        datas = new ArrayList<DetailsData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(final int position, PjViewHolder holder) {
                final DetailsData data = datas.get(position);
                if (XUtil.notEmptyOrNull(data.getContent())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    String title = data.getContent();
                    if (XUtil.notEmptyOrNull(query)) {
                        SpannableString str = null;
                        if (!query.equals(EnumData.queryEnum._TODAY.getValue())) {
                            str = XUtil.getSerachString(title, query);
                        } else {
                            str = SpannableString.valueOf(title);
                        }
                        holder.tvTitle.setText(str);
                    } else {
                        holder.tvTitle.setText(title);
                    }
                    if (title.length() < Constant.MAX_LEN && EnumData.DetailBEnum.progress.getValue() == data.getBusiness_type()) {
                        holder.tvTitle.setTextSize(Constant.FS_1);
                    } else {
                        holder.tvTitle.setTextSize(Constant.FS_2);
                    }
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                    holder.tvTitle.setTextSize(Constant.FS_2);
                }


                if (!MySp.getShowSimple()) {
                    if (data.getTime() != null) {
                        holder.tvTime.setText(XUtil.getDateYMDEHM(data.getTime()));
                        holder.tvTime.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvTime.setVisibility(View.GONE);
                    }
                }


                holder.tvContent.setVisibility(View.GONE);
                if (EnumData.DetailBEnum.progress.getValue() == data.getBusiness_type()) {
                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgprogress));
                    holder.tvTag.setVisibility(View.GONE);
                } else if (EnumData.DetailBEnum.check.getValue() == data.getBusiness_type()) {
                    holder.tvTag.setVisibility(View.GONE);
                    if (data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgcheck_));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgcheck));
                    }
                } else if (EnumData.DetailBEnum.remind.getValue() == data.getBusiness_type()) {
                    if (data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgremind_));
                    } else {
                        holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgremind));
                    }
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
                                                } else {
                                                    DetailsData tmp = datas.get(position);
                                                    if (tmp == null) {
                                                        return;
                                                    }
                                                    if (XUtil.notEmptyOrNull(query)) {
//                                                        Intent intent = new Intent(ctx, TextActivity.class);
//                                                        intent.putExtra(Constant.KEY_PARAM_DATA, tmp);
//                                                        intent.putExtra("text", tmp.getContent());
//                                                        startActivity(intent);
                                                        Intent intent = new Intent(ctx, ItemDetailsActivity.class);
                                                        intent.putExtra("DetailsData", tmp);
                                                        intent.putExtra("size", 1);
                                                        intent.putExtra(Constant.KEY_CURRENT_POSITION, 0);
                                                        startActivity(intent);
                                                    } else {
                                                        Intent intent = new Intent(ctx, ItemDetailsActivity.class);
                                                        intent.putExtra(Constant.KEY_PARAM_DATA, curTaskData);
                                                        intent.putExtra("size", datas.size());
                                                        intent.putExtra(Constant.KEY_CURRENT_POSITION, position);
                                                        startActivity(intent);
                                                    }
                                                }

                                            }
                                        }
                );

            }
        };
        XUtil.listViewRandomAnimation(listView, mAdapter);
        getData(true);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add1Dlg(null, -1);
            }
        });
        addButton.setOnLongClickListener(new View.OnLongClickListener() {
                                             @Override
                                             public boolean onLongClick(View v) {
                                                 XUtil.themeMaterialDialog(ctx)
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

    public void operateCheckListData(final CheckListData data, final int pos) {
        boolean isLike = DbUtils.isLiked(EnumData.BusinessEnum.CHECK_LIST.getValue(), data.getId());
        final boolean isChecked = EnumData.StatusEnum.OFF.getValue() == data.getStatus();
        XUtil.themeMaterialDialog(ctx)
                .items(new String[]{isChecked ? "未完成" : "已完成", "编辑", "删除", isLike ? "移除收藏" : "收藏", "复制", "笔记本"})
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
                               XUtil.themeMaterialDialog(ctx)
                                        .title("删除确认?")
                                        .content(data.getTitle())
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
                                DbUtils.like(data.getTitle(), EnumData.BusinessEnum.CHECK_LIST.getValue(), data.getId(), data.getTk_id());
                                break;
                            case 4:
                                XUtil.copyText(ctx, data.getTitle());
                                break;
                            case 5:
                                toTkDetails(data.getTk_id());
                                break;
                            default:
                                break;
                        }

                    }
                })
                .show();
    }

    public void operateProgressData(final ProgressData data, final int position) {
        boolean isLike = DbUtils.isLiked(EnumData.BusinessEnum.PROGRESS.getValue(), data.getId());
        XUtil.themeMaterialDialog(ctx)
                .items(new String[]{"编辑", "删除", isLike ? "移除收藏" : "收藏", "复制", "笔记本"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                add1Dlg(data, position);
                                break;
                            case 1:
                                XUtil.themeMaterialDialog(ctx)
                                        .title("删除确认?").content(data.getContent())
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

                            case 2:
                                DbUtils.like(data.getContent(), EnumData.BusinessEnum.PROGRESS.getValue(), data.getId(), data.getTk_id());
                                break;
                            case 3:
                                XUtil.copyText(ctx, data.getContent());
                                break;

                            case 4:
                                toTkDetails(data.getTk_id());
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
        XUtil.themeMaterialDialog(ctx)
                .items(new String[]{isChecked ? "提醒" : "不提醒", "复制", "编辑", "删除", "笔记本"})
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
                               XUtil.themeMaterialDialog(ctx)
                                        .title("删除确认?")
                                        .content(data.getContent())
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

                            case 4:
                                toTkDetails(data.getTk_id());
                                break;

                        }

                    }
                })
                .show();
    }

    private void toTkDetails(int tk_id) {
        TaskData tk = taskDao.queryForId(tk_id);
        if (tk != null) {
            Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
            newIntent.putExtra(Constant.KEY_PARAM_DATA, tk);
            startActivity(newIntent);
        }
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
                }
                List<DetailsData> list = new ArrayList<>();
                if (ctx instanceof TkDetailsActivity) {
                    query = ((TkDetailsActivity) ctx).query;
                }
                if (XUtil.notEmptyOrNull(query)) {
                    list = DbUtils.getDetailsDatasByContent(query, datas.size(), datas.size() + Constant.MAX_DB_QUERY);
                } else if (curTaskData != null) {
                    list = DbUtils.getDetailsDatas(curTaskData.getId(), datas.size(), datas.size() + Constant.MAX_DB_QUERY);
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
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
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
                        if (curTaskData != null) {
                            tk_id = curTaskData.getId();
                            pj_id = curTaskData.getPj_id();
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
        MaterialDialog md =  XUtil.themeMaterialDialog(ctx)
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

                            if (curTaskData != null) {
                                tk_id = curTaskData.getId();
                                pj_id = curTaskData.getPj_id();
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

                                if (curTaskData != null) {
                                    tk_id = curTaskData.getId();
                                    pj_id = curTaskData.getPj_id();
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
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
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
        MaterialDialog md =  XUtil.themeMaterialDialog(ctx)
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
                                if (curTaskData != null) {
                                    tk_id = curTaskData.getId();
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
                                    remindData.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
                                    rdDao.update(remindData);
                                    DetailsData tmp = datas.get(pos);
                                    tmp.setContent(content);
                                    tmp.setRemind_time(remind_time);
                                    tmp.setType(spRepeatChoose);
                                    tmp.setStatus(data.getStatus());
                                    refreshCell(tmp, false);
                                } else {
                                    initRemind(remindData);
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
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        editText.setHint("输入安排...");
        if (bUpadte) {
            editText.setText(data.getTitle());
            editText.setSelection(data.getTitle().length());
        }
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);
        MaterialDialog md = XUtil.themeMaterialDialog(ctx)
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
                            if (curTaskData != null) {
                                tk_id = curTaskData.getId();
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
                                int tk_id = MySp.getDefaultTk();
                                if (curTaskData != null) {
                                    tk_id = curTaskData.getId();
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

    private void updateTkTime() {
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
}
