package com.m2team.onetouch.main;

/*
Copyright 2011 jawsware international

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.m2team.onetouch.R;
import com.m2team.onetouch.SettingActivity;
import com.m2team.onetouch.core.OverlayService;


public class OneTouchService extends OverlayService {

    public static OneTouchService instance;

    private OneTouchOverlayView overlayView;

    static public void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }

    public static void changeIcon(int resId) {
        if (instance != null && instance.overlayView != null) {
            Log.e("hm", "changeIcon");
            instance.overlayView.changeIcon(resId);
        }
    }

    public static void setOneTouch(int type) {
        if (instance != null && instance.overlayView != null) {
            Log.e("hm", "setOneTouch: " + type);
            instance.overlayView.setActionType(type);
        }
    }

    public static void showAgain() {
        if (instance != null && instance.overlayView != null) {
            Log.e("hm", "showAgain");
            instance.overlayView.show();
        }
    }

    public static void changeIconSize(int size, int type) {
        if (instance != null && instance.overlayView != null) {
            Log.e("hm", "changeIconSize");
            instance.overlayView.changeIconSize(size, type);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //boolean isOneTouch = Utils.getPrefBoolean(getApplicationContext(), Constant.IS_ONE_TOUCH);
        overlayView = new OneTouchOverlayView(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (overlayView != null) {
            overlayView.destroy();
        }
    }

    @Override
    protected Notification foregroundNotification(int notificationId, int iconId, String title, String msg) {
        int state = msg.equals(getApplicationContext().getString(R.string.show_again)) ? 0 : 1;
        Log.e("hm", String.valueOf(state));
        PendingIntent pendingIntent = notificationIntent(state);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());
        builder.setContentText(msg).setContentTitle(title).setVibrate(new long[]{-1, 0, 1}).setSmallIcon(iconId);
        builder.setContentIntent(pendingIntent);
        return builder.build();
    }

    private PendingIntent notificationIntent(int state) {
        PendingIntent pendingIntent;
        if (state == 0) {
            Log.e("hm", "noti service");
            Intent intent = new Intent(this, OneTouchReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        } else {
            Log.e("hm", "noti activity");
            Intent intent = new Intent(this, SettingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        return pendingIntent;
    }
}
