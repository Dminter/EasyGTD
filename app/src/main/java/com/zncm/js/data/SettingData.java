package com.zncm.js.data;

import java.io.Serializable;

public class SettingData extends BaseData implements Serializable {
    private static final long serialVersionUID = 8838725426885988959L;
    private int id;
    private int parent_id;
    private int type;
    private CharSequence title;
    private CharSequence summary;
    private CharSequence status;
    private BaseData data;
    private Long time;

    public SettingData() {
    }

    public SettingData(int id, CharSequence title) {
        this.id = id;
        this.title = title;
    }

    public SettingData(int id, CharSequence title, CharSequence summary) {
        this.id = id;
        this.title = title;
        this.summary = summary;
    }

    public SettingData(int id, CharSequence title, CharSequence summary, CharSequence status) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.status = status;
    }


    public SettingData(int id, int type, BaseData data, CharSequence title, CharSequence summary, CharSequence status) {
        this.id = id;
        this.type = type;
        this.data = data;
        this.title = title;
        this.summary = summary;
        this.status = status;
    }

    public SettingData(int id,int parent_id, int type, BaseData data, CharSequence title, CharSequence summary, CharSequence status, Long time) {
        this.id = id;
        this.parent_id = parent_id;
        this.type = type;
        this.data = data;
        this.title = title;
        this.summary = summary;
        this.status = status;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public CharSequence getSummary() {
        return summary;
    }

    public void setSummary(CharSequence summary) {
        this.summary = summary;
    }

    public CharSequence getStatus() {
        return status;
    }

    public void setStatus(CharSequence status) {
        this.status = status;
    }

    public BaseData getData() {
        return data;
    }

    public void setData(BaseData data) {
        this.data = data;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
}
