package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
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


public class RemindFragment extends BaseListFragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private ProjectAdapter mAdapter;
    private Activity ctx;
    public List<RemindData> datas = null;
    private boolean onLoading = false;
    private TaskData taskData;
    private boolean bUpadte = false;
    private boolean inTk = false;
    private Long remind_time = null;
    private Calendar calendar = Calendar.getInstance();
    private Calendar remindCalendar = Calendar.getInstance();
    private TextView tvTime;
    private Spinner spTime;
    private Spinner spTimeRepeat;
    private int spChoose = 0;
    private int spRepeatChoose = 0;


    private Long curDate;
    private boolean bCal = false;
    private int status = EnumData.StatusEnum.ON.getValue();
    private boolean bOutDate = false;

    private String notRing = "已过期，将不会提醒！";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        Serializable dataParam = ctx.getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
        taskData = (TaskData) dataParam;
        if (taskData == null) {
            int tk_id = ctx.getIntent().getIntExtra(Intent.EXTRA_UID, 0);
            taskData = DbUtils.getTkById(tk_id);
        }
        if (taskData != null) {
            inTk = true;
        } else {
            inTk = false;
        }

        if (ctx.getIntent().getExtras() != null) {
            curDate = ctx.getIntent().getExtras().getLong(Constant.KEY_PARAM_LONG);
        }

        if (curDate != null && curDate > 0) {
            bCal = true;
            remind_time = curDate;
        }

        datas = new ArrayList<RemindData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(int position, PjViewHolder holder) {
                final RemindData data = datas.get(position);
                String title = data.getContent();
//                if (data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
//                    title = "0" + title;
//                    if (data.getRemind_time() < System.currentTimeMillis()) {
//                        title = "0" + title;
//                    }
//
//                } else if (data.getStatus() == EnumData.StatusEnum.OUTDATE.getValue()) {
//                    title = "1" + title;
//                }
                if (XUtil.notEmptyOrNull(title)) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(title);
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }


//                if (data.getRemind_time() < System.currentTimeMillis()&&data.getStatus() == EnumData.StatusEnum.ON.getValue()){
//
//                }else {
//
//
//                }
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgremind));
                holder.tvTag.setTextColor(getResources().getColor(R.color.colorKeep6));
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
                        } else {
                            show = Math.abs(diffDay) + "天 " + XUtil.diffWeekDays(Math.abs(diffDay));
                        }
                    }
                    holder.tvTag.setVisibility(View.VISIBLE);
                    holder.tvTag.setText(show);
                    if (diffDay <= 0 && data.getType() == 0) {
//                        if (data.getStatus() == EnumData.StatusEnum.OUTDATE.getValue() || data.getRemind_time() < System.currentTimeMillis()) {
//                            holder.tvTag.setTextColor(getResources().getColor(R.color.colorKeep2));
//                        } else {
//                            holder.tvTag.setTextColor(getResources().getColor(R.color.colorKeep6));
//                        }

                    } else {
                        holder.tvTag.setTextColor(getResources().getColor(R.color.colorKeep3));
                    }
                } else {

                    holder.tvTag.setVisibility(View.GONE);
                }

                if (data.getRemind_time() > 0) {
                    holder.tvContent.setVisibility(View.VISIBLE);
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
                        holder.tvTag.setTextColor(getResources().getColor(R.color.colorKeeptb));
                    } else {
                        holder.tvContent.setText(XUtil.getTimeMDHM(data.getRemind_time()));
//                        holder.tvContent.setText(XUtil.getTimeMDHM(data.getRemind_time()) + " " + XUtil.getTimeMDHM(data.getTime()));
                    }
                } else {
                    holder.tvContent.setVisibility(View.GONE);
                }


                holder.setClickListener(new MxItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {
                            final RemindData data = datas.get(position);
                            operate(data);
                        } else {
                            if (inTk) {
                                initDlg(datas.get(position));
                            } else {
                                TaskData tk = taskDao.queryForId(datas.get(position).getTk_id());
                                if (tk != null) {
                                    Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
                                    newIntent.putExtra(Constant.KEY_PARAM_DATA, tk);
                                    startActivity(newIntent);
                                }
                            }
                        }

                    }
                });
            }

        };

        XUtil.listViewRandomAnimation(listView, mAdapter);
