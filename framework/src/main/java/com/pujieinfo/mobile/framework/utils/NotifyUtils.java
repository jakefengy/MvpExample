package com.pujieinfo.mobile.framework.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.pujieinfo.mobile.framework.R;


/**
 * 通知栏
 */
public class NotifyUtils {

    private final static int Notify_Id_Update = 100;

    private NotificationManager notifyMgr;
    private Notification updateNotify;
    private NotificationCompat.Builder builder;

    private static class SingletonHolder {
        private static final NotifyUtils INSTANCE = new NotifyUtils();
    }

    //获取单例
    public static NotifyUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private NotificationManager getNotifyMgr(Context appContext) {
        if (null == notifyMgr) {
            notifyMgr = (NotificationManager) appContext
                    .getSystemService(Context.NOTIFICATION_SERVICE);
        }

        return notifyMgr;

    }

    public void cancelNotify() {
        if (notifyMgr != null) {
            notifyMgr.cancelAll();
        }
    }

    public void showNotification(Context context, String apkFile, int progress, boolean issuccess) {

        if (updateNotify == null) {
            builder = new NotificationCompat.Builder(context);
            builder.setSmallIcon(R.mipmap.icon);
            builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon));
            //禁止用户点击删除按钮删除
            builder.setAutoCancel(false);
            //禁止滑动删除
            builder.setOngoing(true);
            //取消右上角的时间显示
            builder.setShowWhen(false);
            builder.setOngoing(true);
            builder.setShowWhen(false);
        }

        if (issuccess) {
            builder.setContentTitle("下载完成！");
            builder.setOngoing(false);
            try {
                Intent intent = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    intent = FileUtils.installN(context, apkFile);
                } else {
                    intent = FileUtils.installApk(apkFile);
                }

                if (intent == null) {
                    return;
                }

                PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(contentIntent);
                context.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            builder.setContentTitle("下载中..." + progress + "%");
        }
        builder.setProgress(100, progress, false);
        updateNotify = builder.build();
        if (progress == 100) {
            getNotifyMgr(context).cancel(Notify_Id_Update);
        } else {
            getNotifyMgr(context).notify(Notify_Id_Update, updateNotify);
        }
    }

}
