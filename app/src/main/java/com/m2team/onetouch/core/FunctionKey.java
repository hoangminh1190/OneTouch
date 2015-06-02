package com.m2team.onetouch.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

/**
 * Created by Hoang Minh on 6/1/2015.
 */
public class FunctionKey {

    private static Camera mCamera;
    SurfaceHolder mHolder;

    public boolean hasFlashDevice(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    public void turnOnFlashlight(SurfaceView surfaceView) throws IOException {

        mHolder = surfaceView.getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.e("hm", "surfaceCreated");
                //mCamera.setPreviewDisplay(mHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                Log.e("hm", "surfaceChanged");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.e("hm", "surfaceDestroyed");
                /*mCamera.stopPreview();
                mHolder = null;*/
            }
        });
        // Turn on LED
        if (mCamera == null) {
            try {
                mCamera = Camera.open();
                mCamera.setPreviewDisplay(mHolder);
                Camera.Parameters params = mCamera.getParameters();
                params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(params);
                mCamera.startPreview();
            } catch (RuntimeException re) {
                Log.e("hm", "error open camera");
                turnOffFlashlight();
            }
        } else {
            Log.e("hm", "is opening camera");
            turnOffFlashlight();
        }
    }

    public void turnOffFlashlight() {
        // Turn off LED
        Camera.Parameters params = mCamera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        mCamera.setParameters(params);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    private void toggleRecents(Context context) {
        Intent closeRecents = new Intent("com.android.systemui.recent.action.TOGGLE_RECENTS");
        closeRecents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        ComponentName recents = new ComponentName("com.android.systemui", "com.android.systemui.recent.RecentsActivity");
        closeRecents.setComponent(recents);
        context.startActivity(closeRecents);
    }

}
