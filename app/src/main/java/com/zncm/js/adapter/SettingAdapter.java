package com.zncm.js.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zncm.js.R;
import com.zncm.js.data.BaseData;
import com.zncm.js.view.loadmore.LoadMoreRecyclerView;
import com.zncm.js.view.loadmore.MxItemClickListener;

import java.util.List;

public abstract class SettingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<? extends BaseData> items;
    private Activity ctx;

    public SettingAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<? extends BaseData> items,LoadMoreRecyclerView recyclerView) {
        this.items = items;
        notifyDataSetChanged();
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LoadMoreRecyclerView.TYPE_STAGGER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_setting, parent, false);
            return new SettingViewHolder(view);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder2, int position) {
        SettingViewHolder holder = (SettingViewHolder) holder2;
        setData(position, holder);

    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class SettingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MxItemClickListener clickListener;
        public TextView tvTitle;
        public TextView tvSummary;
        public TextView tvStatus;
        private View view;

        public SettingViewHolder(View convertView) {
            super(convertView);
            view = convertView;
            tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            tvSummary = (TextView) convertView.findViewById(R.id.tvSummary);
            tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        public void setClickListener(MxItemClickListener itemClickListener) {
            this.clickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            clickListener.onClick(view, getPosition(), false);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onClick(view, getPosition(), true);
            return true;
        }
    }



    public abstract void setData(int position,SettingViewHolder holder);


}