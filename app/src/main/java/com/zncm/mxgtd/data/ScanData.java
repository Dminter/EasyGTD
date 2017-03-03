package com.zncm.mxgtd.data;

import java.io.Serializable;

public class ScanData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    private int id;
    private int parent_id;
    private int business_type;
    private String content;
    private Long time;


    public ScanData() {
    }

    public ScanData(int id, int business_type, String content, Long time) {
        this.id = id;
        this.business_type = business_type;
        this.content = content;
        this.time = time;
    }
    public ScanData(int id,int parent_id, int business_type, String content, Long time) {
        this.id = id;
        this.parent_id = parent_id;
        this.business_type = business_type;
        this.content = content;
        this.time = time;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
