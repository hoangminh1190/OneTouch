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

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.m2team.onetouch.Constant;
import com.m2team.onetouch.R;
import com.m2team.onetouch.Utils;
import com.m2team.onetouch.core.LockPhoneFunction;
import com.m2team.onetouch.core.OverlayService;
import com.m2team.onetouch.core.OverlayView;


public class OneTouchOverlayView extends OverlayView {

    private ImageView icon;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private int actionType;
    private long timeTouchDown;
    private int resId;
    private Context mContext;

    public OneTouchOverlayView(OverlayService service) {
        super(service, R.layout.overlay, 1);
        mContext = getContext();
        layoutParams.x = 0;
        layoutParams.y = 100;
        actionType = -1;
    }

    public int getLayoutGravity() {
        return Gravity.CENTER_VERTICAL + Gravity.LEFT;
    }

    @Override
    protected void onInflateView() {
        icon = (ImageView) this.findViewById(R.id.icon);
        resId = Utils.getPrefInt(getContext(), Constant.ICON_ID_NOTI);
        if (resId == 0) {
            resId = R.drawable.ic_star;
            Utils.putPrefValue(mContext, Constant.ICON_ID_NOTI, resId);
        }
        icon.setImageResource(resId);
    }

    @Override
    protected void refreshViews() {
    }

    @Override
    protected void onTouchEvent_Up(MotionEvent event) {
        if (System.currentTimeMillis() - timeTouchDown > 300) {
            Log.e("hm", "DRAGGGGGGGGGGGGGGGGGGGGGG");
            /*float rawX = event.getRawX();
            float rawY = event.getRawY();
            int[] location = new int[2];
            getLocationOnScreen(location);
            if (rawX < location[0]);
            int topOnScreen = getTopOnScreen();
            layoutParams.x = topOnScreen;
            layoutParams.y = leftOnScreen;
            refreshLayout();*/
        } else {
            Log.e("hm", "CLICKED");
            executeOneTouch();
        }
    }

    @Override
    protected void onTouchEvent_Move(MotionEvent event) {
        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
        refreshLayout();
    }

    @Override
    protected void onTouchEvent_Press(MotionEvent event) {
        initialX = layoutParams.x;
        initialY = layoutParams.y;
        initialTouchX = event.getRawX();
        initialTouchY = event.getRawY();
        timeTouchDown = System.currentTimeMillis();
    }

    @Override
    protected void show() {
        Utils.putPrefValue(getService(), Constant.MSG_NOTI, "Click here to setting");
        getService().moveToForeground(1, !showNotificationHidden());
        load();
    }

    @Override
    public boolean onTouchEvent_LongPress() {
        Utils.putPrefValue(getService(), Constant.MSG_NOTI, getContext().getString(R.string.show_again));
        getService().moveToForeground(1, true);
        unload();

        return true;
    }

    public void changeIcon(int resourceId) {
        icon.setImageResource(resourceId);
        Utils.putPrefValue(mContext, Constant.ICON_ID_NOTI, resourceId);
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void executeOneTouch() {
        switch (actionType) {
            case 0:
                LockPhoneFunction function = new LockPhoneFunction(mContext);
                function.lock();
                break;
            default:
                break;

        }
    }


}
