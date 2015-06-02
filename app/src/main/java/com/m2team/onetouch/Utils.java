package com.m2team.onetouch;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 30/05/2015.
 */
public class Utils {
    public static String getPrefString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getString(key, "");
    }

    public static int getPrefInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getInt(key, 0);
    }

    public static boolean getPrefBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_MULTI_PROCESS);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void putPrefValue(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constant.PREF_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof Integer)
            editor.putInt(key, Integer.parseInt(value.toString()));
        else if (value instanceof String)
            editor.putString(key, value.toString());
        editor.commit();
    }

    public static int convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }
}
