package com.zncm.mxgtd.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.astuetz.PagerSlidingTabStrip;
import com.github.johnpersano.supertoasts.SuperToast;
import com.malinskiy.materialicons.IconDrawable;
import com.malinskiy.materialicons.Iconify;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.receiver.RemindReceiver;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.utils.statusbar.StatusBarCompat;
import com.zncm.mxgtd.view.loadmore.LoadMoreRecyclerView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by MX on 2014/8/21.
 */
public class XUtil {

    public static SpannableString getSerachString(String str, String searchWord) {
        if (!notEmptyOrNull(str)) {
            return new SpannableString("");
        }
        SpannableString spannableString = new SpannableString(str);
        try {
            //是否是搜索关键字标蓝
            ForegroundColorSpan span = new ForegroundColorSpan(MyApplication.getInstance().ctx.getResources().getColor(R.color.colorSearch));
            int bstart = str.indexOf(searchWord);  //返回指定字符串在目标字符串出现的一个次的索引！
            int eindex = bstart + searchWord.length();
            spannableString.setSpan(span, bstart, eindex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return spannableString;
    }

    /**
     * @param activity
     * @param proText  缩放的倍数
     */
    public static void scaleTextSize(Activity activity, float proText) {
        float size;
        Configuration configuration = activity.getResources().getConfiguration();
        size = proText;
        configuration.fontScale = size; //0.85 small size, 1 normal size, 1.15 big etc
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        activity.getBaseContext().getResources().updateConfiguration(configuration, metrics);//更新全局的配置
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public static void sendToDesktop(Context ctx, Class<?> mClass, String title, int resId) {
        Intent sender = new Intent();
        Intent shortcutIntent = new Intent(ctx, mClass);
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.putExtra(Intent.EXTRA_UID, 0);
        sender.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        sender.putExtra(Intent.EXTRA_SHORTCUT_NAME, makeShortcutIconTitle(title));
        sender.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(ctx, resId));
        sender.putExtra("duplicate", true);
        sender.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        ctx.sendBroadcast(sender);
    }


    public static void sendToDesktopId(Context ctx, Class<?> mClass, String title, int resId, int extra_uid) {
        Intent sender = new Intent();
        Intent shortcutIntent = new Intent(ctx, mClass);
        shortcutIntent.setAction(Intent.ACTION_VIEW);
        shortcutIntent.putExtra(Intent.EXTRA_UID, extra_uid);
        sender.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        sender.putExtra(Intent.EXTRA_SHORTCUT_NAME, makeShortcutIconTitle(title));
        sender.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(ctx, resId));
        sender.putExtra("duplicate", true);
        sender.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        ctx.sendBroadcast(sender);
    }

    // 创建快捷方式
    public static String makeShortcutIconTitle(String content) {
        XUtil.tShort("快捷方式创建成功~~~");
        final int SHORTCUT_ICON_TITLE_MAX_LEN = 10;
        final String TAG_CHECKED = String.valueOf('\u221A');
        final String TAG_UNCHECKED = String.valueOf('\u25A1');
        content = content.replace(TAG_CHECKED, "");
        content = content.replace(TAG_UNCHECKED, "");
        return content.length() > SHORTCUT_ICON_TITLE_MAX_LEN ? content.substring(0,
                SHORTCUT_ICON_TITLE_MAX_LEN) : content;

    }

    public static String getPathFromUri(Context context, Uri uri) {
        String path = "";
        if (uri == null || "content://media/external/file/-1".equals(uri.toString())) {
            XUtil.tShort("文件选取失败~");
            return null;
        }
        String[] projection = {"_data"};
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        path = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
                    }
                    cursor.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            path = uri.getPath();
        }
        return path;
    }

    public static void viewPagerRandomAnimation(ViewPager mViewPager) {
//        int type = MySp.getViewpagerAnimation();
//
//        if (type == EnumData.ViewPagerAnimationEnum.NONE.getValue()) {
//            type = new Random().nextInt(10);
//        }
//        switch (type) {
//            case 0:
//                //普通
//                mViewPager.setPageTransformer(true, new DefaultTransformer());
//                break;
//            case 1:
//                //滑入
//                mViewPager.setPageTransformer(true, new BackgroundToForegroundTransformer());
//                break;
//            case 2:
//                //桌面
//                mViewPager.setPageTransformer(true, new TabletTransformer());
//                break;
//            case 3:
//                //立方体,旋转,向外
//                mViewPager.setPageTransformer(true, new CubeOutTransformer());
//                break;
//            case 4:
//                //缩小,滑入
//                mViewPager.setPageTransformer(true, new ZoomOutSlideTransformer());
//
//                break;
//            case 5:
//                //覆盖
//                mViewPager.setPageTransformer(true, new DepthPageTransformer());
//                break;
//            case 6:
//                //切入
//                mViewPager.setPageTransformer(true, new StackTransformer());
//                break;
//            case 7:
//                //扇面,底部
//                mViewPager.setPageTransformer(true, new RotateUpTransformer());
//                break;
//            case 8:
//                //浮出
//                mViewPager.setPageTransformer(true, new ForegroundToBackgroundTransformer());
//                break;
//            case 9:
//                //扇面
//                mViewPager.setPageTransformer(true, new RotateDownTransformer());
//                break;
//            default:
//                break;
//        }
        //推入
//                mViewPager.setPageTransformer(true, new AccordionTransformer());
        //立方体内
//                mViewPager.setPageTransformer(true, new CubeInTransformer());
        //翻转,横向
//                mViewPager.setPageTransformer(true, new FlipHorizontalTransformer());
        //翻转,纵向
//                mViewPager.setPageTransformer(true, new FlipVerticalTransformer());
        //缩小
//                mViewPager.setPageTransformer(true, new ZoomInTransformer());
        //放大
//                mViewPager.setPageTransformer(true, new ZoomOutTranformer());
    }

    //year
    public static String getDateYear(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String dt = sdf.format(time);
        return dt;
    }

    //month
    public static String getDateMonth(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("M");
        String dt = sdf.format(time);
        return dt;
    }

    //month
    public static Integer getDateMonthInt(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("M");
        String dt = sdf.format(time);
        int month = Integer.valueOf(dt);
        month = month - 1;
        return month;
    }

    //day
    public static String getDateDay(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String dt = sdf.format(time);
        return dt;
    }

    //week
    public static String getDateWeek(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dt = sdf.format(time);
        return dt;
    }

    //hour
    public static String getDateHM(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dt = sdf.format(time);
        return dt;
    }


    public static Long installedTime(Context context) {
        Long installed = null;
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo appInfo = null;
            appInfo = pm.getApplicationInfo("com.zncm.mxgtd", 0);
            String appFile = appInfo.sourceDir;
            installed = new File(appFile).lastModified();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return installed;
    }

    public static void setTextView(View view, int id, Object text) {
        if (view != null) {
            TextView tv = (TextView) view.findViewById(id);
            if (tv != null && text != null) {
                tv.setText(String.valueOf(text));
            }
        }
    }


    public static void setTextView(Activity activity, int id, String text) {
        if (activity != null) {
            TextView tv = (TextView) activity.findViewById(id);
            if (tv != null && text != null) {
                tv.setText(String.valueOf(text));
            }
        }
    }


    public static void listViewRandomAnimation(LoadMoreRecyclerView listView, RecyclerView.Adapter mAdapter) {
        listView.setAdapter(mAdapter);
//        if (!MySp.getListAnimation()) {
//            listView.setAdapter(mAdapter);
//            return;
//        }
//        int type = 0;
//
//        type = new Random().nextInt(5);
//        AnimationAdapter animationAdapter = null;
//        switch (type) {
//            case 0:
//                animationAdapter = new AlphaInAnimationAdapter(mAdapter);
//                break;
//            case 1:
//                animationAdapter = new SwingLeftInAnimationAdapter(mAdapter);
//                break;
//            case 2:
//                animationAdapter = new SwingRightInAnimationAdapter(mAdapter);
//                break;
//            case 3:
//                animationAdapter = new ScaleInAnimationAdapter(mAdapter);
//                break;
//            case 4:
//                animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
//                break;
//            default:
//
//                break;
//        }
//
//        animationAdapter.setAbsListView(listView);
//        if (animationAdapter.getViewAnimator() != null) {
//            animationAdapter.getViewAnimator().setInitialDelayMillis(300);
//        }
//
//
//        listView.setAdapter(animationAdapter);

    }


    public static void dismissShowDialog(DialogInterface dialog, boolean flag) {
//        try {
//            Field field = dialog.getClass().getSuperclass().getDeclaredField("mShowing");
//            field.setAccessible(true);
//            field.set(dialog, flag);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        if (flag) {
            dialog.dismiss();
        }
    }

    public static void autoKeyBoardShow(final EditText editText) {
        new Timer().schedule(new TimerTask() {
                                 public void run() {
                                     InputMethodManager inputManager =
                                             (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                     inputManager.showSoftInput(editText, 0);
                                 }
                             },
                500);
    }


    public static final WindowManager wm = (WindowManager) MyApplication.getInstance().ctx.getSystemService(Context.WINDOW_SERVICE);

    public static DisplayMetrics getDeviceMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static int getDeviceHeight() {
        return getDeviceMetrics().heightPixels;
    }

    public static int getDeviceWidth() {
        return getDeviceMetrics().widthPixels;
    }


    public static void doRemind(Context context, RemindData data) {
        Long currentTime = System.currentTimeMillis();
        String hmStr = XUtil.getTimeHMS(data.getRemind_time());
        String hm[] = hmStr.split(":");
        int hour = Integer.parseInt(hm[0]);
        int minute = Integer.parseInt(hm[1]);
        int sec = Integer.parseInt(hm[2]);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, sec);
        calendar.set(Calendar.MILLISECOND, 0);
        if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_DAY.getValue()) {
            // 每天
            if (calendar.getTimeInMillis() > currentTime) {
                data.setRemind_time(calendar.getTimeInMillis());
                ring(context, data);
            }
        } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_WORK_WEEK.getValue()) {
            // 每工作日
            if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY
                    && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
                    && calendar.getTimeInMillis() > currentTime) {
                data.setRemind_time(calendar.getTimeInMillis());
                ring(context, data);
            }
        } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_WEEK.getValue()) {
            // 每周
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date(data.getTime()));
            if (calendar.get(Calendar.DAY_OF_WEEK) == calendar2.get(Calendar.DAY_OF_WEEK)
                    && calendar.getTimeInMillis() > currentTime) {
                data.setRemind_time(calendar.getTimeInMillis());
                ring(context, data);
            }
        } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_MONTH.getValue()) {
            // 每月
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date(data.getTime()));
            if (calendar.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)
                    && calendar.getTimeInMillis() > currentTime) {
                data.setRemind_time(calendar.getTimeInMillis());
                ring(context, data);
            }
        } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_EVERY_YEAR.getValue()) {
            // 每年
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTime(new Date(data.getTime()));
            if (calendar.get(Calendar.DAY_OF_YEAR) == calendar2.get(Calendar.DAY_OF_YEAR)
                    && calendar.getTimeInMillis() > currentTime) {
                data.setRemind_time(calendar.getTimeInMillis());
                ring(context, data);
            }
        } else if (data.getType() == EnumData.RemindRepeatTypeEnum.WARN_NO.getValue()) {
            // 不重复 一次提醒
            ring(context, data);
        }
    }


    public static void initBarTheme(Activity ctx, Toolbar toolbar) {

            toolbar.setBackgroundColor(MySp.getTheme());
            StatusBarCompat.setStatusBarColor(ctx, MySp.getTheme());


    }

    public static MaterialDialog.Builder themeMaterialDialog(Context context) {
        return new MaterialDialog.Builder(context).theme(MySp.getIsNight()?Theme.DARK:Theme.LIGHT);

    }


    public static void ring(Context ctx, RemindData data) {
        Intent sender = new Intent(ctx, RemindReceiver.class);
        sender.putExtra("remind_info", data.toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, data.getId(), sender, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alermManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alermManager.set(AlarmManager.RTC_WAKEUP, data.getRemind_time(), pendingIntent);
    }

    public static void cancelRemind(Context ctx, int remindId) {
        Intent sender = new Intent(ctx, RemindReceiver.class);
        //FLAG_UPDATE_CURRENT
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, remindId, sender, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alermManager = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
        alermManager.cancel(pendingIntent);
    }


    public static boolean copyAllFiles(String oldPath, String newPath)
            throws IOException {
        if (notEmptyOrNull(oldPath) && notEmptyOrNull(newPath)) {
            File oldFile = new File(oldPath);
            File newFile = new File(newPath);

            if (oldPath != null && newFile != null) {
                return copyFilesTo(oldFile, newFile);
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static boolean copyFilesTo(File srcDir, File destDir)
            throws IOException {
        if (srcDir == null || destDir == null) {
            return false;
        }

        if (!srcDir.exists()) {
            return false;
        }
        if (!destDir.exists()) {
            createFolder(destDir.getAbsolutePath());
        }

        if (!srcDir.isDirectory() || !destDir.isDirectory())
            return false;

        File[] srcFiles = srcDir.listFiles();
        for (int i = 0; i < srcFiles.length; i++) {
            if (srcFiles[i].isFile()) {
                File destFile = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFileTo(srcFiles[i], destFile);
            } else if (srcFiles[i].isDirectory()) {
                File theDestDir = new File(destDir.getPath() + "//"
                        + srcFiles[i].getName());
                copyFilesTo(srcFiles[i], theDestDir);
            }
        }
        return true;
    }


    public static boolean copyFileTo(File srcFile, File destFile)
            throws IOException {
        if (srcFile == null || destFile == null) {
            return false;
        }
        if (srcFile.isDirectory() || destFile.isDirectory())
            return false;
        if (!srcFile.exists()) {
            return false;
        }
        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileInputStream fis = new FileInputStream(srcFile);
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = fis.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        fis.close();
        return true;
    }

    public static boolean copyFileTo(InputStream inputStream, File destFile)
            throws IOException {
        if (inputStream == null || destFile == null) {
            return false;
        }
        if (destFile.isDirectory())
            return false;

        if (!destFile.exists()) {
            createFile(destFile.getAbsolutePath());
        }
        FileOutputStream fos = new FileOutputStream(destFile);
        int readLen = 0;
        byte[] buf = new byte[1024];
        while ((readLen = inputStream.read(buf)) != -1) {
            fos.write(buf, 0, readLen);
        }
        fos.flush();
        fos.close();
        inputStream.close();
        return true;
    }

    public static File createFile(String path) throws IOException {
        if (notEmptyOrNull(path)) {
            File file = new File(path);
            if (!file.exists()) {
                int lastIndex = path.lastIndexOf(File.separator);
                String dir = path.substring(0, lastIndex);
                if (createFolder(dir) != null) {
                    file.createNewFile();
                    return file;
                }
            } else {
                file.createNewFile();
                return file;
            }
        }
        return null;
    }

    public static File createFolder(String path) {
        if (notEmptyOrNull(path)) {
            File dir = new File(path);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    return dir;
                }
            }
            dir.mkdirs();
            return dir;
        } else {
            return null;
        }
    }


    public static Integer getVersionCode(Activity ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        Integer versionCode = packInfo.versionCode;
        return versionCode;
    }


    // SmartBar Support
    public static boolean hasSmartBar() {
        try {
            Method method = Class.forName("android.os.Build").getMethod("hasSmartBar");
            return ((Boolean) method.invoke(null)).booleanValue();
        } catch (Exception e) {
        }

        if (Build.DEVICE.equals("mx2") || Build.DEVICE.equals("mx3")) {
            return true;
        } else if (Build.DEVICE.equals("mx") || Build.DEVICE.equals("m9")) {
            return false;
        }

        return false;
    }

    public static String getVersionName(Activity ctx) {
        PackageManager packageManager = ctx.getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(ctx.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }



    public static void rateUs(Activity ctx) {
        try {
            Uri uri = Uri.parse("market://details?id=com.zncm.mxgtd");
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            ctx.startActivity(it);
        } catch (Exception e) {
            XUtil.tShort("很抱歉没能找着匹配的Android市场!");
        }
    }


    public static void sendTo(Context ctx, String sendWhat) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sendWhat);
        ctx.startActivity(shareIntent);
    }

    public static void sendFileTo(Context ctx, String filePath) {
        Uri uri = Uri.parse(filePath);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        ctx.startActivity(shareIntent);
    }

    public static void aboutUsDlg(Context ctx) {
        new MaterialDialog.Builder(ctx)
                .title("关于我们")
                .content("1.使用中遇到任何问题和意见反馈可加入产品交流群" + Constant.AUTHOR_QQ_GROUP + "\n2.注意:请勿使用系统->应用程序->清除数据,那样将会丢失本软件的一切数据,后果自担")
                .theme(Theme.LIGHT)  // the default is light, so you don't need this line
                .positiveText("知道了")
                .show();

    }


    public static void function(Context ctx) {
        Uri uri = Uri.parse(Constant.FUNCTION_INTRODUCTION_URL);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ctx.startActivity(intent);
    }


    public static long getTimeLong(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTimeInMillis();
    }

    public static long getTimeLongTenHours(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }


    public static void initIndicatorTheme(PagerSlidingTabStrip indicator) {
        Context ctx = MyApplication.getInstance().ctx;
        indicator.setTextSize(dip2px(16));
        indicator.setTextColor(ctx.getResources().getColor(R.color.white));


            indicator.setIndicatorColor(MySp.getTheme());
            indicator.setBackgroundColor(MySp.getTheme());


//        Context ctx = MyApplication.getInstance().ctx;
//        indicator.setIndicatorColor(ctx.getResources().getColor(R.color.material_light_white));
//        indicator.setBackgroundColor(MySp.getTheme());
//        indicator.setDividerColor(MySp.getTheme());
//        indicator.setTextColor(ctx.getResources().getColor(R.color.material_light_white));
//        indicator.setUnderlineHeight(XUtil.dip2px(2));
//        indicator.setIndicatorHeight(XUtil.dip2px(2));
    }

    public static void openUrl(String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MyApplication.getInstance().ctx.startActivity(it);
        } catch (Exception e) {

        }
    }

    public static void copyText(Activity ctx, String text) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
        tShort("已复制");
    }

    public static void copyText(Activity ctx, String text, String toast) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
        tShort(toast);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(float dpValue) {
        final float scale = MyApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(float pxValue) {
        final float scale = MyApplication.getInstance().ctx.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static String getDisplayDateTime(Long time) {
        String ret;
        if (time < getYearStart()) {
            ret = getTimeYMDHM(new Date(time));
        } else {
            ret = getTimeMDHM(new Date(time));
        }
        return ret;

    }

    public static String getDisplayDate(Long time) {
        String ret;
        if (time < getYearStart() || time > (getYearStart() + 365 * Constant.DAY)) {
            ret = getDateYMD(new Date(time));
        } else {
            ret = getDateMD(new Date(time));
        }
        return ret;

    }

    public static long getDayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getDayStart(Long curTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(curTime);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static long getDayTenStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 10);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }


    public static String getTimeMDHM(Date inputDate) {
        return new SimpleDateFormat("E MM-dd HH:mm").format(inputDate);
    }

    public static String getTimeYMDHM(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd E HH:mm").format(inputDate);
    }

    public static String getTimeMDHM(Long inputDate) {
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(inputDate));
    }

    public static String getTimeYMDHM(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(inputDate));
    }

    public static String getTimeYMDHM_(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm").format(new Date(inputDate));
    }

    public static String getTimeYMDE(Long inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd E").format(new Date(inputDate));
    }

    public static String getDateYMD(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }

    public static String getYear() {
        return new SimpleDateFormat("yyyy").format(new Date());
    }

    public static String getDateYMD(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }

    public static String getDateYMDNoDiv(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyyMMdd").format(inputDate);
    }

    public static String getDateYMDE(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("yyyy-MM-dd E").format(inputDate);
    }

    public static String getDateY_M_D() {
        return new SimpleDateFormat("yyyy_MM_dd").format(new Date());
    }

    public static String getDateMD(Date inputDate) {
        return new SimpleDateFormat("MM-dd").format(inputDate);
    }

    public static String getDateMD(Long inputDate) {
        return new SimpleDateFormat("MM-dd").format(new Date(inputDate));
    }

    public static String getDateMDE(Long inputDate) {
        return new SimpleDateFormat("MM-dd E").format(new Date(inputDate));
    }

    public static String getDateMDEHM(Long inputDate) {
        return new SimpleDateFormat("MM-dd E HH:mm").format(new Date(inputDate));
    }

    public static String getDateMDHM(Long inputDate) {
        return new SimpleDateFormat("MM-dd HH:mm").format(new Date(inputDate));
    }

    public static String getDateYMDEHM(Long inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd E HH:mm").format(new Date(inputDate));
    }

    public static String getDateEHM(Long inputDate) {
        return new SimpleDateFormat("E HH:mm").format(new Date(inputDate));
    }

    public static String getDateDHM(Long inputDate) {
        return new SimpleDateFormat("dd日 HH:mm").format(new Date(inputDate));
    }

    public static String getTimeHM(Date inputDate) {
        return new SimpleDateFormat("HH:mm").format(inputDate);
    }


    public static String getTimeHM(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("HH:mm").format(new Date(inputDate));
    }

    public static String getTimeHMS(Long inputDate) {
        if (inputDate == null) {
            return null;
        }
        return new SimpleDateFormat("HH:mm:ss").format(new Date(inputDate));
    }


    public static int diffDays(Long dayTime) {
        long diff = dayTime - getDayStart();
        int day = (int) (Math.abs(diff) * 1.0f / Constant.DAY);
        return day;
    }


    public static int diffNowDays(Long day2) {
        long diff = dateStrToLong(getDateYMD(new Date(day2))) - dateStrToLong(getDateYMD(new Date()));
        int day = (int) (diff * 1.0f / Constant.DAY);
        return day;
    }

    public static String diffWeekDays(int dayDiff) {
        int wk = dayDiff / 7;
        int wkLeft = dayDiff - wk * 7;
        return wkLeft == 0 ? wk + "" : wk + "/" + wkLeft;
    }

    public static String diffTime(Long time) {
        long diff = time - System.currentTimeMillis();

        diff = Math.abs(diff);

        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数
        long day = diff / nd;//计算差多少天
        long hour = diff % nd / nh;//计算差多少小时
        long min = diff % nd % nh / nm;//计算差多少分钟
        long sec = diff % nd % nh % nm / ns;//计算差多少秒//输出结果
        StringBuffer sbTime = new StringBuffer();

        if (day > 0) {
            sbTime.append(day).append("天");
        } else {
            if (hour > 0) {
                sbTime.append(hour).append("小时");
            } else {

                if (min > 0) {
                    sbTime.append(min).append("分钟");
                } else {
                    if (sec > 0) {
                        sbTime.append(sec).append("秒");
                    }
                }
            }
        }


        if (!notEmptyOrNull(sbTime.toString())) {
            sbTime.append("一会儿");
        }
        return sbTime.toString();
    }


    public static Long dateStrToLong(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date addTime = null;
        try {
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime.getTime();
    }

    public static Long dateToLong(String str) {
        Date addTime = new Date();
        if (notEmptyOrNull(str)) {

            if (str.length() == 4) {
                str = getYear() + str;
            }
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            try {
                addTime = dateFormat.parse(str);
            } catch (ParseException e) {
                e.printStackTrace();
                return addTime.getTime();
            }
        }
        return addTime.getTime();
    }


    //当年第一天
    public static long getYearStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static boolean isToday(long time) {
        boolean flag = false;
        long dayStart = getDayStart();
        if (time >= dayStart && time < dayStart + Constant.DAY) {
            flag = true;
        }
        return flag;
    }

    public static long getMonthStart(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static String getDateShow(long time) {
        String ret;
        if (time < getYearStart()) {
            ret = getDateYMD(new Date(time));
        } else {
            ret = getDateMD(new Date(time));
        }
        return ret;
    }


    public static Drawable initIconWhite(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).colorRes(R.color.white).sizeDp(24);
    }

    public static Drawable initIconBottom(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).colorRes(R.color.bt_text).sizeDp(32);
    }

    public static Drawable initIconTheme(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).color(MyApplication.getInstance().ctx.getResources().getColor(R.color.colorPrimary)).sizeDp(30);
    }

    public static Drawable initIconThemeSel(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).color(MyApplication.getInstance().ctx.getResources().getColor(R.color.colorPrimary) + 500).sizeDp(32);
    }

    public static Drawable initIconDark(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).colorRes(R.color.icon_dark).sizeDp(24);
    }

    public static Drawable initIconThemeColor(Iconify.IconValue iconValue) {
        return new IconDrawable(MyApplication.getInstance().ctx, iconValue).color(MyApplication.getInstance().ctx.getResources().getColor(R.color.colorPrimary)).sizeDp(24);
    }


    public static boolean notEmptyOrNull(String string) {
        if (string != null && !string.equalsIgnoreCase("null") && string.trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static <T> boolean listNotNull(List<T> t) {
        if (t != null && t.size() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static void debug(Object string) {
        if (true) {
//        if (BuildConfig.DEBUG) {
            try {
                Log.i("[GTD]", String.valueOf(string));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void tShort(String msg) {
        if (!notEmptyOrNull(msg)) {
            return;
        }
        Toast.makeText(MyApplication.getInstance().ctx, msg, Toast.LENGTH_SHORT).show();
    }

    public static void tLong(String msg) {
        if (!notEmptyOrNull(msg)) {
            return;
        }
        Toast.makeText(MyApplication.getInstance().ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static SuperToast.Animations getAnimations() {
        SuperToast.Animations animations = null;
        int type = 0;
        type = new Random().nextInt(4);
        switch (type) {
            case 0:
                animations = SuperToast.Animations.FADE;
                break;
            case 1:
                animations = SuperToast.Animations.FLYIN;
                break;
            case 2:
                animations = SuperToast.Animations.SCALE;
                break;
            case 3:
                animations = SuperToast.Animations.POPUP;
                break;
            default:
                animations = SuperToast.Animations.FADE;
                break;
        }
        return animations;
    }


    public static Long getLongTime() {
        return System.currentTimeMillis();
    }

    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager manager =
                (ConnectivityManager) ctx.getApplicationContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }
        NetworkInfo networkinfo = manager.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            return false;
        }
        return true;
    }


    public static void deleteFile(File oldPath) {
        if (oldPath.isDirectory()) {
            File[] files = oldPath.listFiles();
            for (File file : files) {
                deleteFile(file);
            }
        }
        oldPath.delete();
    }

}
