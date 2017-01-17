package com.zncm.mxgtd.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.zncm.mxgtd.R;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.utils.XUtil;

public class WrapContentHeightViewPager extends ViewPager {
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        Context context = MyApplication.getInstance().ctx;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }

        int height = XUtil.getDeviceHeight() - 240;
//        for (int i = 0; i < getChildCount(); i++) {
//            View child = getChildAt(i);
//            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
//            int h = child.getMeasuredHeight();
//            if (h > height) height = h;
//        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


//    /**
//     * 设置viewpager是否可以滚动
//     *
//     * @param enable
//     */
//    public void setScrollable(boolean enable) {
//        scrollable = enable;
//    }

//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event) {
//        return false;
//    }
}