package com.m2team.onetouch.main;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.m2team.onetouch.Applog;
import com.m2team.onetouch.Constant;
import com.m2team.onetouch.OneActivity;
import com.m2team.onetouch.R;
import com.m2team.onetouch.Utils;
import com.m2team.onetouch.core.OverlayService;


public class OneTouchService extends OverlayService {

    public static OneTouchService instance;

    private OneTouchOverlayView overlayView;

    static public void stop() {
        if (instance != null) {
            instance.stopSelf();
            instance = null;
        }
    }

    public static void changeIcon(int resId) {
        if (instance != null && instance.overlayView != null) {
            Applog.d("changeIcon");
            instance.overlayView.changeIcon(resId);
        }
    }

    public static void setOneTouch(int type) {
        if (instance != null && instance.overlayView != null) {
            Applog.d("setOneTouch: " + type);
            instance.overlayView.setActionType(type);
        }
    }

    public static void showAgain() {
        if (instance != null && instance.overlayView != null) {
            Applog.d("showAgain");
            instance.overlayView.show();
        }
    }

    public static void changeIconSize(int size, int type) {
        if (instance != null && instance.overlayView != null) {
            Applog.d("changeIconSize");
            instance.overlayView.changeIconSize(size, type);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        instance = this;
        overlayView = new OneTouchOverlayView(this);
        int actionType = Utils.getPrefInt(getApplicationContext(), Constant.ACTION_TYPE);
        overlayView.setActionType(actionType);

    }

    private void init() {
        if (TextUtils.isEmpty(Utils.getPrefString(getApplicationContext(), Constant.MSG_NOTI))) {
            Utils.putPrefValue(getApplicationContext(), Constant.MSG_NOTI, getString(R.string.show_setting));
        }
        if (TextUtils.isEmpty(Utils.getPrefString(getApplicationContext(), Constant.MSG_HIDDEN_NOTI))) {
            Utils.putPrefValue(getApplicationContext(), Constant.MSG_HIDDEN_NOTI, getString(R.string.show_again));
        }
        if (TextUtils.isEmpty(Utils.getPrefString(getApplicationContext(), Constant.TITLE_NOTI))) {
            Utils.putPrefValue(getApplicationContext(), Constant.TITLE_NOTI, getString(R.string.app_name));
        }
        if (Utils.getPrefInt(getApplicationContext(), Constant.ICON_ID) == 0) {
            Utils.putPrefValue(getApplicationContext(), Constant.ICON_ID, R.drawable.ic_launcher);
        }
        if (Utils.getPrefInt(getApplicationContext(), Constant.SIZE) == 0) {
            int[] dimensionScreen = Utils.getDimensionScreen(getApplicationContext());
            Utils.putPrefValue(getApplicationContext(), Constant.SIZE, dimensionScreen[1] / 15);
        }
        if (Utils.getPrefInt(getApplicationContext(), Constant.OPACITY) == 0) {
            Utils.putPrefValue(getApplicationContext(), Constant.OPACITY, 255);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayView != null) {
            overlayView.destroy();
        }
    }

    @Override
    protected Notification foregroundNotification(int notificationId, int iconId, String title, String msg, boolean isHidden) {
        PendingIntent pendingIntent = notificationIntent(isHidden);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentText(msg).setContentTitle(title).setVibrate(new long[]{-1, 0, 1}).setSmallIcon(iconId);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }

    private PendingIntent notificationIntent(boolean isHidden) {
        PendingIntent pendingIntent;
        if (isHidden) {
            Intent intent = new Intent();
            intent.setAction("com.m2team.onetouch.main.UPDATE_NOTIFICATION");
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Intent intent = new Intent(this, OneActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return pendingIntent;
    }
}
