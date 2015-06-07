package com.m2team.onetouch.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.m2team.onetouch.Applog;

public class OneTouchReceiver extends BroadcastReceiver {
    public OneTouchReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Applog.d("onReceive");
        OneTouchService.showAgain();
    }
}
