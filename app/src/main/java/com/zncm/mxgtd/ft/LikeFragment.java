package com.zncm.mxgtd.ft;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.j256.ormlite.stmt.QueryBuilder;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.adapter.ProjectAdapter;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.LikeData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.ItemDetailsActivity;
import com.zncm.mxgtd.ui.ProjectDetailsActivity;
import com.zncm.mxgtd.ui.TextActivity;
import com.zncm.mxgtd.ui.TkDetailsActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.loadmore.MxItemClickListener;

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
                holder.cbCheck.setVisibility(View.GONE);
                holder.tvTag.setVisibility(View.GONE);
                holder.tvTitle.setTextSize(24);
                if (!MySp.getShowSimple()) {
                    if (likeData.getTime() != null) {
                        holder.tvTime.setText(XUtil.getDateYMDEHM(likeData.getTime()));
                        holder.tvTime.setVisibility(View.VISIBLE);
                    } else {
                        holder.tvTime.setVisibility(View.GONE);
                    }
                }

                holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorSearch));
                holder.setClickListener(new MxItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        if (isLongClick) {


                            LikeData likeData = datas.get(position);
                            if (likeData == null) {
                                return;
                            }
                            operate(position, likeData);

                        } else {
                            final LikeData data = datas.get(position);

                            DetailsData tmp = new DetailsData();
                            tmp.setTime(data.getTime());
                            tmp.setBusiness_type(EnumData.DetailBEnum.like.getValue());
                            tmp.setContent(data.getContent());
                            tmp.setId(data.getId());

                            if (data != null && XUtil.notEmptyOrNull(data.getContent())) {
//                                Intent intent = new Intent(ctx, TextActivity.class);
//                                intent.putExtra(Constant.KEY_PARAM_DATA, tmp);
//                                intent.putExtra("text", data.getContent());
//                                startActivity(intent);


                                Intent intent = new Intent(ctx, ItemDetailsActivity.class);
                                intent.putExtra("DetailsData", tmp);
                                intent.putExtra("size", 1);
                                intent.putExtra(Constant.KEY_CURRENT_POSITION, 0);
                                startActivity(intent);



                            }


                        }

                    }
                });
            }


        };
        XUtil.listViewRandomAnimation(listView, mAdapter);
        getData(true);
        return view;
    }

    public void operate(final int position, final LikeData likeData) {
        XUtil.themeMaterialDialog(ctx)
                .items(new String[]{"移除收藏", "复制", "笔记本"})
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                DbUtils.like(likeData.getContent(), likeData.getBusiness_type(), likeData.getBusiness_id(), likeData.getParent_id());
                                likeData.setStatus(EnumData.StatusEnum.DEL.getValue());
                                likeDao.update(likeData);
                                refreshCell(likeData, true);
                                break;
                            case 1:
                                XUtil.copyText(ctx, likeData.getContent());
                                break;
                            case 2:
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
                                break;


                            default:
                                break;
                        }
                    }
                })
                .show();
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
