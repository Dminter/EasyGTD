package com.zncm.js.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.js.adapter.ProjectAdapter;
import com.zncm.js.data.Constant;
import com.zncm.js.data.EnumData;
import com.zncm.js.data.LikeData;
import com.zncm.js.data.ProjectData;
import com.zncm.js.data.TaskData;
import com.zncm.js.ui.ProjectDetailsActivity;
import com.zncm.js.ui.TkDetailsActivity;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.XUtil;
import com.zncm.js.view.loadmore.MxItemClickListener;

import java.util.ArrayList;
import java.util.List;


public class LikeFragment extends BaseListFragment {
    private ProjectAdapter mAdapter;
    private Activity ctx;
    private List<LikeData> datas = null;
    private boolean onLoading = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
        addButton.setVisibility(View.GONE);
        datas = new ArrayList<LikeData>();
        mAdapter = new ProjectAdapter(ctx) {
            @Override
            public void setData(final int position, PjViewHolder holder) {
                final LikeData likeData = datas.get(position);
                if (XUtil.notEmptyOrNull(likeData.getContent())) {
                    holder.tvTitle.setVisibility(View.VISIBLE);
                    String title = likeData.getContent();
                    holder.tvTitle.setText(title);
                } else {
                    holder.tvTitle.setVisibility(View.GONE);
                }
                holder.tvContent.setVisibility(View.GONE);


                holder.setClickListener(new MxItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {
                            final LikeData data = datas.get(position);
                            operate(position, data);
                        } else {
                            LikeData likeData = datas.get(position);
                            if (likeData == null) {
                                return;
                            }
                            if (likeData.getBusiness_type() == EnumData.BusinessEnum.PROJECT.getValue()) {
                                ProjectData tmp = projectDao.queryForId(likeData.getParent_id());
                                if (tmp != null) {
                                    Intent newIntent = null;
                                    newIntent = new Intent(ctx, ProjectDetailsActivity.class);
                                    newIntent.putExtra(Constant.KEY_PARAM_DATA, tmp);
                                    startActivity(newIntent);
                                }
                            } else if (likeData.getBusiness_type() == EnumData.BusinessEnum.CHECK_LIST.getValue() || likeData.getBusiness_type() == EnumData.BusinessEnum.PROGRESS.getValue() || likeData.getBusiness_type() == EnumData.BusinessEnum.TASK.getValue()) {
                                TaskData tmp = taskDao.queryForId(likeData.getParent_id());
                                if (tmp != null) {
                                    Intent newIntent = null;
                                    newIntent = new Intent(ctx, TkDetailsActivity.class);
                                    newIntent.putExtra(Constant.KEY_PARAM_DATA, tmp);
                                    startActivity(newIntent);
                                }
                            }
                        }

                    }
                });
            }


        };
        XUtil.listViewRandomAnimation(listView, mAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//                                            @SuppressWarnings({"rawtypes", "unchecked"})
//                                            @Override
//                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                                int curPosition = position - listView.getHeaderViewsCount();
//                                                LikeData likeData = datas.get(curPosition);
//                                                if (likeData == null) {
//                                                    return;
//                                                }
//                                                if (likeData.getBusiness_type() == EnumData.BusinessEnum.PROJECT.getValue()) {
//                                                    ProjectData tmp = projectDao.queryForId(likeData.getParent_id());
//                                                    if (tmp != null) {
//                                                        Intent newIntent = null;
//                                                        newIntent = new Intent(ctx, ProjectDetailsActivity.class);
//                                                        newIntent.putExtra(Constant.KEY_PARAM_DATA, tmp);
//                                                        startActivity(newIntent);
//                                                    }
//                                                } else if (likeData.getBusiness_type() == EnumData.BusinessEnum.CHECK_LIST.getValue() || likeData.getBusiness_type() == EnumData.BusinessEnum.PROGRESS.getValue() || likeData.getBusiness_type() == EnumData.BusinessEnum.TASK.getValue()) {
//                                                    TaskData tmp = taskDao.queryForId(likeData.getParent_id());
//                                                    if (tmp != null) {
//                                                        Intent newIntent = null;
//                                                        newIntent = new Intent(ctx, TaskDetailsActivity.class);
//                                                        newIntent.putExtra(Constant.KEY_PARAM_DATA, tmp);
//                                                        if (likeData.getBusiness_type() == EnumData.BusinessEnum.CHECK_LIST.getValue()) {
//                                                            newIntent.putExtra(Constant.KEY_PARAM_ID, 1);
//                                                        } else {
//                                                            newIntent.putExtra(Constant.KEY_PARAM_ID, 0);
//                                                        }
//                                                        startActivity(newIntent);
//                                                    }
//                                                }
//
//                                            }
//                                        }
//
//        );
//
//
//        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
//                final LikeData data = datas.get(position);
//                operate(position, data);
//                return true;
//            }
//        });

        getData(true);

        return view;
    }

    public void operate(final int position, final LikeData data) {
        new MaterialDialog.Builder(ctx)
                .items(new String[]{"移除收藏", "复制", "查看"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                DbUtils.like(data.getContent(), data.getBusiness_type(), data.getBusiness_id(), data.getParent_id());
                                data.setStatus(EnumData.StatusEnum.DEL.getValue());
                                likeDao.update(data);
                                refreshCell(data, true);
                                break;
                            case 1:
                                XUtil.copyText(ctx, data.getContent());
                                break;
                            case 2:
                                scan(position);
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
    }

    public void scan(int i) {
//        final ArrayList<ScanData> tmp = new ArrayList<ScanData>();
//        if (XUtil.listNotNull(datas)) {
//            for (LikeData likeData : datas) {
//                tmp.add(new ScanData(likeData.getBusiness_id(), likeData.getParent_id(), likeData.getBusiness_type(), likeData.getContent(), likeData.getTime()));
//            }
//        } else {
//            XUtil.tShort("无可查看的内容!");
//            return;
//        }
//        Intent intent = new Intent(ctx, ShowActivity.class);
//        intent.putExtra("current", i);
//        intent.putExtra("type", EnumData.BusinessEnum.LIKE.getValue());
//        ArrayList list = new ArrayList();
//        list.add(tmp);
//        intent.putParcelableArrayListExtra(Constant.KEY_PARAM_LIST, list);
//        startActivity(intent);
    }

    //更新CELL
    private void refreshCell(LikeData data, boolean bDel) {
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

    private void getData(boolean bFirst) {
        GetData getData = new GetData();
        getData.execute(bFirst);
    }

    class GetData extends AsyncTask<Boolean, Integer, Boolean> {

        protected Boolean doInBackground(Boolean... params) {

            boolean canLoadMore = true;
            try {
                QueryBuilder<LikeData, Integer> builder = likeDao.queryBuilder();
                builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
                if (params[0]) {
                    builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
                } else {
                    builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY).offset((long) datas.size());
                }
                List<LikeData> list = likeDao.query(builder.prepare());
                if (params[0]) {
                    datas = new ArrayList<LikeData>();
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


}
