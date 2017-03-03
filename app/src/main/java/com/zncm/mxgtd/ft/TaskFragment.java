package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.adapter.ProjectAdapter;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.TkAddActivity;
import com.zncm.mxgtd.ui.TkDetailsActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.loadmore.MxItemClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class TaskFragment extends BaseListFragment {
    private ProjectAdapter mAdapter;
    private Activity ctx;
    public List<TaskData> datas = null;
    private boolean onLoading = false;
    public ProjectData projectData;
    private boolean bFinish = false;
    private boolean inPj = false;
    private Boolean bTimeLine = false;
    private Boolean bOneDay = false;
    private Long oneDay = null;
    private int pj_id;
    private int status = 0;

    private ItemTouchHelper itemTouchHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
//        LinearLayout view = new LinearLayout(ctx);
//        view.setLayoutParams(new ViewGroup.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        TextView textView = new TextView(ctx);
//        textView.setText("笔记本时间轴");
//        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ctx, TkTimeLineActivity.class);
//                startActivity(intent);
//            }
//        });


//        ViewUtils.bindClickListenerOnViews(view,ctx, R.id.tvCalTask, R.id.tvTimeLine);

//        listView.setLayoutManager(new StaggeredGridLayoutManager(MySp.getSpanCount() + 1, StaggeredGridLayoutManager.VERTICAL));

        datas = new ArrayList<TaskData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(int position, final PjViewHolder holder) {
//                position = position - listView.getHeaderViewsCount();
                final TaskData tk = datas.get(position);
                holder.tvContent.setVisibility(View.GONE);
                holder.tvTag.setVisibility(View.GONE);


                if (XUtil.notEmptyOrNull(tk.getTitle())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    String title = tk.getTitle();

//                    if (tk.getStatus() == EnumData.StatusEnum.ON.getValue()) {
//                        title = "0" + title;
//                    } else if (tk.getStatus() == EnumData.StatusEnum.OFF.getValue()) {
//                        title = "1" + title;
//                    }


                    holder.tvTitle.setText(title);

                    if (title.length() < Constant.MAX_LEN) {
                        holder.tvTitle.setTextSize(Constant.FS_1);
                    } else {
                        holder.tvTitle.setTextSize(Constant.FS_2);
                    }

                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }


                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgremind));


                //2015年8月31日 不显示 笔记本组 进入笔记本可以查看所属笔记本组 简洁列表
                holder.tvTag.setVisibility(View.GONE);
//                if (tk.getPj_id() > 0 && !inPj) {
//                    holder.tvTag.setVisibility(View.VISIBLE);
//                    if (XUtil.notEmptyOrNull(tk.getTag())) {
//                        holder.tvTag.setText(tk.getTag());
//                    } else {
//                        holder.tvTag.setText("#" + tk.getPj_id());
//                    }
//                } else {
//                    holder.tvTag.setVisibility(View.GONE);
//                }
//                holder.tvTag.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        int pj_id = tk.getPj_id();
//                        ProjectData pj = projectDao.queryForId(pj_id);
//                        if (pj != null) {
//                            Intent newIntent = null;
//                            newIntent = new Intent(ctx, ProjectDetailsActivity.class);
//                            newIntent.putExtra(Constant.KEY_PARAM_DATA, pj);
//                            startActivity(newIntent);
//                        }
//                    }
//                });
                //2015年8月31日 简化列表显示
//                if (tk.getLevel() > 0) {
//                    int colorRes = 0;
//                    if (tk.getLevel() == EnumData.TaskLevelEnum.HIGH.getValue()) {
//                        colorRes = getResources().getColor(R.color.swipe_color1);
//                    } else if (tk.getLevel() == EnumData.TaskLevelEnum.MIDDLE.getValue()) {
//                        colorRes = getResources().getColor(R.color.swipe_color2);
//                    } else if (tk.getLevel() == EnumData.TaskLevelEnum.LOW.getValue()) {
//                        colorRes = getResources().getColor(R.color.swipe_color3);
//                    }
//                    if (colorRes != 0) {
//                        holder.tvTitle.setTextColor(colorRes);
//                    } else {
//                        holder.tvTitle.setTextColor(getResources().getColor(R.color.black));
//                    }
//                } else {
//                    holder.tvTitle.setTextColor(getResources().getColor(R.color.black));
//                }


