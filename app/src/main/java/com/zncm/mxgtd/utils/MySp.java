package com.zncm.mxgtd.utils;

import android.content.SharedPreferences;

import com.alibaba.fastjson.JSON;
import com.zncm.mxgtd.data.BaseData;

public class MySp extends MySharedPreferences {
    private static final String STATE_PREFERENCE = "state_preference";
    private static SharedPreferences sharedPreferences;

    enum Key {
        app_version_code,
        default_tk,
        font_ratio,
        default_pj,
        clipboard_tk,
        last_tab,
        album_numcolumns,
        simple_model,
        theme,
        pwd,
        show_lunar,
        sys_tip,
        show_finish,
        clipboard_listen,
        show_simple,
        today_date,
        list_animation,
        vp_animation,
        font_size,
        is_short_cut,
        is_big_ring,
        span_count,
        is_night,
        is_auto_night,
        is_lock,
    }


    public static <T> void put(String key, T value) {
        if (value == null) {
            return;
        }
        String realStr = "";
        if (value instanceof BaseData) {
            realStr = value.toString();
        } else {
            realStr = JSON.toJSONString(value);
        }
        putString(getSharedPreferences(), key, realStr);
    }

    public static <T> T get(String key, Class<T> cls, T defaultValue) {
        String stoStr = getString(getSharedPreferences(), key, "");
        if (!XUtil.notEmptyOrNull(stoStr)) {
            return defaultValue;
        }
        try {
            String realStr = new String(stoStr);
            T data = JSON.parseObject(realStr, cls);
            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static SharedPreferences getSharedPreferences() {
        if (sharedPreferences == null)
            sharedPreferences = getPreferences(STATE_PREFERENCE);
        return sharedPreferences;
    }


    public static void setLastTab(Integer last_tab) {
        putInt(getSharedPreferences(), Key.last_tab.toString(), last_tab);
    }

    public static Integer getLastTab() {
        return getInt(getSharedPreferences(), Key.last_tab.toString(), 0);
    }

    public static void setAlbumNumColumns(Integer album_numcolumns) {
        putInt(getSharedPreferences(), Key.album_numcolumns.toString(), album_numcolumns);
    }

    public static Integer getAlbumNumColumns() {
        return getInt(getSharedPreferences(), Key.album_numcolumns.toString(), 1);
    }

    public static void setSpanCount(Integer span_count) {
        putInt(getSharedPreferences(), Key.span_count.toString(), span_count);
    }

    public static Integer getSpanCount() {
        return getInt(getSharedPreferences(), Key.span_count.toString(), 2);
    }

    public static void setDefaultTk(Integer default_tk) {
        putInt(getSharedPreferences(), Key.default_tk.toString(), default_tk);
    }

    public static Integer getDefaultTk() {
        return getInt(getSharedPreferences(), Key.default_tk.toString(), 1);
    }

    public static void setFontRatio(Float font_ratio) {
        putFloat(getSharedPreferences(), Key.font_ratio.toString(), font_ratio);
    }

    public static Float getFontRatio() {
        return getFloat(getSharedPreferences(), Key.font_ratio.toString(), 1.0f);
    }

//    public static void setClipboardTk(Integer clipboard_tk) {
//        putInt(getSharedPreferences(), Key.clipboard_tk.toString(), clipboard_tk);
//    }
//
//    public static Integer getClipboardTk() {
//        return getInt(getSharedPreferences(), Key.clipboard_tk.toString(), 0);
//    }

    public static void setDefaultPj(Integer default_pj) {
        putInt(getSharedPreferences(), Key.default_pj.toString(), default_pj);
    }

    public static Integer getDefaultPj() {
        return getInt(getSharedPreferences(), Key.default_pj.toString(), 1);
    }

    public static void setAppVersionCode(Integer app_version_code) {
        putInt(getSharedPreferences(), Key.app_version_code.toString(), app_version_code);
    }

    public static Integer getAppVersionCode() {
        return getInt(getSharedPreferences(), Key.app_version_code.toString(), 0);
    }

    public static void setViewpagerAnimation(Integer viewpager_animation) {
        putInt(getSharedPreferences(), Key.vp_animation.toString(), viewpager_animation);
    }

    public static Integer getViewpagerAnimation() {
        return getInt(getSharedPreferences(), Key.vp_animation.toString(), 0);
    }

    public static void setFontSize(Float font_size) {
        putFloat(getSharedPreferences(), Key.font_size.toString(), font_size);
    }

    public static Float getFontSize() {
        return getFloat(getSharedPreferences(), Key.font_size.toString(), 20f);
    }

    public static void setTheme(Integer theme) {
        putInt(getSharedPreferences(), Key.theme.toString(), theme);
    }

    public static Integer getTheme() {
        if (MySp.getIsNight()){
            /**
             *主题色，黑色
             */
            return -13092808;
        }else {
            return getInt(getSharedPreferences(), Key.theme.toString(), -10177034);
        }


    }

    public static void setPwd(String pwd) {
        putString(getSharedPreferences(), Key.pwd.toString(), pwd);
    }

    public static String getPwd() {
        return getString(getSharedPreferences(), Key.pwd.toString(), "");
    }


    public static void setSimpleModel(Boolean simple_model) {
        putBoolean(getSharedPreferences(), Key.simple_model.toString(), simple_model);
    }

    public static Boolean getSimpleModel() {
        return getBoolean(getSharedPreferences(), Key.simple_model.toString(), false);
    }

    public static void setListAnimation(Boolean list_animation) {
        putBoolean(getSharedPreferences(), Key.list_animation.toString(), list_animation);
    }

    public static Boolean getListAnimation() {
        return getBoolean(getSharedPreferences(), Key.list_animation.toString(), true);
    }


    public static void setShowLunar(Boolean show_lunar) {
        putBoolean(getSharedPreferences(), Key.show_lunar.toString(), show_lunar);
    }

    public static Boolean getShowLunar() {
        return getBoolean(getSharedPreferences(), Key.show_lunar.toString(), true);
    }

    public static void setIsShortCut(Boolean is_short_cut) {
        putBoolean(getSharedPreferences(), Key.is_short_cut.toString(), is_short_cut);
    }

    public static Boolean getIsShortCut() {
        return getBoolean(getSharedPreferences(), Key.is_short_cut.toString(), true);
    }

    public static void setIsNight(Boolean is_night) {
        putBoolean(getSharedPreferences(), Key.is_night.toString(), is_night);
    }

    public static Boolean getIsNight() {
        return getBoolean(getSharedPreferences(), Key.is_night.toString(), false);
    }
 public static void setIsAutoNight(Boolean is_auto_night) {
        putBoolean(getSharedPreferences(), Key.is_auto_night.toString(), is_auto_night);
    }

    public static Boolean getIsAutoNight() {
        return getBoolean(getSharedPreferences(), Key.is_auto_night.toString(), false);
    }


    public static void setIsLock(Boolean is_lock) {
        putBoolean(getSharedPreferences(), Key.is_lock.toString(), is_lock);
    }

    public static Boolean getIsLock() {
        return getBoolean(getSharedPreferences(), Key.is_lock.toString(), false);
    }

    public static void setIsBigRing(Boolean is_big_ring) {
        putBoolean(getSharedPreferences(), Key.is_big_ring.toString(), is_big_ring);
    }

    public static Boolean getIsBigRing() {
        return getBoolean(getSharedPreferences(), Key.is_big_ring.toString(), false);
    }

    public static void setClipboardListen(Boolean clipboard_listen) {
        putBoolean(getSharedPreferences(), Key.clipboard_listen.toString(), clipboard_listen);
    }

    public static Boolean getClipboardListen() {
        return getBoolean(getSharedPreferences(), Key.clipboard_listen.toString(), false);
    }
    public static void setShowSimple(Boolean show_simple) {
        putBoolean(getSharedPreferences(), Key.show_simple.toString(), show_simple);
    }

    public static Boolean getShowSimple() {
        return getBoolean(getSharedPreferences(), Key.show_simple.toString(), true);
    }

    public static void setSysTip(Boolean sys_tip) {
        putBoolean(getSharedPreferences(), Key.sys_tip.toString(), sys_tip);
    }

    public static Boolean getSysTip() {
        return getBoolean(getSharedPreferences(), Key.sys_tip.toString(), false);
    }

    public static void setShowFinish(Boolean show_finish) {
        putBoolean(getSharedPreferences(), Key.show_finish.toString(), show_finish);
    }

    public static Boolean getShowFinish() {
        return getBoolean(getSharedPreferences(), Key.show_finish.toString(), true);
    }

    public static void setTodayDate(String today_date) {
        putString(getSharedPreferences(), Key.today_date.toString(), today_date);
    }

    public static String getTodayDate() {
        return getString(getSharedPreferences(), Key.today_date.toString(), "");
    }


}
