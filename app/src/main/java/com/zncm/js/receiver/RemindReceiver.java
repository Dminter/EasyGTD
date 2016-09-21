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

package com.zncm.js.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.zncm.js.data.Constant;
import com.zncm.js.data.EnumData;
import com.zncm.js.data.RemindData;
import com.zncm.js.data.TaskData;
import com.zncm.js.ui.MyApplication;
import com.zncm.js.ui.TkDetailsActivity;
import com.zncm.js.utils.DbUtils;
import com.zncm.js.utils.NotiHelper;
import com.zncm.js.utils.XUtil;

import java.io.Serializable;

public class RemindReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        Serializable dataParam = intent.getSerializableExtra(Constant.KEY_PARAM_DATA);
        final RemindData remindData = (RemindData) dataParam;
        if (remindData != null) {
//            sysDlg(context, remindData);
            if (remindData.getStatus() == EnumData.StatusEnum.ON.getValue()) {
                //系统对话框被拦截,就只能这样了,为保证这个不被拦截,再提示一下
//                NotifyHelper.notify(context, nm, remindData);
//                String title, String content, String ticker, Intent intent, boolean autoCancel, boolean bRing, int notifyId
                NotiHelper.notifyRemind(context, remindData);
                sysDlg(context, remindData);
                DbUtils.initRemind(context);
            }
        }

    }

    private void sysDlg(final Context context, final RemindData remindData) {
        final TaskData tk = DbUtils.getTkById(remindData.getTk_id());
        String title = "MxGTD-提醒";
        if (tk != null && XUtil.notEmptyOrNull(tk.getTitle())) {
            title = tk.getTitle();
        }
        MaterialDialog dialog = new MaterialDialog.Builder(MyApplication.getInstance().ctx)
                .title(title)
                .content(remindData.getContent())
                .positiveText("查看")
                .negativeText("知道了")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        if (tk != null) {
                            Intent select = new Intent();
                            select.setClass(context, TkDetailsActivity.class);
                            select.putExtra(Constant.KEY_PARAM_DATA, tk);
                            select.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(select);
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                    }
                })
                .build();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }
}
