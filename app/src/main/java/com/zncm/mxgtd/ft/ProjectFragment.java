package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.adapter.ProjectAdapter;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.PjAddActivity;
import com.zncm.mxgtd.ui.ProjectDetailsActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.loadmore.MxItemClickListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ProjectFragment extends BaseListFragment {
    private ProjectAdapter mAdapter;
    private Activity ctx;
    private List<ProjectData> datas = null;
    private boolean onLoading = false;
    private boolean bFinish = false;
    private int status = EnumData.StatusEnum.ON.getValue();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        datas = new ArrayList<ProjectData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(int position, PjViewHolder holder) {
                final ProjectData pj = datas.get(position);
                holder.tvContent.setVisibility(View.GONE);
                holder.tvTag.setVisibility(View.GONE);
                if (XUtil.notEmptyOrNull(pj.getTitle())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    holder.tvTitle.setText(pj.getTitle());
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }
                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorBgremind));
                holder.setClickListener(new MxItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {
                            final ProjectData data = datas.get(position);
                            operate(data);
                        } else {
                            Intent newIntent = null;
                            newIntent = new Intent(ctx, ProjectDetailsActivity.class);
                            newIntent.putExtra(Constant.KEY_PARAM_DATA, datas.get(position));
                            startActivity(newIntent);
                        }

                    }
                });

            }

        };

        XUtil.listViewRandomAnimation(listView, mAdapter);
        if (getArguments() != null) {
            Bundle bundle = getArguments();
            status = bundle.getInt(Constant.KEY_PARAM_TYPE);
            if (status == EnumData.StatusEnum.OFF.getValue()) {
                bFinish = true;
            } else if (status == EnumData.StatusEnum.OFF.getValue()) {
                bFinish = false;
            }
        }
        getData(true);
        initAddBtn();
        return view;
    }

    public void operate(final ProjectData data) {
        final boolean isChecked = EnumData.StatusEnum.OFF.getValue() == data.getStatus();
        XUtil.themeMaterialDialog(ctx)
                .items(new String[]{isChecked ? "重启" : "完成", "复制", "删除", "克隆", "收藏"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {

                            case 0:
                                final int status = EnumData.StatusEnum.OFF.getValue();
                                if (isChecked) {
                                    data.setStatus(EnumData.StatusEnum.ON.getValue());
                                } else {
                                    data.setStatus(EnumData.StatusEnum.OFF.getValue());
                                }
                                new MaterialDialog.Builder(ctx)
                                        .content(isChecked ? "重启笔记本组？" : "完成笔记本组？")
                                        .positiveText("确定")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                try {
                                                    data.setModify_time(System.currentTimeMillis());
                                                    projectDao.update(data);
                                                    if (taskDao == null) {
                                                        taskDao = getHelper().getTkDao();
                                                    }
                                                    UpdateBuilder<TaskData, Integer> updateBuilder = taskDao.updateBuilder();
                                                    updateBuilder.where().eq("pj_id", data.getId()).and().eq("status", EnumData.StatusEnum.ON.getValue());
                                                    if (EnumData.StatusEnum.OFF.getValue() == status) {
                                                        updateBuilder.updateColumnValue("status", status);
                                                    }
                                                    updateBuilder.update();
                                                    refresh();
                                                } catch (Exception e) {
                                                }
                                            }

                                            @Override
                                            public void onNegative(MaterialDialog dialog) {
                                                super.onNegative(dialog);
                                                refresh();
                                                return;
                                            }
                                        })
                                        .show();
                                break;

                            case 1:
                                XUtil.copyText(ctx, data.getTitle());
                                break;
                            case 2:
                                new MaterialDialog.Builder(ctx)
                                        .title("删除笔记本组!")
                                        .content("删除笔记本组将会连带删除笔记本组下所有的笔记本,确认删除?")
                                        .theme(Theme.LIGHT)
                                        .positiveText("删除")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                delMath(data);
                                            }
                                        })
                                        .show();
                                break;
                            case 3:
                                new MaterialDialog.Builder(ctx)
                                        .title("克隆笔记本组!")
                                        .content("克隆笔记本组将会连带克隆笔记本组下所有的笔记本,确认进行克隆?")
                                        .theme(Theme.LIGHT)
                                        .positiveText("克隆")
                                        .negativeText("取消")
                                        .callback(new MaterialDialog.ButtonCallback() {
                                            @Override
                                            public void onPositive(MaterialDialog materialDialog) {
                                                clonePj(data);
                                            }
                                        })
                                        .show();
                                break;
                            case 4:
                                DbUtils.like(data.getTitle(), EnumData.BusinessEnum.PROJECT.getValue(), data.getId(), data.getId());
                                break;
                            default:
                                break;
                        }

                    }
                })
                .show();
    }

    public void clonePj(ProjectData data) {
        try {
            int pj_id = data.getId();
            QueryBuilder<TaskData, Integer> builder2 = taskDao.queryBuilder();
            builder2.where().notIn("status", EnumData.StatusEnum.DEL.getValue()).and().eq("pj_id", pj_id);
            List<TaskData> list = taskDao.query(builder2.prepare());
            //克隆 确认
            data.setModify_time(System.currentTimeMillis());
            data.setTime(System.currentTimeMillis());
            projectDao.create(data);
            if (XUtil.listNotNull(list)) {
                for (TaskData tk : list) {
                    tk.setPj_id(data.getId());
                    tk.setModify_time(System.currentTimeMillis());
                    tk.setTime(System.currentTimeMillis());
                    tk.setStatus(EnumData.StatusEnum.ON.getValue());
                    taskDao.create(tk);
                }
            }
            EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.PROJECT.getValue()));
        } catch (Exception e) {
        }
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
                Intent newIntent = new Intent(ctx, PjAddActivity.class);
                startActivity(newIntent);
            }
        });
    }

    private void delMath(final ProjectData data) {
        int x = new Random().nextInt(20);
        int y = new Random().nextInt(20);
        final int result = x + y;
        final EditText editText = new EditText(ctx);
        LinearLayout view = new LinearLayout(ctx);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        XUtil.autoKeyBoardShow(editText);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        editText.setHint("输入结果...");
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setTextColor(getResources().getColor(R.color.material_light_black));
        editText.setBackgroundDrawable(new BitmapDrawable());
        view.addView(editText);


        XUtil.themeMaterialDialog(ctx)
                .customView(view, true)
                .title("删除确认(" + x + "+" + y + " =?)")
                .positiveText("确定")
                .negativeText("取消")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog materialDialog) {
                        String res = editText.getText().toString();
                        if (XUtil.notEmptyOrNull(res) && res.equals(String.valueOf(result))) {
                            try {
                                data.setStatus(EnumData.StatusEnum.DEL.getValue());
                                data.setModify_time(System.currentTimeMillis());
                                projectDao.update(data);
                                if (taskDao == null) {
                                    taskDao = getHelper().getTkDao();
                                }
                                UpdateBuilder<TaskData, Integer> updateBuilder = taskDao.updateBuilder();
                                updateBuilder.where().eq("pj_id", data.getId());
                                updateBuilder.updateColumnValue("status", EnumData.StatusEnum.DEL.getValue());
                                updateBuilder.update();
                            } catch (Exception e) {
                            }
                            refreshCell(data);
                        } else {
                            XUtil.tShort("回答错误~");
                        }
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
                QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
                if (status == -2) {
                    builder.where().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OFF.getValue());
                    builder.orderBy("status", true);
                } else {
                    builder.where().eq("status", status);
                }
                if (params[0]) {
                    builder.orderBy("modify_time", false).limit(Constant.MAX_DB_QUERY);
                } else {
                    builder.orderBy("modify_time", false).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                }
                List<ProjectData> list = projectDao.query(builder.prepare());

                if (params[0]) {
                    datas = new ArrayList<ProjectData>();
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
    private void refreshCell(ProjectData data) {
        datas.remove(data);
        mAdapter.setItems(datas, listView);
    }
}
