package com.zncm.mxgtd.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.zncm.mxgtd.R;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.receiver.RemindReceiver;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.ui.TkDetailsActivity;
import com.zncm.mxgtd.utils.db.DatabaseHelper;

import java.util.Random;


/**
 * Created by jmx on 12/17 0017.
 */
public class NotiHelper {
    private MediaPlayer mediaPlayer;

    public static void noti(String title, String content, String ticker, Intent intent, boolean autoCancel, boolean bRing, int notifyId) {
        Context ctx = MyApplication.getInstance().ctx;
        NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(ctx);
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content)
                .setContentIntent(getDefalutIntent(ctx, intent, PendingIntent.FLAG_UPDATE_CURRENT)) //设置通知栏点击意图
                //  .setNumber(number) //设置通知集合的数量
                .setTicker(ticker) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                .setAutoCancel(autoCancel)//设置这个标志当用户单击面板就可以让通知将自动取消
                .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台笔记本,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                .setSmallIcon(R.drawable.ic_launcher);//设置通知小ICON
        if (bRing) {
//            playBig(ctx);
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                    .setSound(uri);       //Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
        }
        mNotificationManager.notify(notifyId, mBuilder.build());
    }


    public static void playBig(Context context) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(context, notification);
        ringtone.play();

//        if (data != null && StrUtil.notEmptyOrNull(data.getUri())) {
//
//            if (mediaPlayer == null) {
//                mediaPlayer = new MediaPlayer();
//            } else {
//                if (mediaPlayer.isPlaying())
//                    mediaPlayer.stop();
//                mediaPlayer.reset();
//            }
//            try {
//                mediaPlayer.setVolume(0.2f, 0.2f);
//                mediaPlayer.setDataSource(ctx, Uri.parse(data.getUri()));
//                mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
//                mediaPlayer.setLooping(false);
//                mediaPlayer.prepare();
//                mediaPlayer.start();
//                if (alarmToneTimer != null)
//                    alarmToneTimer.cancel();
//                alarmToneTimer = new CountDownTimer(3000, 3000) {
//                    @Override
//                    public void onTick(long millisUntilFinished) {
//
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        try {
//                            if (mediaPlayer.isPlaying())
//                                mediaPlayer.stop();
//                        } catch (Exception e) {
//
//                        }
//                    }
//                };
//                alarmToneTimer.start();
//            } catch (Exception e) {
//                try {
//                    if (mediaPlayer.isPlaying())
//                        mediaPlayer.stop();
//                } catch (Exception e2) {
//
//                }
//            }
//        }
    }


    public static PendingIntent getDefalutIntent(Context ctx, Intent intent, int flags) {
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(ctx, new Random().nextInt(), intent, flags);
        return pendingIntent;
    }


    public static void notifyRemind(Context context, RemindData remindData) {
        DatabaseHelper databaseHelper = null;
        try {
            databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
            RuntimeExceptionDao<RemindData, Integer> rdDao = databaseHelper.getRdDao();
            if (remindData.getType() == 0) {//重复的不能过期
                remindData.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
            }
            rdDao.update(remindData);
        } catch (Exception e) {
        }

        RemindReceiver.sysDlg(context, remindData);

//        Intent select = new Intent();
//        if (remindData.getTk_id() != 0) {
//            TaskData taskData = null;
//            try {
//                databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
//                RuntimeExceptionDao<TaskData, Integer> tkDao = databaseHelper.getTkDao();
//                taskData = tkDao.queryForId(remindData.getTk_id());
//                if (taskData != null) {
//                    String title = taskData.getTitle();
//                    String content = remindData.getContent();
//                    select.setClass(context, TkDetailsActivity.class);
//                    select.putExtra(Constant.KEY_PARAM_DATA, taskData);
//                    select.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    noti(title, content, "", select, false, true, remindData.getId());
//                }
//            } catch (Exception e) {
//            }
//        }


    }

}