//                if (bFinish) {
//                    holder.cbComplete.setChecked(true);
//                } else {
//                    holder.cbComplete.setChecked(false);
//                }


                holder.setClickListener(new MxItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {
                            final TaskData data = datas.get(position);
                            operate(data, position);
                        } else {
                            Intent newIntent = new Intent(ctx, TkDetailsActivity.class);
                            newIntent.putExtra(Constant.KEY_PARAM_DATA, datas.get(position));
                            newIntent.putExtra(Constant.KEY_PARAM_ID, 0);
                            startActivity(newIntent);
//                            Intent newIntent = new Intent(ctx, TaskDetailsActivity.class);
//                            newIntent.putExtra(Constant.KEY_PARAM_DATA, datas.get(position));
//                            newIntent.putExtra(Constant.KEY_PARAM_ID, 0);
//                            startActivity(newIntent);
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
//                if (curPosition >= 0 && curPosition < datas.size()) {
//                    Intent newIntent = null;
//                    newIntent = new Intent(ctx, TaskDetailsActivity.class);
//                    newIntent.putExtra(Constant.KEY_PARAM_DATA, datas.get(curPosition));
//                    newIntent.putExtra(Constant.KEY_PARAM_ID, 0);
//                    startActivity(newIntent);
//                }
//
//            }
//        });
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
//                final TaskData data = datas.get(i);
//
//
//                operate(data);
//                return true;
//            }
//        });


        if (getArguments() != null) {
            Bundle bundle = getArguments();


            Serializable dataParam = bundle.getSerializable(Constant.KEY_PARAM_DATA);
            if (dataParam != null && dataParam instanceof ProjectData) {
                projectData = (ProjectData) dataParam;
                if (projectData == null) {
                    int tk_id = ctx.getIntent().getIntExtra(Intent.EXTRA_UID, 0);
                    projectData = DbUtils.getPjById(tk_id);
                }
                if (projectData != null) {
                    inPj = true;
                    pj_id = projectData.getId();
                }
            }
            status = bundle.getInt(Constant.KEY_PARAM_TYPE);
            if (status == EnumData.StatusEnum.OFF.getValue()) {
                bFinish = true;
            } else if (status == EnumData.StatusEnum.ON.getValue()) {
                bFinish = false;
            }

            bTimeLine = bundle.getBoolean(Constant.KEY_PARAM_BOOLEAN);
            bOneDay = bundle.getBoolean(Constant.KEY_PARAM_BOOLEAN2);
            oneDay = bundle.getLong(Constant.KEY_PARAM_LONG);
        }
        getData(true);
        initAddBtn();


        return view;
    }


    public void operate(final TaskData data, final int pos) {
        final boolean isChecked = EnumData.StatusEnum.OFF.getValue() == data.getStatus();
        new MaterialDialog.Builder(ctx)
                .items(new String[]{isChecked ? "未完成" : "已完成", "序号", "删除", "编辑"})
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
                                taskDao.update(data);
                                refreshCell(data, false);
                                break;
                            case 1:

                                add1Dlg(data, pos);
                                break;

                            case 2:
                                new MaterialDialog.Builder(ctx)
                                        .title("删除笔记本!")
                                        .theme(Theme.LIGHT)
                                        .positiveText("删除")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                delTask(data);
                                            }


                                        })
                                        .show();
                                break;
                            case 3:
                                int pj_id = data.getPj_id();
                                Intent newIntent = null;
                                newIntent = new Intent(ctx, TkAddActivity.class);
//                                    newIntent.putExtra(Constant.KEY_PARAM_DATA, pj);
                                newIntent.putExtra(Constant.KEY_PARAM_DATA, data);
                                startActivity(newIntent);
