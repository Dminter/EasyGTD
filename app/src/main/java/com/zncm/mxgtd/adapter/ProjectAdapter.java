package com.zncm.mxgtd.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.BaseData;
import com.zncm.mxgtd.view.loadmore.LoadMoreRecyclerView;
import com.zncm.mxgtd.view.loadmore.MxItemClickListener;

import java.util.List;

public abstract class ProjectAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<? extends BaseData> items;
    private Activity ctx;

    public ProjectAdapter(Activity ctx) {
        this.ctx = ctx;
    }

    public void setItems(List<? extends BaseData> items, LoadMoreRecyclerView recyclerView) {
        this.items = items;
        notifyDataSetChanged();
        /**
         * java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling
         */
        try {
            recyclerView.getAdapter().notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LoadMoreRecyclerView.TYPE_STAGGER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cell_project, parent, false);
            return new PjViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder2, int position) {
        PjViewHolder holder = (PjViewHolder) holder2;
        setData(position, holder);
    }

    @Override
    public int getItemCount() {
        if (items != null) {
            return items.size();
        }
        return 0;
    }

    public class PjViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MxItemClickListener clickListener;
        public CardView cardView;
        public TextView tvContent;
        public TextView tvTag;
        public TextView tvTitle;
        public TextView tvTime;
        public CheckBox cbCheck;
        private View view;

        public PjViewHolder(View convertView) {
            super(convertView);
            view = convertView;
            cardView = (CardView) convertView
                    .findViewById(R.id.cardView);
            tvContent = (TextView) convertView
                    .findViewById(R.id.tvContent);
            tvTag = (TextView) convertView
                    .findViewById(R.id.tvTag);
            tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            cbCheck = (CheckBox) convertView.findViewById(R.id.cbCheck);
            tvTime = (TextView) convertView.findViewById(R.id.tvTime);
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

    public abstract void setData(int position, ProjectAdapter.PjViewHolder holder);

}