//        listView.setAdapter(mAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @SuppressWarnings({"rawtypes", "unchecked"})
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                int curPosition = position - listView.getHeaderViewsCount();
//                if (curPosition >= 0 && curPosition < datas.size() && inTk) {
//                    add3Dlg(datas.get(curPosition));
//                } else {
//                    TaskData tk = taskDao.queryForId(datas.get(position).getTk_id());
//                    if (tk != null) {
//                        Intent newIntent = null;
//                        newIntent = new Intent(ctx, TaskDetailsActivity.class);
//                        newIntent.putExtra(Constant.KEY_PARAM_DATA, tk);
//                        newIntent.putExtra(Constant.KEY_PARAM_ID, 2);
//                        startActivity(newIntent);
//                    }
//                }
//            }
//        });
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final RemindData data = datas.get(i);
//
//                operate(data);
//                return true;
//            }
//        });

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            status = bundle.getInt(Constant.KEY_PARAM_TYPE);
            if (status == EnumData.StatusEnum.OUTDATE.getValue()) {
                bOutDate = true;
            }
        }
        getData(true);
        initAddBtn();
        return view;
    }

    public void operate(final RemindData data) {
        final boolean isChecked = EnumData.StatusEnum.OUTDATE.getValue() == data.getStatus();
         XUtil.themeMaterialDialog(ctx)
                .items(new String[]{isChecked ? "提醒" : "不提醒", "复制", "编辑", "删除", "批量删除"})
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
                                        if (data.getType() == 0) {
                                            XUtil.tLong(XUtil.diffTime(data.getRemind_time()) + "后提醒");
                                        }
                                    }
                                }
                                data.setModify_time(System.currentTimeMillis());
                                rdDao.update(data);
                                refreshCell(data, false);
                                break;
                            case 1:
                                XUtil.copyText(ctx, data.getContent());
                                break;

                            case 2:
                                initDlg(data);
                                break;
                            case 3:
                                if (MySp.get(SpConstant.isDeleteConfirm, Boolean.class, true)) {
                                     XUtil.themeMaterialDialog(ctx)
                                            .title("删除确认?")
                                            .content(data.getContent())
                                            .positiveText("确定")
                                            .negativeText("取消")
                                            .callback(new MaterialDialog.ButtonCallback() {
                                                @Override
                                                public void onPositive(MaterialDialog materialDialog) {
                                                    delDo(data);
                                                }
                                            })
                                            .show();
                                } else {
                                    delDo(data);
                                }

                                break;
                            case 4:
                                if (!XUtil.listNotNull(datas)) {
                                    XUtil.tShort("没有要删除的项~");
                                    return;
                                }
                                String items[] = new String[datas.size()];
                                for (int i = 0; i < datas.size(); i++) {
                                    items[i] = datas.get(i).getContent();
                                }

                                 XUtil.themeMaterialDialog(ctx)
                                        .title("批量删除")
                                        .items(items)
                                        .itemColor(getResources().getColor(R.color.red))
                                        .neutralText("取消")
                                        .itemsCallbackMultiChoice(null, new MaterialDialog.ListCallbackMultiChoice() {
                                            @Override
                                            public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                                                for (Integer tmp : which) {
                                                    DbUtils.delRemindById(datas.get(tmp).getId());
                                                }
                                                refresh();
                                                return true;
                                            }
                                        })
                                        .positiveText("确定")
                                        .show();
                                break;
                            default:
                                break;
                        }

                    }
                })
                .show();
    }

    private void delDo(RemindData data) {
        data.setStatus(EnumData.StatusEnum.DEL.getValue());
        rdDao.update(data);
        XUtil.cancelRemind(ctx, data.getId());
        refreshCell(data, true);
    }

    private void initAddBtn() {


        if (bOutDate) {
            addButton.setVisibility(View.GONE);
        } else {
            addButton.setVisibility(View.VISIBLE);
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initDlg(null);
            }
        });


        addButton.setOnLongClickListener(new View.OnLongClickListener() {
                                             @Override
                                             public boolean onLongClick(View v) {
                                                 final String items[] = new String[]{"10秒", "30秒", "1分钟", "2分钟", "3分钟", "5分钟", "10分钟", "15分钟", "20分钟", "25分钟", "30分钟",
                                                         "40分钟", "45分钟", "50分钟", "1小时", "2小时", "3小时", "4小时", "1天", "2天", "3天", "4天", "5天", "6天", "7天", "15天", "30天", "45天", "60天", "90天", "一年"};
                                                  XUtil.themeMaterialDialog(ctx)
                                                         .title("倒计时")
                                                         .items(items)
                                                         .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                                                                     @Override
                                                                     public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                                                         Long between = 0l;
                                                                         between = getRdTime(which, between);
                                                                         remind_time = System.currentTimeMillis() + between;
                                                                         int tk_id = MySp.getDefaultTk();
                                                                         if (taskData != null) {
                                                                             tk_id = taskData.getId();
                                                                         }
                                                                         Integer maxInt = 0;
                                                                         try {
                                                                             maxInt = Integer.parseInt(clDao.queryRaw("SELECT max(id) from reminddata").getFirstResult()[0]);
                                                                         } catch (Exception e) {

                                                                         }
                                                                         String content = "倒计时_" + items[which] + "_" + maxInt;
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
                                                                             refreshCell(remindData, false);
                                                                         } else {
                                                                             //提醒 未来一周以内的 and 重复提醒
                                                                             initRemind(remindData);
                                                                             if (remindData.getType() == 0) {
                                                                                 XUtil.tLong(XUtil.diffTime(remindData.getRemind_time()) + "后提醒");
                                                                             }
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

                                                 return true;
                                             }
                                         }

        );

    }

    public static Long getRdTime(int which, Long between) {
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
        return between;
    }


    private void getData(boolean bFirst) {
        GetData getData = new GetData();
        getData.execute(bFirst);
    }

    class GetData extends AsyncTask<Boolean, Integer, Boolean> {

        protected Boolean doInBackground(Boolean... params) {
            boolean canLoadMore = true;
            try {
                QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
                if (inTk) {
                    builder.where().eq("tk_id", taskData.getId()).and().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OUTDATE.getValue());
                } else {
                    if (bCal) {
                        builder.where().eq("status", status).and().between("remind_time", curDate, curDate + Constant.DAY);
                    } else {
                        if (status == EnumData.StatusEnum.NONE.getValue()) {
                            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().gt("type", 0);
                        } else if (status == EnumData.StatusEnum.ON.getValue()) {
                            builder.where().eq("status", status).and().eq("type", 0);
                        } else if (status == EnumData.StatusEnum.OFF.getValue()) {
                            builder.where().eq("status", status);
                        } else if (status == EnumData.StatusEnum.OUTDATE.getValue()) {
                            builder.where().eq("status", status);
                        } else {
                            builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
                        }
                    }
                }


                Long curTime = System.currentTimeMillis();
                if (params[0]) {
                    if (bOutDate) {
                        builder.orderBy("remind_time", false).limit(Constant.MAX_DB_QUERY);
                    } else {
                        builder.orderByRaw("status ASC,type ASC,abs(remind_time-" + curTime + ") ASC").limit(Constant.MAX_DB_QUERY);
                    }
                } else {
                    if (bOutDate) {
                        builder.orderBy("remind_time", false).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                    } else {
                        builder.orderByRaw("status ASC,type ASC,abs(remind_time-" + curTime + ") ASC").limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                    }
                }
                List<RemindData> list = rdDao.query(builder.prepare());

                if (params[0]) {
                    datas = new ArrayList<RemindData>();
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
                getActivity().invalidateOptionsMenu();
            } catch (Exception e) {
            }
            listView.setCanLoadMore(canLoadMore);


        }

    }

    public void initRemind(RemindData data) {
        if (data.getRemind_time() > XUtil.getLongTime() && data.getRemind_time() < XUtil.getLongTime() + Constant.DAY * 7) {
            XUtil.ring(ctx, data);
        } else if (data.getType() > 0 && data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
            XUtil.doRemind(ctx, data);
        }
    }