//                                new MaterialDialog.Builder(ctx)
//                                        .title("克隆笔记本!")
//                                        .content("克隆笔记本将会连带克隆笔记本下所有的清单,确认进行克隆?")
//                                        .theme(Theme.LIGHT)
//                                        .positiveText("克隆")
//                                        .negativeText("取消")
//                                        .callback(new MaterialDialog.ButtonCallback() {
//                                            @Override
//                                            public void onPositive(MaterialDialog materialDialog) {
//                                                try {
//                                                    int tk_id = data.getId();
//                                                    QueryBuilder<CheckListData, Integer> builder2 = clDao.queryBuilder();
//                                                    builder2.where().notIn("status", EnumData.StatusEnum.DEL.getValue()).and().eq("tk_id", tk_id);
//                                                    List<CheckListData> list = clDao.query(builder2.prepare());
//                                                    //克隆 确认
//                                                    data.setModify_time(System.currentTimeMillis());
//                                                    data.setTime(System.currentTimeMillis());
//                                                    taskDao.create(data);
//                                                    if (XUtil.listNotNull(list)) {
//                                                        for (CheckListData cl : list) {
//                                                            cl.setTk_id(data.getId());
//                                                            cl.setModify_time(System.currentTimeMillis());
//                                                            cl.setTime(System.currentTimeMillis());
//                                                            cl.setStatus(EnumData.StatusEnum.ON.getValue());
//                                                            clDao.create(cl);
//                                                        }
//                                                    }
//                                                    EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.TASK.getValue()));
//                                                } catch (Exception e) {
//                                                }
//                                            }
//                                        })
//                                        .show();


                                break;
                            case 4:
//                                DbUtils.like(data.getTitle(), EnumData.BusinessEnum.TASK.getValue(), data.getId(), data.getId());
                                break;
                            case 5:
