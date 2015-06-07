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
import android.content.Intent;
import android.os.IBinder;

import com.m2team.onetouch.Applog;
import com.m2team.onetouch.Constant;
import com.m2team.onetouch.Utils;


public class OverlayService extends android.app.Service {

    protected boolean foreground = false;
    protected int id = 0;

    protected Notification foregroundNotification(int notificationId, int iconId, String title, String msg, boolean isHidden) {
        return null;
    }

    public void moveToForeground(int id, boolean isHidden) {
        String title = Utils.getPrefString(getApplicationContext(), Constant.TITLE_NOTI);
        String msg = isHidden ? Utils.getPrefString(getApplicationContext(), Constant.MSG_HIDDEN_NOTI) : Utils.getPrefString(getApplicationContext(), Constant.MSG_NOTI);
        int iconId = Utils.getPrefInt(getApplicationContext(), Constant.ICON_ID);
        moveToForeground(id, foregroundNotification(id, iconId, title, msg, isHidden));
    }

    public void moveToForeground(int id, Notification notification) {
        if (!this.foreground && notification != null) {
            this.foreground = true;
            this.id = id;

            super.startForeground(id, notification);
        } else if (id > 0 && notification != null) {
            this.id = id;
            Applog.e("notify");
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
