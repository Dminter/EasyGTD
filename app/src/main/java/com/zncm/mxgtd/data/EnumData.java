package com.zncm.mxgtd.data;

public class EnumData {


    public enum RemindRepeatTypeEnum {


        WARN_NO(0, "不提醒"), WARN_EVERY_DAY(
                1, "天"), WARN_EVERY_WORK_WEEK(
                2, "工作日"), WARN_EVERY_WEEK(
                3, "周"), WARN_EVERY_MONTH(
                4, "月"), WARN_EVERY_YEAR(
                5, "年"),;
        private int value;
        public String strName;

        private RemindRepeatTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


        public static RemindRepeatTypeEnum nameOf(String strName) {
            for (RemindRepeatTypeEnum typeEnum : RemindRepeatTypeEnum.values()) {
                if (typeEnum.getStrName().equals(strName)) {
                    return typeEnum;
                }
            }
            return null;
        }


        public static RemindRepeatTypeEnum valueOf(int value) {
            for (RemindRepeatTypeEnum typeEnum : RemindRepeatTypeEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }


    }


    public enum ViewPagerAnimationEnum {
        NONE(-1, "随机"), DefaultTransformer(0, "默认"), BackgroundToForegroundTransformer(1, "滑入"), TabletTransformer(2, "桌面"), ZoomOutSlideTransformer(3, "缩小,滑入"), DepthPageTransformer(4, "覆盖"), StackTransformer(5, "切入"), RotateUpTransformer(6, "扇面,底部"), ForegroundToBackgroundTransformer(7, "浮出"),
        RotateDownTransformer(8, "扇面");
        private int value;
        private String strName;

