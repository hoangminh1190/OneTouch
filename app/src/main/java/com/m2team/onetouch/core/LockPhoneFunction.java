package com.m2team.onetouch.core;

import android.app.ActivityManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Hoang Minh on 5/30/2015.
 */
public class LockPhoneFunction {
    static final int RESULT_ENABLE = 1;
    private static DevicePolicyManager deviceManger;
    private static ActivityManager activityManager;
    private static ComponentName compName;
    Context mContext;

    public LockPhoneFunction(Context context) {
        mContext = context;
        if (deviceManger == null)
            deviceManger = (DevicePolicyManager) mContext.getSystemService(
                    Context.DEVICE_POLICY_SERVICE);
        if (activityManager == null)
            activityManager = (ActivityManager) mContext.getSystemService(
                    Context.ACTIVITY_SERVICE);
        if (compName == null)
            compName = new ComponentName(mContext, AdminPolicy.class);
    }

    public void lock() {
        boolean active = deviceManger.isAdminActive(compName);
        if (active) {
            deviceManger.lockNow();
        } else {
            Toast.makeText(mContext, "You must enable admin policy", Toast.LENGTH_SHORT).show();
        }
    }

    public void enableAdminPolicy() {
        Intent intent = new Intent(DevicePolicyManager
                .ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                compName);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                "Additional text explaining why this needs to be added.");
        mContext.startActivity(intent);
    }

    public void disableAdminPolicy() {
        deviceManger.removeActiveAdmin(compName);
    }

}
