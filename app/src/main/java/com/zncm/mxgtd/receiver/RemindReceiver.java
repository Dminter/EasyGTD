/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zncm.mxgtd.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.alibaba.fastjson.JSON;
import com.zncm.mxgtd.data.Constant;
import com.zncm.mxgtd.data.DetailsData;
import com.zncm.mxgtd.data.EnumData;
import com.zncm.mxgtd.data.RemindData;
import com.zncm.mxgtd.data.TaskData;
import com.zncm.mxgtd.ui.ItemDetailsActivity;
import com.zncm.mxgtd.ui.MyApplication;
import com.zncm.mxgtd.ui.TextActivity;
import com.zncm.mxgtd.utils.DbUtils;
import com.zncm.mxgtd.utils.MySp;
import com.zncm.mxgtd.utils.NotiHelper;
import com.zncm.mxgtd.utils.PlayRingTone;
import com.zncm.mxgtd.utils.XUtil;

import java.util.Random;

public class RemindReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        String remind_info = intent.getStringExtra("remind_info");
        XUtil.debug("remind_info>>" + remind_info);
        final RemindData remindData = JSON.parseObject(remind_info, RemindData.class);
        if (remindData != null) {
//            sysDlg(context, remindData);
            if (remindData.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                //系统对话框被拦截,就只能这样了,为保证这个不被拦截,再提示一下
//                NotifyHelper.notify(context, nm, remindData);
//                String title, String content, String ticker, Intent intent, boolean autoCancel, boolean bRing, int notifyId
                NotiHelper.notifyRemind(context, remindData);

//                DatabaseHelper databaseHelper = null;
//                try {
//                    databaseHelper = OpenHelperManager.getHelper(MyApplication.getInstance().ctx, DatabaseHelper.class);
//                    RuntimeExceptionDao<RemindData, Integer> rdDao = databaseHelper.getRdDao();
//                    if (remindData.getType() == 0) {//重复的不能过期
//                        remindData.setStatus(EnumData.StatusEnum.OUTDATE.getValue());
//                    }
//                    rdDao.update(remindData);
//                } catch (Exception e) {
//                }


//                Intent intent2 = new Intent(context, TextActivity.class);
//                intent2.putExtra("text", remindData.getContent());
//                DetailsData tmp = new DetailsData();
//                tmp.setTime(remindData.getTime());
//                intent.putExtra(Constant.KEY_PARAM_DATA, tmp);
//                NotiHelper.noti(remindData.getContent(), "", "", intent2, false, !MySp.getIsBigRing(), new Random().nextInt());

//                if (MySp.getIsBigRing()) {
//                    PlayRingTone.playRing();
//                }

//                TaskData taskData = DbUtils.getTkById(remindData.getTk_id());
//                DetailsData tmp = new DetailsData();
//                tmp.setContent(remindData.getContent());
//                tmp.setTime(remindData.getTime());
//                intent = new Intent(context, ItemDetailsActivity.class);
//                intent.putExtra(Constant.KEY_PARAM_DATA, taskData);
//                intent.putExtra("DetailsData", tmp);
//                NotiHelper.noti(tmp.getContent(), "", "", intent, false, false, new Random().nextInt());
//                sysDlg(context, remindData);
//                DbUtils.initRemind(context);
            }
        }

    }

    public static void sysDlg(final Context context, final RemindData remindData) {

//        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//        final Ringtone ringtone = RingtoneManager.getRingtone(context, notification);

//        final Intent intent2 = new Intent(context, TextActivity.class);
//        intent2.putExtra("text", remindData.getContent());
//        DetailsData tmp = new DetailsData();
//        tmp.setTime(remindData.getTime());
//        intent2.putExtra(Constant.KEY_PARAM_DATA, tmp);
//        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


        DetailsData tmp = new DetailsData();
        tmp.setTime(remindData.getTime());
        tmp.setBusiness_type(EnumData.DetailBEnum.remind.getValue());
        tmp.setContent(XUtil.getTimeYMDHM(remindData.getTime()));
        tmp.setId(remindData.getId());
        final     Intent intent = new Intent(context, ItemDetailsActivity.class);
        intent.putExtra("DetailsData", tmp);
        intent.putExtra("size", 1);
        intent.putExtra(Constant.KEY_CURRENT_POSITION, 0);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        NotiHelper.noti(remindData.getContent(), "", "", intent, false, !MySp.getIsBigRing(), new Random().nextInt());

        if (MySp.getIsBigRing()) {
            PlayRingTone.playRing();
        }
        final TaskData tk = DbUtils.getTkById(remindData.getTk_id());
        String title = "MxGTD-提醒";
        if (tk != null && XUtil.notEmptyOrNull(tk.getTitle())) {
            title = tk.getTitle();
        }
        MaterialDialog dialog =  XUtil.themeMaterialDialog(MyApplication.getInstance().ctx)
                .title(title)
                .content(remindData.getContent())
                .positiveText("查看")
                .negativeText("知道了")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        PlayRingTone.stopRing();
//                        if (tk != null) {
//                            Intent select = new Intent();
//                            select.setClass(context, TkDetailsActivity.class);
//                            select.putExtra(Constant.KEY_PARAM_DATA, tk);
//                            select.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            context.startActivity(select);
//                        }

                        context.startActivity(intent);

                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        PlayRingTone.stopRing();
                    }
                })
                .build();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }

    private static void stopRing(Ringtone ringtone) {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }
}
