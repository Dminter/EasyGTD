package com.zncm.mxgtd.ft;

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
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.adapter.SettingAdapter;
import com.zncm.mxgtd.data.CheckListData;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.SettingData;
import com.zncm.mxgtd.data.SpConstant;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.SearchActivity;
import com.zncm.mxgtd.ui.SettingNew;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MyPath;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.loadmore.MxItemClickListener;

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


public class MyFragment extends BaseListFragment {
    private SettingAdapter mAdapter;
    private Activity ctx;
    private List<SettingData> datas = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        ctx = (Activity) getActivity();
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
                                                        if (ITEM == 1) {
                                                            startActivity(new Intent(ctx, SearchActivity.class));
                                                        } else if (ITEM == 2) {
                                                            startActivity(new Intent(ctx, SettingNew.class));
                                                        }

                                                    }


                                                }

                                            }
                                        }

                );

            }
        };

        listView.setAdapter(mAdapter);
        getData();
        return view;
    }


    private void getData() {
        datas = new ArrayList<SettingData>();
        datas.add(new SettingData(1, "搜索"));
        datas.add(new SettingData(2, "设置"));
        mAdapter.setItems(datas, listView);
    }


    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {
    }


}
