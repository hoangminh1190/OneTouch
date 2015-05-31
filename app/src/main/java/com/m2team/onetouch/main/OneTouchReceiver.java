package com.m2team.onetouch.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class OneTouchReceiver extends BroadcastReceiver {
    public OneTouchReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("hm", "onReceive");
        OneTouchService.showAgain();
    }
}
