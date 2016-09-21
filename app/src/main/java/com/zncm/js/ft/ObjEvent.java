package com.zncm.js.ft;

import com.zncm.js.data.BaseData;

/**
 * Created by MX on 2014/8/21.
 */
public class ObjEvent {

    public int type;//1 project 2 task
    public int type2;//默认分组，日记本，xxx
    public BaseData obj;

    public ObjEvent() {
    }

    public ObjEvent(int type, int type2, BaseData obj) {
        this.type = type;
        this.type2 = type2;
        this.obj = obj;
    }

}
