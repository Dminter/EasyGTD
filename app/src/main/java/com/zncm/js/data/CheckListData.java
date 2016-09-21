package com.zncm.js.data;

import com.j256.ormlite.field.DatabaseField;
import com.zncm.js.utils.XUtil;

import java.io.Serializable;

public class CheckListData extends BaseData implements Serializable {

    private static final long serialVersionUID = 8838725426885988959L;
    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField
    private String title;
    @DatabaseField
    private int status;//状态 1-进行中 2-完成 3-删除
    @DatabaseField
    private Long time;
    @DatabaseField
    private Long modify_time;
    @DatabaseField
    private Long finish_time;
    @DatabaseField
    private int level;//优先级 1 无 2 低 3中 4高
    @DatabaseField
    private int tk_id;//日记本 id


    public CheckListData() {
    }


    public CheckListData(String title, int status, int level, int tk_id) {
        this.title = title;
        this.status = status;
        this.time = XUtil.getLongTime();
        this.modify_time = XUtil.getLongTime();
        this.level = level;
        this.tk_id = tk_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public Long getFinish_time() {
        return finish_time;
    }

    public void setFinish_time(Long finish_time) {
        this.finish_time = finish_time;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTk_id() {
        return tk_id;
    }

    public void setTk_id(int tk_id) {
        this.tk_id = tk_id;
    }
}
