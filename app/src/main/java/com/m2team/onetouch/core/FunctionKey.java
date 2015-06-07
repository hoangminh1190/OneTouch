package com.m2team.onetouch.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by Hoang Minh on 6/1/2015.
 */
public class FunctionKey {

    private static Camera mCamera;
    private static long freeMemory;
    SurfaceHolder mHolder;

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            File f;
            for (int i = 0; i < children.length; i++) {
                f = new File(dir, children[i]);
                boolean success = deleteDir(f);
                if (!success) {
                    return false;
                } else {
                    freeMemory += f.length();
                }
            }
        }

        return dir.delete();
    }

    public static void freeSpace(Context context) {
        PackageManager pm = context.getPackageManager();
        // Get all methods on the PackageManager
        Method[] methods = pm.getClass().getDeclaredMethods();
        for (Method m : methods) {
            if (m.getName().equals("freeStorage")) {
                // Found the method I want to use
                try {
                    long desiredFreeStorage = Long.MAX_VALUE; // Request for 8GB of free space
                    m.invoke(pm, desiredFreeStorage, null);
                } catch (Exception e) {
                    // Method invocation failed. Could be a permission problem
                }
                break;
            }
        }
    }

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

    public void clearApplicationData(Context context) {
        freeMemory = 0;
        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));
                    Log.i("hm", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
        Toast.makeText(context, "Free memory: " + freeMemory / 1024 / 1024 + " MB", Toast.LENGTH_SHORT).show();
    }

}
