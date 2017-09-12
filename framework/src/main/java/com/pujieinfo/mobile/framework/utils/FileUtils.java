package com.pujieinfo.mobile.framework.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;

import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * 2017-05-23.
 */

public class FileUtils {

    private final static String IMAGE_CACHE = "image_cache/";

    public static String getAvailableCachePath(Context context) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File exDir = context.getExternalCacheDir();

            if (exDir != null) {
                return exDir.getPath() + "/" + IMAGE_CACHE;
            }
        }
        return context.getCacheDir().getPath() + "/" + IMAGE_CACHE;

    }

    public static String getApkSavePath() {
        try {
            File exDir = new File(getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).getPath(), "tazdb");
            if (!exDir.exists()) {
                exDir.mkdirs();
            }

            return exDir.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getCameraPath() {
        try {
            File exDir = new File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath(), "tazdb");
            if (!exDir.exists()) {
                exDir.mkdirs();
            }

            return exDir.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean mkdirs(String path) {
        File file = new File(path);
        if (file.isDirectory() && !file.exists()) {
            return file.mkdirs();
        }
        return false;
    }

    public static Intent installApk(String apkFile) {
        try {
            File file = new File(apkFile);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Intent installN(Context context, String path) {
        try {
            File file = new File(path);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, "com.pujieinfo.tazdb.fileprovider", file);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            // 由于没有在Activity环境下启动Activity,设置下面的标签
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

            return intent;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
