package com.zncm.mxgtd.utils;

import android.os.Environment;

import com.zncm.mxgtd.data.Constant;

import java.io.File;

public class MyPath {
    public static final String PATH_DB = "db"; // 数据库目录
    public static final String PATH_ALBUM = "album"; // 相册目录
    public static final String PATH_FILE = "file";

    public static String getFolder(String folderName) {
        if (folderName == null) {
            return null;
        }
        File dir = XUtil.createFolder(folderName);
        if (dir != null) {
            return dir.getAbsolutePath();
        } else {
            return null;
        }
    }


    private static String getPathFolder(String path) {
        File rootPath = null;
//        File extSdcard = new File("/storage/sdcard1");
//        if (extSdcard.exists()) {
//            rootPath = new File(extSdcard.getPath() + File.separator + Constant.PATH_ROOT);
//        } else {
//            rootPath = Environment.getExternalStoragePublicDirectory(Constant.PATH_ROOT);
//        }
        rootPath = Environment.getExternalStoragePublicDirectory(Constant.PATH_ROOT);
        return getFolder(rootPath + File.separator
                + path + File.separator);
    }

    public static String getOldAlbum() {
        File rootPath = Environment.getExternalStoragePublicDirectory(Constant.PATH_ROOT);
        return getFolder(rootPath + File.separator
                + "album" + File.separator);
    }


    public static String getPathDb() {
        return getPathFolder(PATH_DB);
    }

    public static String getPathAlbum() {
        return getPathFolder(PATH_ALBUM);
    }

    public static String getPathFile() {
        return getPathFolder(PATH_FILE);
    }

}
