package com.zncm.mxgtd.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.zncm.mxgtd.data.CheckListData;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.LikeData;
import com.zncm.mxgtd.data.ProgressData;
import com.zncm.mxgtd.data.ProjectData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.ScanData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.utils.db.DatabaseHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by MX on 11/19 0019.
 */
public class DbUtils {


    static RuntimeExceptionDao<ProgressData, Integer> progressDao;
    static RuntimeExceptionDao<CheckListData, Integer> clDao;
    static RuntimeExceptionDao<TaskData, Integer> taskDao;
    static RuntimeExceptionDao<ProjectData, Integer> projectDao;
    static RuntimeExceptionDao<RemindData, Integer> rdDao;
    static RuntimeExceptionDao<LikeData, Integer> likeDao = null;
    static DatabaseHelper databaseHelper = null;

    static DatabaseHelper getHelper() {
        if (databaseHelper == null) {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
        }
        return databaseHelper;
    }

    static void init() {
        if (progressDao == null) {
            progressDao = getHelper().getProgressDao();
        }

        if (taskDao == null) {
            taskDao = getHelper().getTkDao();
        }
        if (projectDao == null) {
            projectDao = getHelper().getPjDao();
        }
        if (clDao == null) {
            clDao = getHelper().getClDao();
        }
        if (rdDao == null) {
            rdDao = getHelper().getRdDao();
        }


        if (likeDao == null) {
            likeDao = getHelper().getLikeDao();
        }
    }


    public static void finishCheckList(TaskData tk) {
        try {
            //完成清单
            init();
            UpdateBuilder<CheckListData, Integer> clBuilder = clDao.updateBuilder();
            clBuilder.where().eq("tk_id", tk.getId());
            clBuilder.updateColumnValue("status", EnumData.StatusEnum.OFF.getValue());
            clBuilder.update();
            //完成提醒
            if (rdDao == null) {
                rdDao = getHelper().getRdDao();
            }
            UpdateBuilder<RemindData, Integer> rdBuilder = rdDao.updateBuilder();
            rdBuilder.where().eq("tk_id", tk.getId());
            rdBuilder.updateColumnValue("status", EnumData.StatusEnum.OUTDATE.getValue());
            rdBuilder.update();
        } catch (Exception e) {

        }
    }


