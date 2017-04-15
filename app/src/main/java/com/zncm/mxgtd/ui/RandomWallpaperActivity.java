package com.zncm.mxgtd.ui;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import com.github.lzyzsd.randomcolor.RandomColor;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.utils.ColorGenerator;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.XUtil;


public class RandomWallpaperActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar.setVisibility(View.GONE);
        initData();
    }

    private void initData() {

        ProgressData randomData = DbUtils.getProgressByTkId(MySp.getDefaultTk());
        if (randomData == null) {
            randomData = DbUtils.getProgressByTkId(MySp.getDefaultTk());
        }


        if (randomData != null) {
            String show = randomData.getContent();
            if (!XUtil.notEmptyOrNull(show)) {
                show = "美好一天~";
            }
            int maxLen = 40;
            if (show.length() > maxLen ){
                show = show.substring(0, maxLen);
            }
            XUtil.debug("info:" + show);
            try {
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
                Bitmap bitmap = getNewBitMap(show, XUtil.dip2px(200), 0);
                wallpaperManager.setBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        finish();

    }

    public Bitmap getNewBitMap(String text, int height, int width) {
        Bitmap newBitmap = Bitmap.createBitmap(XUtil.getDeviceWidth(), XUtil.getDeviceHeight(),
                Bitmap.Config.ARGB_8888);
        XUtil.debug("===>>>" + XUtil.getDeviceWidth() + " --- " + XUtil.getDeviceHeight());
//        RandomColor randomColor = new RandomColor();
//        int color = randomColor.randomColor();
//        newBitmap.eraseColor(color);
        newBitmap.eraseColor(ColorGenerator.MATERIAL.getRandomColor());
//        Bitmap newBitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(newBitmap, 0, 0, null);
        TextPaint textPaint = new TextPaint();
        textPaint.setAntiAlias(true);
        float textSize = 40;
        int textLen = text.length();
        textPaint.setTextSize(textSize);
        textPaint.setColor(getResources().getColor(R.color.white));
        StaticLayout sl = new StaticLayout(text, textPaint, newBitmap.getWidth() / 3, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        canvas.translate(XUtil.dip2px(50), XUtil.dip2px(20));
        sl.draw(canvas);
        return newBitmap;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_quickadd;
    }

}