//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (!bOutDate) {
//            if (!inTk && !bCal) {
//                menu.add("calendar").setIcon(XUtil.initIconWhite(Iconify.IconValue.fa_calendar)).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
//            }
//        }
//    }
//
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getTitle().equals("calendar")) {
//            Intent newIntent = null;
//            newIntent = new Intent(ctx, CalActivity.class);
//            newIntent.putExtra("type", 2);
//            startActivity(newIntent);
//        }
//        return false;
//    }

    private void initDatePicker() {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setYearRange(2000, 2025);
        datePickerDialog.show(getActivity().getSupportFragmentManager(), "datepicker");
    }

    void initDlg(final RemindData data) {


        calendar = Calendar.getInstance();

        if (curDate != null && curDate > 0) {
            calendar.setTimeInMillis(curDate);
        }


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


        if (curDate != null && curDate > 0) {
            remind_time += 10 * Constant.HOUR;
            tvTime.setText(XUtil.getTimeYMDHM(new Date(remind_time)));
            spTime.setVisibility(View.GONE);
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
                            if (!XUtil.notEmptyOrNull(content)) {
//                                XUtil.tShort("输入提醒内容~");
//                                XUtil.dismissShowDialog(materialDialog, false);
//                                return;
                                Integer maxInt = 0;
                                try {
                                    maxInt = Integer.parseInt(clDao.queryRaw("SELECT max(id) from reminddata").getFirstResult()[0]);
                                } catch (Exception e) {

                                }
                                content = "提醒_" + maxInt;
                            }
                            Long between = 0l;
                            if (!XUtil.notEmptyOrNull(time)) {
                                between = getRdTime(spChoose, between);
//                                switch (spChoose) {
//                                    case 0:
//                                        between = 5 * Constant.MIN;
//                                        break;
//                                    case 1:
//                                        between = 10 * Constant.MIN;
//                                        break;
//                                    case 2:
//                                        between = 15 * Constant.MIN;
//                                        break;
//                                    case 3:
//                                        between = 30 * Constant.MIN;
//                                        break;
//                                    case 4:
//                                        between = 60 * Constant.MIN;
//                                        break;
//                                    case 5:
//                                        between = 2 * 60 * Constant.MIN;
//                                        break;
//                                    case 6:
//                                        between = Constant.DAY;
//                                        break;
//                                    case 7:
//                                        between = 2 * Constant.DAY;
//                                        break;
//                                    default:
//                                        between = 5 * Constant.MIN;
//                                        break;
//                                }
                                remind_time = System.currentTimeMillis() + between;
                            }


                            if (remind_time == null) {
                                XUtil.tShort("选择提醒时间~");
                                XUtil.dismissShowDialog(materialDialog, false);
                                return;
                            }


                            if (bUpadte) {
                                data.setContent(content);
                                data.setModify_time(XUtil.getLongTime());
                                data.setRemind_time(remind_time);
                                data.setType(spRepeatChoose);
                                if (remind_time < XUtil.getLongTime() && data.getType() == 0) {
//                                    XUtil.tShort(notRing);
                                    data.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
                                } else {
                                    //提醒 未来一周以内的 and 重复提醒
                                    initRemind(data);
                                    if (data.getType() == 0) {
                                        XUtil.tLong(XUtil.diffTime(data.getRemind_time()) + "后提醒");
                                    }
                                }
                                rdDao.update(data);
                                refreshCell(data, false);
                            } else {
                                int tk_id = MySp.getDefaultTk();
                                if (taskData != null) {
                                    tk_id = taskData.getId();
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
                                    refreshCell(remindData, false);
                                } else {
                                    //提醒 未来一周以内的 and 重复提醒
                                    initRemind(remindData);
                                    if (remindData.getType() == 0) {
                                        XUtil.tLong(XUtil.diffTime(remindData.getRemind_time()) + "后提醒");
                                    }
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


    private void updateTkTime() {
//        if (taskData != null && remind_time != null) {
//            taskData.setModify_time(XUtil.getLongTime());
//            taskData.setRemind_time(remind_time);
//            taskDao.update(taskData);
//        }
    }


    @Override
    public void onRefresh() {
        if (onLoading) {
            return;
        }
        refresh();
    }

    private void refresh() {
        datas.clear();
        onLoading = true;
        swipeLayout.setRefreshing(true);
        getData(true);
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


    @Override
    public void onLoadMore() {
        getData(false);
    }


    //更新CELL
    private void refreshCell(RemindData data, boolean bDel) {
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
