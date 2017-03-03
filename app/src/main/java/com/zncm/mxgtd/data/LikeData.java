package com.zncm.mxgtd.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.mxgtd.utils.XUtil;

import java.io.Serializable;

public class LikeData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String content;
    @DatabaseField
    private int business_type;//业务类型 1-笔记本组 2-笔记本 3-笔记本回复 4-清单
    @DatabaseField
    private int status;//状态 1-正常 3-删除
    @DatabaseField
    private Long time;
    @DatabaseField
    private Long modify_time;
    @DatabaseField
    private int business_id;//业务 id
    @DatabaseField
    private int parent_id;//父级 id


    public LikeData() {
    }


    public LikeData(String content, int business_type, int business_id, int parent_id) {
        this.content = content;
        this.business_id = business_id;
        this.parent_id = parent_id;
        this.business_type = business_type;
        this.status = EnumData.StatusEnum.ON.getValue();
        this.time = XUtil.getLongTime();
        this.modify_time = XUtil.getLongTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(int business_id) {
        this.business_id = business_id;
    }

    public int getBusiness_type() {
        return business_type;
    }

    public void setBusiness_type(int business_type) {
        this.business_type = business_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getModify_time() {
        return modify_time;
    }

    public void setModify_time(Long modify_time) {
        this.modify_time = modify_time;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
}
