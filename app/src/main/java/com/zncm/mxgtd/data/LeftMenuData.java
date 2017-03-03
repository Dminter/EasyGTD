package com.zncm.mxgtd.data;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class LeftMenuData extends BaseData implements Serializable {
    private static final long serialVersionUID = 8838725426885988959L;
    private Drawable icon;
    private String title;


    public LeftMenuData() {
    }

    public LeftMenuData(String title) {
        this.title = title;
    }

    public LeftMenuData(Drawable icon, String title) {
        this.icon = icon;
        this.title = title;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
