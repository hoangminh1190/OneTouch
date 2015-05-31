package com.m2team.onetouch.core;

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
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.m2team.onetouch.Constant;
import com.m2team.onetouch.R;
import com.m2team.onetouch.Utils;


public class OverlayService extends android.app.Service {

    protected boolean foreground = false;
    protected boolean cancelNotification = false;
    protected int id = 0;

    protected Notification foregroundNotification(int notificationId, int iconId, String title, String msg) {
        return null;
    }

    public void moveToForeground(int id, boolean cancelNotification) {
        Log.e("hm", "moveFore");
        String title = Utils.getPrefString(getApplicationContext(), Constant.TITLE_NOTI);
        String msg = Utils.getPrefString(getApplicationContext(), Constant.MSG_NOTI);
        int iconId = Utils.getPrefInt(getApplicationContext(), Constant.ICON_ID_NOTI);
        if (iconId == 0) iconId = R.drawable.ic_star;
        if (TextUtils.isEmpty(title)) title = "One Touch";
        if (TextUtils.isEmpty(msg)) msg = getApplicationContext().getString(R.string.show_setting);
        moveToForeground(id, foregroundNotification(id, iconId, title, msg), cancelNotification);
    }

    public void moveToForeground(int id, Notification notification, boolean cancelNotification) {
        Log.e("hm", this.foreground == true ? "true" : "false");
        Log.e("hm", notification != null ? "!null" : "null");
        Log.e("hm", "id: " + id + " ##this.id: " + this.id);
        if (!this.foreground && notification != null) {
            Log.e("hm", "haha");
            this.foreground = true;
            this.id = id;
            this.cancelNotification = cancelNotification;

            super.startForeground(id, notification);
        } else if (id > 0 && notification != null) {
            Log.e("hm", "hehe");
            this.id = id;
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(id, notification);
        }
    }


   /* public void moveToBackground(int id, boolean cancelNotification) {
        foreground = false;
        id = 0;
        super.stopForeground(cancelNotification);
    }

    public void moveToBackground(int id) {
        moveToBackground(id, cancelNotification);
    }*/

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
