package com.zncm.mxgtd.ft;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.ItemDetailsActivity;
import com.zncm.mxgtd.ui.TextActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.NotiHelper;
import com.zncm.mxgtd.utils.XUtil;
import com.zncm.mxgtd.view.HackyViewPager;
import com.zncm.mxgtd.view.PinchZoomTextView;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ItemsDetailsFt extends Fragment {
    private TaskData taskData;
    private DetailsData data;
    private int current = 0;
    private ArrayList<DetailsData> itemsList;
    private ViewPager mViewPager;
    private SamplePagerAdapter mAdapter;
    private int size;
    private int allSize;
    private ItemDetailsActivity ctx;
    private boolean isSingle = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ctx = (ItemDetailsActivity) getActivity();
        Serializable dataParam = ctx.getIntent().getSerializableExtra(Constant.KEY_PARAM_DATA);
        Serializable tmp = ctx.getIntent().getSerializableExtra("DetailsData");
        if (dataParam != null && dataParam instanceof TaskData) {
            taskData = (TaskData) dataParam;
        }
        size = ctx.getIntent().getExtras().getInt("size");
        current = ctx.getIntent().getExtras().getInt(Constant.KEY_CURRENT_POSITION);
        if (tmp != null && tmp instanceof DetailsData) {
            data = (DetailsData) tmp;
            size = 1;
            current = 1;
            isSingle = true;
        }
        itemsList = new ArrayList<>();
        if (taskData!=null){
            itemsList = (ArrayList<DetailsData>) DbUtils.getDetailsDatas(taskData.getId(), 0, size);
            allSize = DbUtils.getTkRows(taskData.getId());
        }else {
            itemsList.add(data);
            allSize =1;
        }

        mViewPager = new HackyViewPager(getActivity());
        mAdapter = new SamplePagerAdapter(itemsList);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                current = position;
                if (!isSingle && current == itemsList.size() - 1) {
                    List<DetailsData> items = (ArrayList<DetailsData>) DbUtils.getDetailsDatas(taskData.getId(), size, size + Constant.MAX_DB_QUERY);
                    if (XUtil.listNotNull(items)) {
                        itemsList.addAll(items);
                        mAdapter.setItems(itemsList);
                    }
                }else {
                    mAdapter.setItems(itemsList);
                }
                initTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setCurrentItem(current);
        initTitle();
        return mViewPager;
    }

    private void initTitle() {
        if (taskData!=null){
            ctx.toolbar.setTitle(taskData.getTitle() + "  " + (current + 1) + "/" + allSize);
        }
    }

    class SamplePagerAdapter extends PagerAdapter {
        private List<DetailsData> datas;


        public void setItems(List<DetailsData> items) {
            this.datas = items;
            notifyDataSetChanged();
        }

        public SamplePagerAdapter(ArrayList<DetailsData> datas) {
            this.datas = datas;
        }

        @Override
        public int getCount() {
            if (datas == null) {
                return 0;
            } else {
                return datas.size();
            }

        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            data = itemsList.get(position);
            ScrollView scrollView = initViews();
            container.addView(scrollView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return scrollView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }


    private ScrollView initViews() {
        ScrollView scrollView = new ScrollView(ctx);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        scrollView.setBackgroundColor(getResources().getColor(R.color.material_light_white));
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int paddingPx = XUtil.dip2px(15);
        linearLayout.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);


        if (!MySp.getShowSimple()) {
            TextView tvTime = new TextView(ctx);
            tvTime.setTextSize(XUtil.dip2px(4));
            tvTime.setTextColor(getResources().getColor(R.color.colorKeeptextg));
            if (data.getTime() != null) {
                tvTime.setText(XUtil.getDateYMDEHM(data.getTime()));
                tvTime.setPadding(0, 0, 0, paddingPx);
                linearLayout.addView(tvTime);
            }
        }


        PinchZoomTextView textView = new PinchZoomTextView(ctx);
//        textView.setTextSize(XUtil.dip2px(6));
        textView.setTextColor(getResources().getColor(R.color.material_light_black));
        textView.setTextSize(MySp.getFontSize());
        textView.setTextIsSelectable(true);
        if (data != null && XUtil.notEmptyOrNull(data.getContent())) {
            textView.setText(data.getContent());
        }
        linearLayout.addView(textView);


        scrollView.addView(linearLayout);
        return scrollView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        SubMenu sub = menu.addSubMenu("");
        sub.setIcon(XUtil.initIconWhite(Iconify.IconValue.md_more_vert));
        sub.add(0, 1, 0, "复制");
        sub.add(0, 2, 0, "分享");
        sub.add(0, 3, 0, "收藏");
        sub.add(0, 4, 0, "贴到通知栏");
        sub.add(0, 5, 0, "详情");
        sub.add(0, 6, 0, "删除");
        sub.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item == null || item.getTitle() == null) {
            return false;
        }
        DetailsData tmpData = itemsList.get(mViewPager.getCurrentItem());
        if (tmpData == null) {
            return false;
        }
        switch (item.getItemId()) {
            case 1:
                XUtil.copyText(ctx,tmpData.getContent());
                break;
            case 2:
                XUtil.sendTo(ctx, tmpData.getContent());
                break;
            case 3:
                DbUtils.like(tmpData.getContent(), EnumData.BusinessEnum.PROGRESS.getValue(), tmpData.getId(), tmpData.getTk_id());
                break;
            case 4:
                Intent intent = new Intent(ctx, ItemDetailsActivity.class);
                intent.putExtra(Constant.KEY_PARAM_DATA, taskData);
                intent.putExtra("size", size);
                intent.putExtra(Constant.KEY_CURRENT_POSITION, current);
                NotiHelper.noti(tmpData.getContent(), "", "", intent, false, false, new Random().nextInt());
                break;

            case 5:
                    showInfo(tmpData);
                break;

            case 6:
                DbUtils.delByDetailBEnum(tmpData.getBusiness_type(),tmpData.getId());
                XUtil.tShort("已删除~"+tmpData.getContent());
                tmpData.setContent("已删除~");
                itemsList.set(mViewPager.getCurrentItem(),tmpData);
                mAdapter.setItems(itemsList);
//                /**
//                 *删除后刷新主界面，更新--这个会导致界面定位有问题，故此不加
//                 */
//                EventBus.getDefault().post(new RefreshEvent(EnumData.RefreshEnum.MAIN_ITEM.getValue()));
                break;

        }
        return false;
    }

    private void showInfo(DetailsData data) {

        ScrollView scrollView = new ScrollView(ctx);
        scrollView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
        scrollView.setBackgroundColor(getResources().getColor(R.color.material_light_white));
        LinearLayout linearLayout = new LinearLayout(ctx);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int paddingPx = XUtil.dip2px(15);
        linearLayout.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);

        TextView tvTime = new TextView(ctx);
        tvTime.setTextSize(XUtil.dip2px(4));
        tvTime.setTextColor(getResources().getColor(R.color.colorKeeptextg));
        if (data.getTime() != null) {
            tvTime.setText(XUtil.getDateYMDEHM(data.getTime()));
            tvTime.setPadding(0, 0, 0, paddingPx);
            linearLayout.addView(tvTime);
        }

        TextView textView = new TextView(ctx);
        textView.setTextSize(XUtil.dip2px(6));
        textView.setTextColor(getResources().getColor(R.color.material_light_black));
        textView.setTextIsSelectable(true);
        if (data != null && XUtil.notEmptyOrNull(data.getContent())) {
            textView.setText(data.getContent());
        }
        linearLayout.addView(textView);


        scrollView.addView(linearLayout);


        XUtil.themeMaterialDialog(ctx)
                .customView(scrollView,true)
                .positiveText("知")
                .show();
    }

    public void onRefresh(DetailsData items) {
        itemsList.set(mViewPager.getCurrentItem(), items);
        mViewPager.getAdapter().notifyDataSetChanged();
    }


}