        private ViewPagerAnimationEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static ViewPagerAnimationEnum valueOf(int value) {
            for (ViewPagerAnimationEnum typeEnum : ViewPagerAnimationEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }


    public enum SettingEnum {

        NONE(0, "NONE"), LIST_MODEL(1, "LIST_MODEL"), THEME(2, "theme"), BACK_UP(3, "back_up"), RECOVER(4, "  recover"), FUNCTION_INTRODUCTION(5, "function_introduction"), DONATE(6, "donate"), FEED_BACK(7, "feed_back"), CHANGE_LOG(8, "change_log"),
        ERROR_LOG(9, "error_log"), OPEN_SOURCE(10, "open_source"), LIST_ANIMATION(11, "list_animation"), SETTING_PWD(12, "setting_pwd"), SHOW_LUNAR(13, "show_lunar"), CLIPBOARD_LISTEN(14, "CLIPBOARD_LISTEN"), DEFAULT_TK(15, "默认笔记本"), DEFAULT_PJ(16, "默认笔记本组"), CLIPBOARD_TK(17, "监听剪切板笔记本"), SYS_TIP(18, "系统提醒"), VIEWPAGER_ANIMATION(19, "系统提醒"), SHOW_FINISH(20, "SHOW_FINISH"), ALBUM_NUMCOLUMNS(21, "ALBUM_NUMCOLUMNS"), IS_CLIPBOARDTIP(22, "SHOW_FINISH"), IS_CELLOPTIONSHOW(23, "ALBUM_NUMCOLUMNS"), IMPORT_FILE(24, "ALBUM_NUMCOLUMNS"), IS_ENTERSAVE(25, "IS_ENTERSAVE"), IS_DELETECONFIRM(26, "IS_DELETECONFIRM"), IS_ADDTASKBYNOTIFY(27, "IS_ADDTASKBYNOTIFY"), IS_CHECKLISTENTER(28, "IS_CHECKLISTENTER"), NEW_TASK(29, "NEW_TASK"), IS_LISTEN_NOTIFY(30, "IS_LISTEN_NOTIFY"), OUTPUT_TXT(31, "OUTPUT_TXT");
        private int value;
        private String strName;

        private SettingEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static SettingEnum valueOf(int value) {
            for (SettingEnum typeEnum : SettingEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public enum HomeEnum {

        NONE(0, "NONE"), PROJECT(1, "笔记本组"), REMIND(2, "提醒"), SETTING(3, "设置"), SUM(4, "统计"), VERSION(5, "版本"), DELETED(6, "已删"), FOCUS(7, "专注"), SEARCH(8, "搜索"), LIKE(9, "收藏"), ALBUM(10, "相册"), CAL(11, "日历"), TODAY(12, "回顾"), TIMELINE(13, "筛选");
        private int value;
        private String strName;

        private HomeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static SettingEnum valueOf(int value) {
            for (SettingEnum typeEnum : SettingEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public enum SortEnum {
        //↓↑   items = new String[]{"√按时间倒序", " 按从属关系", " 按时间顺序"};
        TIME_DESC(1, "按时间倒序"), PARENT(2, "按从属关系"), TIME_ASC(3, "按时间顺序");
        private int value;
        private String strName;

        private SortEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static SortEnum valueOf(int value) {
            for (SortEnum typeEnum : SortEnum.values()) {
                if (typeEnum.getValue() == value) {
                    return typeEnum;
                }
            }
            return null;
        }

    }

    public enum DefaultSettingEnum {

        PROJECT(1, "PROJECT"), TASK(2, "task"), CLIPBOARD_TK(3, "CLIPBOARD_TK");
        private int value;
        private String strName;

        private DefaultSettingEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum fontEnum {

        FONT_1(1, "FONT_1"), FONT_2(2, "FONT_2"), FONT_3(3, "FONT_3"), FONT_4(4, "FONT_4"), FONT_5(5, "FONT_5");
        private int value;
        private String strName;

        private fontEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum queryEnum {

        _TODAY("_TODAY", "_TODAY"),_DAY("_DAY", "_DAY");
        private String value;
        private String strName;

        private queryEnum(String value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public String getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum RefreshEnum {

        PROJECT(1, "project"), TASK(2, "task"), SETTING_PWD(3, "setting_pwd"), PjInfo(4, "setting_pwd"), MAIN(5, "MAIN");
        private int value;
        private String strName;

        private RefreshEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }


    public enum DetailBEnum {
        check(1, "set"), progress(2, "setting_check"), remind(3, "check");
        private int value;
        private String strName;

        private DetailBEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }


    public enum PwdEnum {
        SET(1, "set"), SETTING_CHECK(2, "setting_check"), CHECK(3, "check");
        private int value;
        private String strName;

        private PwdEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum StatusEnum {

        //状态 1-进行中 2-完成 3-删除 4-过期
        NULL(-1, "NULL无期限"), NONE(0, "周期提醒"), ON(1, "进行中"), OFF(2, "完成"), DEL(3, "删除"), OUTDATE(4, "过期"), DEL2(5, "彻底删除");
        private int value;
        private String strName;

        private StatusEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

        public static StatusEnum valueOf(int value) {
            for (StatusEnum typeEnum : StatusEnum.values()) {
                if (typeEnum.value == value) {
                    return typeEnum;
                }
            }
            return null;
        }
    }

    public enum ProgressTypeEnum {

        //类型 1-sys 2-人
        SYS(1, "sys"), PEOPLE(2, "people");
        private int value;
        private String strName;

        private ProgressTypeEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum ProgressStatusEnum {

        //状态 1-正常 2-删除
        NORMAL(1, "正常"), DEL(2, "删除");
        private int value;
        private String strName;

        private ProgressStatusEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum ProgressActionEnum {
        //行为 1 add 2 del 3 update 4 finish 5 重启
        ADD(1, "ADD"), DEL(2, "DEL"), UPDATE(3, "UPDATE"), FINISH(4, "FINISH"), RESTART(5, "重启");
        private int value;
        private String strName;

        private ProgressActionEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }


    public enum BusinessEnum {
        //业务类型 1-笔记本组 2-笔记本  3-清单 4-进展 5-提醒
        PROJECT(1, "project"), TASK(2, "task"), CHECK_LIST(3, "check_list"), PROGRESS(4, "progress"), REMIND(5, "remind"), LIKE(6, "like"), SEARCH(7, "SEARCH");
        private int value;
        private String strName;

        private BusinessEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum ProgressBusinessEnum {
        //业务类型 1-笔记本组 2-笔记本 3-笔记本回复 4-清单
        PROJECT(1, "project"), TASK(2, "task"), REPLY(3, "reply"), CHECKLIST(4, "checklist");
        private int value;
        private String strName;

        private ProgressBusinessEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }

    }

    public enum TaskLevelEnum {

        //优先级 1 无 2 低 3 中 4 高
        NO(1, "无"), LOW(2, "低"), MIDDLE(3, "中"), HIGH(4, "高");
        private int value;
        private String strName;

        private TaskLevelEnum(int value, String strName) {
            this.value = value;
            this.strName = strName;
        }

        public int getValue() {
            return value;
        }

        public String getStrName() {
            return strName;
        }


        public static TaskLevelEnum valueOf(int value) {
            for (TaskLevelEnum typeEnum : TaskLevelEnum.values()) {
                if (typeEnum.getValue() == value) {
                    return typeEnum;
                }
            }
            return null;
        }


    }


}
