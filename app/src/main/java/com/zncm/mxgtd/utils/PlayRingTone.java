package com.zncm.mxgtd.utils;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.zncm.mxgtd.ui.MyApplication;

/**
 * Created by dminter on 2016/11/16.
 */

public class PlayRingTone {

    public static Ringtone instance;


    public static void playRing() {
        if (MySp.getIsBigRing()) {
            getInstance().play();
        }
    }

    public static void stopRing() {
        if (getInstance() != null && getInstance().isPlaying()) {
            getInstance().stop();
        }
    }


    public static Ringtone getInstance() {
        if (instance == null) {
            instance = RingtoneManager.getRingtone(MyApplication.getInstance().ctx, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        }
        return instance;
    }

}