//                                int pj_id = data.getPj_id();
//                                ProjectData pj = projectDao.queryForId(pj_id);
//                                if (pj != null) {
//                                    Intent newIntent = null;
//                                    newIntent = new Intent(ctx, ProjectDetailsActivity.class);
//                                    newIntent.putExtra(Constant.KEY_PARAM_DATA, pj);
//                                    startActivity(newIntent);
//                                }
                                break;
                            default:
                                break;
                        }

                    }
                })
                .show();
    }

    private void delTask(TaskData data) {
        data.setStatus(EnumData.StatusEnum.DEL.getValue());
        data.setModify_time(System.currentTimeMillis());
        taskDao.update(data);
        refreshCell(data, true);
        DbUtils.finishCheckList(data);
    }


    void add1Dlg(final TaskData data, final int pos) {
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        final EditText editText = new EditText(ctx);
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setHint("序号");
        editText.setSingleLine();
        editText.setTextColor(getResources().getColor(R.color.black));
        editText.setText(data.getLevel() + "");
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);
        MaterialDialog md = new MaterialDialog.Builder(ctx)
                .customView(view, true)
                .positiveText("修改")
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
                            data.setLevel(Integer.parseInt(content));
                            taskDao.update(data);
                            refresh();
                            XUtil.dismissShowDialog(materialDialog, true);
                        } catch (Exception e) {
                            XUtil.tShort("序号只能是数字~");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNeutral(MaterialDialog materialDialog) {
                        XUtil.dismissShowDialog(materialDialog, true);
                    }

                })
                .autoDismiss(false)
                .build();
        md.setCancelable(false);
        md.show();

    }


    private void initAddBtn() {

        if (status == EnumData.StatusEnum.OFF.getValue()) {
            addButton.setVisibility(View.GONE);
        } else {
            addButton.setVisibility(View.VISIBLE);
        }


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = null;
                newIntent = new Intent(ctx, TkAddActivity.class);
                if (inPj) {
                    newIntent.putExtra(Constant.KEY_PARAM_ID, pj_id);
                }
                if (bOneDay) {
                    newIntent.putExtra(Constant.KEY_PARAM_LONG, oneDay);
                }

                startActivity(newIntent);
            }
        });
    }

    void initLevel(final TaskData data) {
        new MaterialDialog.Builder(ctx)
                .title("重要性")
                .items(new String[]{EnumData.TaskLevelEnum.NO.getStrName(), EnumData.TaskLevelEnum.LOW.getStrName(), EnumData.TaskLevelEnum.MIDDLE.getStrName(), EnumData.TaskLevelEnum.HIGH.getStrName()})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        int level = (which + 1);
                        data.setLevel(level);
                        data.setModify_time(XUtil.getLongTime());
                        taskDao.update(data);
                        refresh();
                    }
                })
                .show();

    }


    private void getData(boolean bFirst) {
        GetData getData = new GetData();
        getData.execute(bFirst);
    }

    class GetData extends AsyncTask<Boolean, Integer, Boolean> {

        protected Boolean doInBackground(Boolean... params) {

            boolean canLoadMore = true;
            try {


                QueryBuilder<TaskData, Integer> builder = taskDao.queryBuilder();


                if (status != 0) {
                    builder.where().eq("status", status).and().eq("pj_id", pj_id);
                } else {
                    builder.where().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OFF.getValue()).and().eq("pj_id", pj_id);
                }

//                if (ctx.getIntent().getExtras() != null) {
//                    bTimeLine = ctx.getIntent().getExtras().getBoolean(Constant.KEY_PARAM_BOOLEAN);
//                }
//                if (bOneDay) {
//                    builder.where().eq("status", status).and().between("start_time", oneDay, (oneDay + Constant.DAY));
//                    if (params[0]) {
//                        builder.orderBy("start_time", status == EnumData.StatusEnum.ON.getValue()).limit(Constant.MAX_DB_QUERY);
//                    } else {
//                        builder.orderBy("start_time", status == EnumData.StatusEnum.ON.getValue()).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
//                    }
//                } else {
//                    if (bTimeLine) {
////                    builder.where().eq("status", EnumData.StatusEnum.OFF.getValue());
//                        if (status == EnumData.StatusEnum.ON.getValue()) {
//                            builder.where().eq("status", status).and().ge("start_time", XUtil.getDayStart());
////                        builder.where().eq("status", status).and().between("start_time", XUtil.getDayStart(), (XUtil.getLongTime() + Constant.DAY * 30));
//                        }
//                        if (status == EnumData.StatusEnum.OFF.getValue()) {
//                            builder.where().eq("status", status);
//                        }
//                        if (status == EnumData.StatusEnum.OUTDATE.getValue()) {
//                            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().lt("start_time", XUtil.getDayStart());
//                        }
//
//                        if (status == EnumData.StatusEnum.OUTDATE.getValue()) {
//                            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().lt("start_time", XUtil.getDayStart());
//                        }
//                        if (status == EnumData.StatusEnum.NULL.getValue()) {
//                            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().isNull("start_time");
//                        }
//                        //OUTDATE
//
//                        if (params[0]) {
//                            builder.orderBy("start_time", status == EnumData.StatusEnum.ON.getValue()).limit(Constant.MAX_DB_QUERY);
//                        } else {
//                            builder.orderBy("start_time", status == EnumData.StatusEnum.ON.getValue()).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
//                        }
//                    } else {
//                        if (inPj) {
//                            builder.where().eq("status", status).and().eq("pj_id", pj_id);
//                        } else {
//                            builder.where().notIn("status", EnumData.StatusEnum.DEL.getValue());
//                            if (MySp.getShowFinish()) {
//                                builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
//                            }
//                        }
//                        if (params[0]) {
//                            builder.orderBy("status", true).orderBy("level", false).orderBy("modify_time", false).limit(Constant.MAX_DB_QUERY);
//                        } else {
//                            builder.orderBy("status", true).orderBy("level", false).orderBy("modify_time", false).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
//                        }
//                    }
//                }

                if (params[0]) {
                    builder.orderBy("status", true).orderBy("level", true).orderBy("modify_time", false).limit(Constant.MAX_DB_QUERY);
                } else {
                    builder.orderBy("status", true).orderBy("level", true).orderBy("modify_time", false).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                }
                List<TaskData> list = taskDao.query(builder.prepare());

                if (params[0]) {
                    datas = new ArrayList<TaskData>();
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
            if (datas.size() <= Constant.MAX_DB_QUERY) {
//                listView.setSelection(0);
            }

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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
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
    private void refreshCell(TaskData data, boolean bDel) {
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