    public static void like(String content, int business_type, int business_id, int parent_id) {
        init();
        try {
            QueryBuilder<LikeData, Integer> builder = likeDao.queryBuilder();
            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().eq("business_type", business_type).and().eq("business_id", business_id);
            List<LikeData> list = likeDao.query(builder.prepare());
            if (!XUtil.listNotNull(list)) {
                //String content, int business_type, int business_id
                LikeData tmp = new LikeData(content, business_type, business_id, parent_id);
                likeDao.create(tmp);
                XUtil.tShort("已加入收藏!");
            } else {
                LikeData tmp = list.get(0);
                likeDao.deleteById(tmp.getId());
                XUtil.tShort("收藏已取消!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isLiked(int business_type, int business_id) {
        init();
        try {
            QueryBuilder<LikeData, Integer> builder = likeDao.queryBuilder();
            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().eq("business_type", business_type).and().eq("business_id", business_id);
            List<LikeData> list = likeDao.query(builder.prepare());
            if (!XUtil.listNotNull(list)) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }


    public static void addRd(RemindData remindData) {
        init();
        try {
            rdDao.create(remindData);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean insertProgress(String content) {
        init();
        boolean flag = false;
        try {
            if (!XUtil.notEmptyOrNull(content)) {
                return false;
            }
            TaskData tk = taskDao.queryForId(MySp.getDefaultTk());
            if (tk != null) {
                ProgressData progressData = new ProgressData(content, EnumData.ProgressTypeEnum.SYS.getValue(),
                        EnumData.ProgressBusinessEnum.REPLY.getValue(), EnumData.ProgressStatusEnum.NORMAL.getValue(),
                        tk.getPj_id(), tk.getId(), EnumData.ProgressActionEnum.ADD.getValue()
                );
                progressDao.create(progressData);
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public static TaskData getDefaultTk() {
        init();
        TaskData tk = null;
        try {
            tk = taskDao.queryForId(MySp.getDefaultTk());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tk;
    }

    public static void insertRemind(RemindData remindData) {
        init();
        try {
            if (remindData != null) {
                rdDao.create(remindData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertTask(TaskData tk) {
        init();
        try {
            if (tk != null) {
                taskDao.create(tk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertCheckList(CheckListData checkListData) {
        init();
        try {
            if (checkListData != null) {
                clDao.create(checkListData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void insertProgress(ProgressData progressData) {
        init();
        try {
            if (progressData != null) {
                progressDao.create(progressData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Integer getMaxRemind() {
        Integer maxInt = -1;
        init();
        try {
            maxInt = Integer.parseInt(clDao.queryRaw("SELECT max(id) from reminddata").getFirstResult()[0]);
        } catch (Exception e) {
        }
        return maxInt;
    }

    public static Integer getMaxPj() {
        Integer maxInt = -1;
        init();
        try {
            maxInt = Integer.parseInt(projectDao.queryRaw("SELECT max(id) from projectdata").getFirstResult()[0]);
        } catch (Exception e) {
        }
        return maxInt;
    }

    public static Integer getMaxTk() {
        Integer maxInt = -1;
        init();
        try {
            maxInt = Integer.parseInt(projectDao.queryRaw("SELECT max(id) from taskdata").getFirstResult()[0]);
        } catch (Exception e) {
        }
        return maxInt;
    }

    public static Integer getMaxProgress() {
        Integer maxInt = -1;
        init();
        try {
            maxInt = Integer.parseInt(projectDao.queryRaw("SELECT max(id) from progressdata").getFirstResult()[0]);
        } catch (Exception e) {
        }
        return maxInt;
    }

    public static int getTkRows(int tk_id) {
        int progressInt = 0;
        int checkInt = 0;
        int remindInt = 0;
        init();
        try {
            progressInt = Integer.parseInt(projectDao.queryRaw("SELECT count(id) from progressdata where tk_id = " + tk_id).getFirstResult()[0]);
            checkInt = Integer.parseInt(clDao.queryRaw("SELECT count(id) from checklistdata where tk_id = " + tk_id).getFirstResult()[0]);
            remindInt = Integer.parseInt(rdDao.queryRaw("SELECT count(id) from reminddata where tk_id = " + tk_id).getFirstResult()[0]);
        } catch (Exception e) {
        }
        return progressInt + checkInt + remindInt;
    }

    public static void insertProgressForCsv(String path) {
        init();
        try {
            File file = new File(path);
            List<String> datas = CSVUtils.importCsv(file);
            int pj_id = MySp.getDefaultPj();
            TaskData tk = new TaskData(file.getName(), XUtil.getTimeYMDHM(System.currentTimeMillis()) + "导入", System.currentTimeMillis(), System.currentTimeMillis(), EnumData.TaskLevelEnum.NO.getValue(), pj_id, null, null);
            taskDao.create(tk);
            int _id = 0;
            ArrayList<TaskData> tkDatas = new ArrayList<TaskData>();
            QueryBuilder<TaskData, Integer> builder = taskDao.queryBuilder();
            builder.orderBy("id", false).limit(1l);
            tkDatas = (ArrayList<TaskData>) taskDao.query(builder.prepare());
            TaskData tkData = tkDatas.get(0);
            if (tkData != null) {
                _id = tkData.getId();
            }
            if (_id != 0) {
                for (String tmp : datas) {
                    if (XUtil.notEmptyOrNull(tmp)) {
                        ProgressData progressData = new ProgressData(tmp, EnumData.ProgressTypeEnum.SYS.getValue(),
                                EnumData.ProgressBusinessEnum.REPLY.getValue(), EnumData.ProgressStatusEnum.NORMAL.getValue(),
                                pj_id, _id, EnumData.ProgressActionEnum.ADD.getValue()
                        );
                        progressDao.create(progressData);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<DetailsData> getDetailsDatas(int tk_id, int startRow, int endRow) {
        init();
        ArrayList<DetailsData> infos = new ArrayList<DetailsData>();
        try {
            final SQLiteDatabase db = getHelper().getReadableDatabase();
            String MY_SQL_QUERY = "SELECT id,time,title as content,1 as business_type,status,tk_id,0 as remind_time,0 as type FROM checklistdata WHERE tk_id = " + tk_id + " AND status <>3 UNION SELECT id,time,content,2,status,tk_id,0,0 FROM progressdata WHERE tk_id = " + tk_id + " AND status <>2 UNION SELECT id,time,content,3,status,tk_id,remind_time,type FROM reminddata WHERE tk_id = " + tk_id + " AND status <>3 ORDER BY time DESC LIMIT " + startRow + "," + endRow;
            Cursor cursor = db.rawQuery(MY_SQL_QUERY, null);
            while (cursor.moveToNext()) {
                DetailsData tmp = new DetailsData();
                tmp.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tmp.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                tmp.setContent(cursor.getString(cursor.getColumnIndex("content")));
                tmp.setBusiness_type(cursor.getInt(cursor.getColumnIndex("business_type")));
                tmp.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                tmp.setTk_id(cursor.getInt(cursor.getColumnIndex("tk_id")));
                tmp.setRemind_time(cursor.getLong(cursor.getColumnIndex("remind_time")));
                tmp.setType(cursor.getInt(cursor.getColumnIndex("type")));
                infos.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }

    public static ArrayList<DetailsData> getItemsPageMaxRows(int tk_id, int pageIndex, int maxRow) {
        init();
        ArrayList<DetailsData> infos = new ArrayList<DetailsData>();
        try {
            final SQLiteDatabase db = getHelper().getReadableDatabase();
            String MY_SQL_QUERY = "SELECT id,time,title as content,1 as business_type,status,tk_id,0 as remind_time,0 as type FROM checklistdata WHERE tk_id = " + tk_id + " AND status <>3 UNION SELECT id,time,content,2,status,tk_id,0,0 FROM progressdata WHERE tk_id = " + tk_id + " AND status <>2 UNION SELECT id,time,content,3,status,tk_id,remind_time,type FROM reminddata WHERE tk_id = " + tk_id + " AND status <>3 ORDER BY time DESC LIMIT " + maxRow + " offset" + pageIndex;// .limit(Constant.MAX_DB_QUERY).offset((long) pageIndex);
            Cursor cursor = db.rawQuery(MY_SQL_QUERY, null);
            while (cursor.moveToNext()) {
                DetailsData tmp = new DetailsData();
                tmp.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tmp.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                tmp.setContent(cursor.getString(cursor.getColumnIndex("content")));
                tmp.setBusiness_type(cursor.getInt(cursor.getColumnIndex("business_type")));
                tmp.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                tmp.setTk_id(cursor.getInt(cursor.getColumnIndex("tk_id")));
                tmp.setRemind_time(cursor.getLong(cursor.getColumnIndex("remind_time")));
                tmp.setType(cursor.getInt(cursor.getColumnIndex("type")));
                infos.add(tmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }

    public static ArrayList<DetailsData> getDetailsDatasByContent(String content, int startRow, int endRow) {
        init();
        ArrayList<DetailsData> infos = new ArrayList<DetailsData>();
        try {
            final SQLiteDatabase db = getHelper().getReadableDatabase();

            if (!XUtil.notEmptyOrNull(content)) {
                return null;
            }
            String MY_SQL_QUERY = "";


            Long dayStart = 0l;

            if (content.startsWith("@")) {
                dayStart = XUtil.dateToLong(content.replace("@", ""));
            } else if (content.equals(EnumData.queryEnum._TODAY.getValue())) {
                dayStart = XUtil.getDayStart();
            }
            if (dayStart > 0) {
                MY_SQL_QUERY = "SELECT id,time,title as content,1 as business_type,status,tk_id,0 as remind_time,0 as type FROM checklistdata WHERE time > " + dayStart + "  AND time < " + (dayStart + Constant.DAY) + " AND status <>3 UNION SELECT id,time,content,2,status,tk_id,0,0 FROM progressdata WHERE time > " + dayStart + "  AND time < " + (dayStart + Constant.DAY) + " AND status <>2 UNION SELECT id,time,content,3,status,tk_id,remind_time,type FROM reminddata WHERE time > " + dayStart + "  AND time < " + (dayStart + Constant.DAY) + " AND status <>3 ORDER BY time DESC LIMIT " + startRow + "," + endRow;
            } else {
                MY_SQL_QUERY = "SELECT id,time,title as content,1 as business_type,status,tk_id,0 as remind_time,0 as type FROM checklistdata WHERE content like '%" + content + "%' AND status <>3 UNION SELECT id,time,content,2,status,tk_id,0,0 FROM progressdata WHERE content like '%" + content + "%' AND status <>2 UNION SELECT id,time,content,3,status,tk_id,remind_time,type FROM reminddata WHERE content like '%" + content + "%' AND status <>3 ORDER BY time DESC LIMIT " + startRow + "," + endRow;
            }


            XUtil.debug("MY_SQL_QUERY=>" + MY_SQL_QUERY);

            Cursor cursor = db.rawQuery(MY_SQL_QUERY, null);

            while (cursor.moveToNext()) {
                //根据列的索引直接读取  比如第0列的值
//                String strValue= cursor.getString(0);
//                XUtil.debug("strValue:"+strValue);
//                int nameColumnIndex = cursor.getColumnIndex(“username");
//               String strValue=cursor.getString(nameColumnIndex);

//                private int id;
//                private Long time;
//                private String content;
//                private int business_type;//业务类型 1-笔记本组 2-笔记本 3-笔记本回复 4-清单
//                private int status;//状态 1-正常 3-删除
//                private int tk_id;//笔记本 id
//                private Long remind_time;//提醒时间
//                private int type;//类型 0-一次提醒 1-重复提醒【不存在过期概念】
                DetailsData tmp = new DetailsData();
                tmp.setId(cursor.getInt(cursor.getColumnIndex("id")));
                tmp.setTime(cursor.getLong(cursor.getColumnIndex("time")));
                tmp.setContent(cursor.getString(cursor.getColumnIndex("content")));
                tmp.setBusiness_type(cursor.getInt(cursor.getColumnIndex("business_type")));
                tmp.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                tmp.setTk_id(cursor.getInt(cursor.getColumnIndex("tk_id")));
                tmp.setRemind_time(cursor.getLong(cursor.getColumnIndex("remind_time")));
                tmp.setType(cursor.getInt(cursor.getColumnIndex("type")));
                infos.add(tmp);
            }


//            infos = (ArrayList<DetailsData>) resultQueryBuilder.query();

//            ArrayList<String> pNames = new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return infos;
    }


    public static Long firstTask(Context ctx) {
        Long time = System.currentTimeMillis();
        init();
        try {
            QueryBuilder<TaskData, Integer> builder = taskDao.queryBuilder();
            builder.orderBy("time", true).limit(1);
            List<TaskData> list = taskDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                TaskData data = list.get(0);
                if (data != null) {
                    time = data.getTime();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static List<RemindData> initRemind(Context ctx) {
        init();
        List<RemindData> ret = new ArrayList<>();
        try {
            QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
            //提醒 未来一周以内的
            builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
            List<RemindData> list = rdDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                for (RemindData data : list) {
                    //提醒 未来一周以内的 and 重复提醒
                    if (data.getRemind_time() > XUtil.getLongTime() && data.getRemind_time() < XUtil.getLongTime() + Constant.DAY * 7) {
                        XUtil.ring(ctx, data);
                        ret.add(data);
                    } else if (data.getType() > 0 && data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        XUtil.doRemind(ctx, data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static ArrayList<TaskData> getWidgetTasks() {
        init();
        ArrayList<TaskData> datas = new ArrayList<TaskData>();
        try {
            QueryBuilder<TaskData, Integer> builder = taskDao.queryBuilder();
            builder.where().eq("status", EnumData.StatusEnum.ON.getValue()).and().ge("start_time", XUtil.getDayStart());
            builder.orderBy("start_time", true).limit(Constant.MAX_DB_QUERY);
            List<TaskData> list = taskDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<ScanData> getProgressPage(int tk_id, int pageIndex) {
        init();
        ArrayList<ScanData> datas = new ArrayList<ScanData>();
        try {
            QueryBuilder<ProgressData, Integer> builder = progressDao.queryBuilder();
            builder.where().eq("status", EnumData.ProgressStatusEnum.NORMAL.getValue()).and().eq("tk_id", tk_id);
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY).offset((long) pageIndex);
            List<ProgressData> list = progressDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                for (ProgressData progressData : list) {
                    datas.add(new ScanData(progressData.getId(), EnumData.BusinessEnum.PROGRESS.getValue(), progressData.getContent(), progressData.getTime()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static List<ProgressData> getProgressForTest() {
        init();
        List<ProgressData> datas = new ArrayList<ProgressData>();
        try {
            QueryBuilder<ProgressData, Integer> builder = progressDao.queryBuilder();
            builder.where().eq("status", EnumData.ProgressStatusEnum.NORMAL.getValue());
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            datas = progressDao.query(builder.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ProgressData getProgressByTkId(int tkId) {
        init();
        ProgressData ret = null;
        List<ProgressData> datas = new ArrayList<ProgressData>();
        try {
            QueryBuilder<ProgressData, Integer> builder = progressDao.queryBuilder();
            builder.where().eq("tk_id", tkId).and().eq("status", EnumData.ProgressStatusEnum.NORMAL.getValue());
            builder.limit(Constant.MAX_DB_QUERY);
            datas = progressDao.query(builder.prepare());
            if (XUtil.listNotNull(datas)) {
                ret = datas.get(new Random().nextInt(datas.size()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static ProgressData getProgressLast() {
        init();
        ProgressData last = null;
        List<ProgressData> datas = new ArrayList<ProgressData>();
        try {
            QueryBuilder<ProgressData, Integer> builder = progressDao.queryBuilder();
            builder.where().eq("status", EnumData.ProgressStatusEnum.NORMAL.getValue());
            builder.orderBy("time", false).limit(1);
            datas = progressDao.query(builder.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (XUtil.listNotNull(datas)) {
            last = datas.get(0);
        }
        return last;
    }

    public static ProgressData getProgressRandom() {
        init();
        ProgressData last = null;
        List<ProgressData> datas = new ArrayList<ProgressData>();
        try {
            int maxRow = getMaxProgress();
            QueryBuilder<ProgressData, Integer> builder = progressDao.queryBuilder();
            builder.where().eq("status", EnumData.ProgressStatusEnum.NORMAL.getValue());
            builder.where().idEq(new Random().nextInt(maxRow));
            datas = progressDao.query(builder.prepare());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (XUtil.listNotNull(datas)) {
            XUtil.debug("info-datas:" + datas);
            last = datas.get(0);
        }
        return last;
    }


   

    public static ArrayList<ScanData> getCheckListPage(int tk_id, int pageIndex) {
        init();
        ArrayList<ScanData> datas = new ArrayList<ScanData>();
        try {
            QueryBuilder<CheckListData, Integer> builder = clDao.queryBuilder();
            builder.where().eq("tk_id", tk_id).and().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OFF.getValue());
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY).offset((long) pageIndex);
            List<CheckListData> list = clDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                for (CheckListData checkListData : list) {
                    String tmp = null;
                    if (checkListData.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        tmp = "×";
                    } else if (checkListData.getStatus() == EnumData.StatusEnum.OFF.getValue()) {
                        tmp = "√";
                    }
                    datas.add(new ScanData(checkListData.getId(), EnumData.BusinessEnum.CHECK_LIST.getValue(), tmp + " " + checkListData.getTitle(), checkListData.getTime()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<ProjectData> getProjectDataAll() {
        init();
        ArrayList<ProjectData> datas = new ArrayList<ProjectData>();
        try {
            QueryBuilder<ProjectData, Integer> builder = projectDao.queryBuilder();
            builder.where().in("status", EnumData.StatusEnum.ON.getValue());
//            builder.where().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OFF.getValue());
            builder.orderBy("modify_time", false).limit(20);
            List<ProjectData> list = projectDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<TaskData> getTaskDataByDefaultPj() {
        init();
        ArrayList<TaskData> datas = new ArrayList<TaskData>();
        try {
            int default_pj = MySp.getDefaultPj();
            QueryBuilder<TaskData, Integer> builder = taskDao.queryBuilder();
            builder.where().eq("pj_id", default_pj).and().in("status", EnumData.StatusEnum.ON.getValue());
            builder.orderBy("level", true).orderBy("modify_time", false).limit(20);
            List<TaskData> list = taskDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }


    public static ArrayList<ScanData> getLikePage(int pageIndex) {
        init();
        ArrayList<ScanData> datas = new ArrayList<ScanData>();
        try {
            QueryBuilder<LikeData, Integer> builder = likeDao.queryBuilder();
            builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY).offset((long) pageIndex);
            List<LikeData> list = likeDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                for (LikeData likeData : list) {
                    datas.add(new ScanData(likeData.getId(), EnumData.BusinessEnum.LIKE.getValue(), likeData.getContent(), likeData.getTime()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static void savePj(String content) {
        init();
        try {
            ProjectData pj = new ProjectData(content, "", System.currentTimeMillis(), System.currentTimeMillis());
            projectDao.create(pj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveTk(String content) {
        init();
        try {
            int pj_id = new Random().nextInt(5) + 1;
            TaskData tk = new TaskData(content, "", System.currentTimeMillis(), System.currentTimeMillis(), 0, pj_id, null, "");
            taskDao.create(tk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveRd(int min) {
        init();
        try {
            long time = min * Constant.MIN + System.currentTimeMillis();
            RemindData remindData = new RemindData(XUtil.getDateMDEHM(time), EnumData.StatusEnum.ON.getValue(), MySp.getDefaultTk(), time);
            rdDao.create(remindData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<String> getDataForCSV(TaskData taskData) {
        init();
        ArrayList<String> datas = new ArrayList<String>();
        try {
            StringBuilder tmp;
            int tk_id = taskData.getId();
            datas.add("笔记本名称,状态,优先级,创建时间,开始时间,完成时间,描述");
            tmp = new StringBuilder();
            tmp.append(taskData.getTitle()).append(",");
            tmp.append(EnumData.StatusEnum.valueOf(taskData.getStatus()).getStrName()).append(",");
            tmp.append(EnumData.TaskLevelEnum.valueOf(taskData.getLevel()).getStrName()).append(",");
            tmp.append(taskData.getTime() != null ? XUtil.getTimeYMDHM(taskData.getTime()) : "").append(",");
            tmp.append(taskData.getStart_time() != null ? XUtil.getTimeYMDHM(taskData.getStart_time()) : "").append(",");
            tmp.append(taskData.getStop_time() != null ? XUtil.getTimeYMDHM(taskData.getStop_time()) : "").append(",");
            tmp.append(taskData.getDescribe()).append(",");
            datas.add(tmp.toString());

            QueryBuilder<ProgressData, Integer> builder = progressDao.queryBuilder();
            builder.where().eq("tk_id", tk_id).and().eq("status", EnumData.ProgressStatusEnum.NORMAL.getValue());
            List<ProgressData> list = progressDao.query(builder.prepare());

            datas.add("进展,时间");
            if (XUtil.listNotNull(list)) {
                for (ProgressData data : list) {
                    tmp = new StringBuilder();
                    tmp.append(data.getContent().replaceAll(",", "，").replaceAll("\n", "       "));
                    tmp.append(",");
                    tmp.append(XUtil.getTimeYMDHM(data.getTime()));
                    tmp.append(",");
                    datas.add(tmp.toString());
                }
            }
            //checklist
            QueryBuilder<CheckListData, Integer> builder2 = clDao.queryBuilder();
            builder2.where().eq("tk_id", tk_id).and().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OFF.getValue());
            List<CheckListData> list2 = clDao.query(builder2.prepare());
            datas.add("\n\n清单,状态,时间");
            if (XUtil.listNotNull(list2)) {
                for (CheckListData data : list2) {
                    tmp = new StringBuilder();
                    if (data.getStatus() == EnumData.StatusEnum.OFF.getValue()) {
                        tmp.append(data.getTitle().replaceAll(",", "，").replaceAll("\n", "       ")).append(",").append("√").append(",");
                    } else if (data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        tmp.append(data.getTitle().replaceAll(",", "，").replaceAll("\n", "       ")).append(",").append("×").append(",");
                    }
                    tmp.append(XUtil.getTimeYMDHM(data.getTime())).append(",");
                    datas.add(tmp.toString());
                }
            }

            //remind
            QueryBuilder<RemindData, Integer> builder3 = rdDao.queryBuilder();
            builder3.where().eq("tk_id", tk_id).and().in("status", EnumData.StatusEnum.ON.getValue(), EnumData.StatusEnum.OUTDATE.getValue());
            List<RemindData> list3 = rdDao.query(builder3.prepare());
            datas.add("\n\n事件,状态,提醒时间");
            if (XUtil.listNotNull(list)) {
                for (RemindData data : list3) {
                    tmp = new StringBuilder();
                    if (data.getStatus() == EnumData.StatusEnum.OUTDATE.getValue()) {
                        tmp.append(data.getContent().replaceAll(",", "，").replaceAll("\n", "       ")).append(",").append("√").append(",");
                    } else if (data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        tmp.append(data.getContent().replaceAll(",", "，").replaceAll("\n", "       ")).append(",").append("×").append(",");
                    }
                    tmp.append(XUtil.getTimeYMDHM(data.getTime()));
                    tmp.append(",");
                    datas.add(tmp.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<RemindData> getWidgetReminds() {
        init();
        ArrayList<RemindData> datas = new ArrayList<RemindData>();
        try {
            QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
            //提醒 未来一周以内的
            builder.where().eq("status", EnumData.StatusEnum.ON.getValue());
            List<RemindData> list = rdDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                for (RemindData data : list) {
                    //提醒 未来一周以内的 and 重复提醒
                    if (data.getRemind_time() > XUtil.getLongTime() && data.getRemind_time() < XUtil.getLongTime() + Constant.DAY * 7) {
                        datas.add(data);
                    } else if (data.getType() > 0 && data.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                        datas.add(data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static ArrayList<RemindData> getFocusReminds() {
        init();
        ArrayList<RemindData> datas = new ArrayList<RemindData>();
        try {
            QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
            //提醒 未来一周以内的
            builder.where().eq("status", RemindData.STATUS_FOCUS).and().isNotNull("modify_time");
            builder.orderBy("time", false).limit(Constant.MAX_DB_QUERY);
            List<RemindData> list = rdDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                datas.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datas;
    }

    public static RemindData getFocusRun() {
        RemindData ret = null;
        init();
        ArrayList<RemindData> datas = new ArrayList<RemindData>();
        try {
            QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
            //提醒 未来一周以内的
            builder.where().eq("status", RemindData.STATUS_FOCUS).and().isNull("modify_time");
            builder.orderBy("time", false).limit(1);
            List<RemindData> list = rdDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }


    public static RemindData getFocusTop() {
        RemindData ret = null;
        init();
        ArrayList<RemindData> datas = new ArrayList<RemindData>();
        try {
            RemindData tmp = rdDao.queryForFirst(null);

            QueryBuilder<RemindData, Integer> builder = rdDao.queryBuilder();
            //提醒 未来一周以内的
            builder.where().eq("status", RemindData.STATUS_FOCUS).and().isNull("modify_time");
            builder.orderBy("time", false).limit(1);
            List<RemindData> list = rdDao.query(builder.prepare());
            if (XUtil.listNotNull(list)) {
                ret = list.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String getFocusTitle() {
        String ret = null;
        Integer maxInt;
        init();
        try {
            maxInt = Integer.parseInt(clDao.queryRaw("SELECT max(id) from reminddata").getFirstResult()[0]);
            ret = "专注_" + maxInt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static ProjectData getPjById(int id) {
        ProjectData projectData = null;
        init();
        try {
            projectData = projectDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return projectData;
    }

    public static int addPj() {
        int id = 0;
        init();
        try {
            ProjectData pj = new ProjectData("默认笔记本组", "", System.currentTimeMillis(), System.currentTimeMillis() + 365 * Constant.DAY);
            projectDao.create(pj);
            id = getMaxPj();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static int addTk(String title) {
        int id = 0;
        init();
        try {
            TaskData tk = new TaskData(title, "", System.currentTimeMillis(), System.currentTimeMillis() + 365 * Constant.DAY, EnumData.TaskLevelEnum.NO.getValue(), MySp.getDefaultPj(), null, "");
            taskDao.create(tk);
            id = getMaxTk();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public static TaskData getTkById(int id) {
        TaskData taskData = null;
        init();
        try {
            taskData = taskDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskData;
    }

    public static ProgressData getProgressDataById(int id) {
        ProgressData progressData = null;
        init();
        try {
            progressData = progressDao.queryForId(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressData;
    }


    public static void delByBType(int business_type, int id) {
        init();
        try {
            if (business_type == EnumData.BusinessEnum.PROGRESS.getValue()) {
                UpdateBuilder updateBuilder = progressDao.updateBuilder();
                updateBuilder.where().eq("id", id);
                updateBuilder.updateColumnValue("status", EnumData.ProgressStatusEnum.DEL.getValue());
                updateBuilder.update();
            } else if (business_type == EnumData.BusinessEnum.CHECK_LIST.getValue()) {
                UpdateBuilder updateBuilder = clDao.updateBuilder();
                updateBuilder.where().eq("id", id);
                updateBuilder.updateColumnValue("status", EnumData.StatusEnum.DEL.getValue());
                updateBuilder.update();
            } else {
                XUtil.tShort("删除失败!仅可删除清单和进展!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void delProgressByTkId(int tk_id) {
        init();
        try {
            UpdateBuilder updateBuilder = progressDao.updateBuilder();
            updateBuilder.where().eq("tk_id", tk_id);
            updateBuilder.updateColumnValue("status", EnumData.ProgressStatusEnum.DEL.getValue());
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delProgressById(int _id) {
        init();
        try {
            UpdateBuilder updateBuilder = progressDao.updateBuilder();
            updateBuilder.where().eq("id", _id);
            updateBuilder.updateColumnValue("status", EnumData.ProgressStatusEnum.DEL.getValue());
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delCheckListById(int _id) {
        init();
        try {
            UpdateBuilder updateBuilder = clDao.updateBuilder();
            updateBuilder.where().eq("id", _id);
            updateBuilder.updateColumnValue("status", EnumData.StatusEnum.DEL.getValue());
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void delRemindById(int id) {
        init();
        try {
            UpdateBuilder updateBuilder = rdDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("status", EnumData.StatusEnum.DEL.getValue());
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void del2ByBType(int business_type, int id) {
        init();
        try {
            if (business_type == EnumData.BusinessEnum.PROJECT.getValue()) {
                UpdateBuilder updateBuilder = projectDao.updateBuilder();
                updateBuilder.where().eq("id", id);
                updateBuilder.updateColumnValue("status", EnumData.StatusEnum.DEL2.getValue());
                updateBuilder.update();
            } else if (business_type == EnumData.BusinessEnum.TASK.getValue()) {
                UpdateBuilder updateBuilder = taskDao.updateBuilder();
                updateBuilder.where().eq("id", id);
                updateBuilder.updateColumnValue("status", EnumData.StatusEnum.DEL2.getValue());
                updateBuilder.update();
            } else {
                XUtil.tShort("删除失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void renewByBType(int business_type, int id) {
        init();
        try {
            if (business_type == EnumData.BusinessEnum.PROJECT.getValue()) {
                UpdateBuilder updateBuilder = projectDao.updateBuilder();
                updateBuilder.where().eq("id", id);
                updateBuilder.updateColumnValue("status", EnumData.StatusEnum.ON.getValue());
                updateBuilder.update();
            } else if (business_type == EnumData.BusinessEnum.TASK.getValue()) {
                UpdateBuilder updateBuilder = taskDao.updateBuilder();
                updateBuilder.where().eq("id", id);
                updateBuilder.updateColumnValue("status", EnumData.StatusEnum.ON.getValue());
                updateBuilder.update();
            } else {
                XUtil.tShort("删除失败!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void updateTaskByStatus(int status, int id) {
        init();
        try {
            UpdateBuilder updateBuilder = taskDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("status", status);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateRemind(int id) {
        init();
        try {
            UpdateBuilder updateBuilder = rdDao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("modify_time", System.currentTimeMillis());
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateTaskByPjTitle(String pj_title, int pj_id) {
        init();
        try {
            UpdateBuilder updateBuilder = taskDao.updateBuilder();
            updateBuilder.where().eq("pj_id", pj_id);
            updateBuilder.updateColumnValue("tag", pj_title);
            updateBuilder.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
