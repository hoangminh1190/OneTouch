package com.m2team.onetouch.main;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

/**
 * Created by Administrator on 31/05/2015.
 */
public class FloatingButtonListener implements View.OnClickListener {
    Context context;
    FloatingActionMenu menu;

    public FloatingButtonListener(Context context, FloatingActionMenu menu) {
        this.context = context;
        this.menu = menu;
    }

    @Override
    public void onClick(View v) {
        Integer fabIndex = (Integer) v.getTag();
        switch (fabIndex) {
            case 1:
                Log.e("hm", "1");
                Intent startMain = new Intent(Intent.ACTION_MAIN);
                startMain.addCategory(Intent.CATEGORY_HOME);
                startMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(startMain);
                break;
            case 2:
                Log.e("hm", "2");
                break;
            case 3:
                Log.e("hm", "3");
                break;
            case 4:
                Log.e("hm", "4");
                break;
            case 5:
                Log.e("hm", "5");
                break;
            default:
                Log.e("hm", "df");
                break;
        }
        menu.toggle(true);
    }
}
