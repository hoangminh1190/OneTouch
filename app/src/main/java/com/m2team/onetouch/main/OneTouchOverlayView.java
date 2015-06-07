package com.m2team.onetouch.main;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.m2team.onetouch.Applog;
import com.m2team.onetouch.Constant;
import com.m2team.onetouch.R;
import com.m2team.onetouch.Utils;
import com.m2team.onetouch.core.FunctionKey;
import com.m2team.onetouch.core.LockPhoneFunction;
import com.m2team.onetouch.core.OverlayService;
import com.m2team.onetouch.core.OverlayView;

import java.io.IOException;


public class OneTouchOverlayView extends OverlayView implements GestureDetector.OnGestureListener {

    SurfaceView previewFlashlight;
    private ImageView icon;
    private int initialX;
    private int initialY;
    private float initialTouchX;
    private float initialTouchY;
    private int actionType;
    private long timeTouchDown;
    private int width = 0, height = 0;
    private boolean isLongClick = false;
    private GestureDetectorCompat mDetector;

    public OneTouchOverlayView(OverlayService service) {
        super(service, R.layout.overlay, 1);
        layoutParams.x = 0;
        layoutParams.y = 100;
        actionType = -1;
        mDetector = new GestureDetectorCompat(getContext(), this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    public int getLayoutGravity() {
        return Gravity.CENTER_VERTICAL + Gravity.LEFT;
    }

    @Override
    protected void onInflateView() {
        icon = (ImageView) this.findViewById(R.id.icon);
        previewFlashlight = (SurfaceView) findViewById(R.id.preview_flashlight);

        //icon
        icon.setImageResource(Utils.getPrefInt(getContext(), Constant.ICON_ID));

        //size of icon
        int dp = Utils.getPrefInt(getContext(), Constant.SIZE);
        icon.setLayoutParams(new RelativeLayout.LayoutParams(dp, dp));

        //opacity
        dp = Utils.getPrefInt(getContext(), Constant.OPACITY);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            icon.setImageAlpha(dp);
        else
            icon.setAlpha(dp / 255);

    }

    @Override
    protected void refreshViews() {
        ((WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE)).updateViewLayout(this, layoutParams);
    }

    /*@Override
    protected void onTouchEvent_Up(MotionEvent event) {
        long currentTime = System.currentTimeMillis();
        Applog.v("Up" + (currentTime - timeTouchDown));
        if (currentTime - timeTouchDown < 150) {
            Applog.v("CLICKED");
            executeOneTouch();
            isLongClick = false;
        } else if (isLongClick && currentTime - timeTouchDown < 830) {
            Applog.v("LONG");
            getService().moveToForeground(1, true);
            unload();
        } else {
            changeToEdge();
            Applog.v("DRAG");
            isLongClick = false;
        }
    }

    @Override
    protected void onTouchEvent_Move(MotionEvent event) {
        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
        refreshViews();
        *//*Applog.e("initX: " + initialX);
        Applog.e("event.getRawX: " + event.getRawX());
        Applog.e("initialTouchX: " + initialTouchX);
        Applog.e("layoutParams.x: " + layoutParams.x);
        Applog.e("initY: " + initialY);
        Applog.e("event.getRawY: " + event.getRawY());
        Applog.e("initialTouchY: " + initialTouchY);
        Applog.e("layoutParams.y: " + layoutParams.y);*//*
    }

    @Override
    protected void onTouchEvent_Press(MotionEvent event) {
        Applog.v("Down");
        initialX = layoutParams.x;
        initialY = layoutParams.y;
        initialTouchX = event.getRawX();
        initialTouchY = event.getRawY();
        timeTouchDown = System.currentTimeMillis();
    }

    @Override
    public boolean onTouchEvent_LongPress() {
        isLongClick = true;
        return true;
    }*/

    @Override
    protected void show() {
        getService().moveToForeground(1, false);
        load();
    }

    public void changeIcon(int resourceId) {
        icon.setImageResource(resourceId);
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void executeOneTouch() {
        Context context = getContext();
        switch (actionType) {
            case 0:
                LockPhoneFunction function = new LockPhoneFunction(getContext());
                function.lock();
                break;

            case 1:
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                getContext().startActivity(intent);
                break;

            case 2:
                FunctionKey functionKey = new FunctionKey();
                try {
                    if (functionKey.hasFlashDevice(context)) {
                        functionKey.turnOnFlashlight(previewFlashlight);
                    } else {
                        Toast.makeText(context, "Phone does not support flash light", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Applog.e("flash error");
                }
                break;
            default:
                break;

        }
    }

    public void changeIconSize(int dp, int type) {
        if (icon != null) {
            if (type == 1)
                icon.setLayoutParams(new RelativeLayout.LayoutParams(dp, dp));
            else if (type == 2) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    icon.setImageAlpha(dp);
                else
                    icon.setAlpha(dp / 255);
            }
        }
    }

    public void changeToEdge() {
        if (width == 0 && height == 0) {
            WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            width = size.x;
            height = size.y;
        }
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        int left = getLeftOnScreen();
        int top = getTopOnScreen();
        Applog.e("L: " + left + " T: " + top + " W: " + width + " H: " + height);
        if (left < width / 2) {
            layoutParams.x = 0;//left
           /* if (top > (0.75 * height)) {
                layoutParams.y = height;
            } else if (top < (0.25 * height)) {
                layoutParams.y = 100;
            } else {
                layoutParams.x = 0;//left
            }*/
        } else {
            layoutParams.x = width;
            /*if (top > (0.75 * height)) {
                layoutParams.y = height;
            } else if (top < (0.25 * height)) {
                layoutParams.y = 100;
            } else {
                layoutParams.x = width;
            }*/
        }
        refreshViews();
    }

    @Override
    public boolean onDown(MotionEvent event) {
        initialX = layoutParams.x;
        initialY = layoutParams.y;
        initialTouchX = event.getRawX();
        initialTouchY = event.getRawY();
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Applog.e("onShowPress");
        changeToEdge();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Applog.e("CLICKED");
        executeOneTouch();
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent event, float distanceX, float distanceY) {
        layoutParams.x = initialX + (int) (event.getRawX() - initialTouchX);
        layoutParams.y = initialY + (int) (event.getRawY() - initialTouchY);
        refreshViews();
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Applog.e("LONG");
        getService().moveToForeground(1, true);
        unload();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Applog.e("DRAG");
        changeToEdge();
        return false;
    }
}
