package com.m2team.onetouch;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import com.m2team.onetouch.core.LockPhoneFunction;
import com.m2team.onetouch.main.OneTouchService;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.Button;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.SnackBar;
import com.startapp.android.publish.Ad;
import com.startapp.android.publish.AdDisplayListener;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;
import com.startapp.android.publish.splash.SplashConfig;

public class OneActivity extends ActionBarActivity implements View.OnClickListener {

    Button btn_about, btn_setting, btn_theme, btn_uninstall, btn_start;
    ProgressView progress_view;

    /** StartAppAd object declaration */
    private StartAppAd startAppAd = new StartAppAd(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "105895203", "205365019", true);
        //105895203 - AccountID
        //205365019 - AppId
        setContentView(R.layout.layout_main);
        StartAppAd.showSlider(this);
        init();
        if (OneTouchService.instance != null) {
            onStartedService();
        } else {
            onStoppedService();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAppAd.onResume();
        if (OneTouchService.instance != null) {
            if (progress_view != null && btn_start != null) {
                onStartedService();
            }
        } else {
            if (progress_view != null && btn_start != null) {
                onStoppedService();
            }
        }
    }

    private void onStartedService() {
        progress_view.setVisibility(View.VISIBLE);
        progress_view.start();
        btn_start.setText("STOP");
    }

    private void onStoppedService() {
        progress_view.setVisibility(View.INVISIBLE);
        progress_view.stop();
        btn_start.setText("START");
    }

    private void init() {
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_about = (Button) findViewById(R.id.btn_about);
        btn_theme = (Button) findViewById(R.id.btn_theme);
        btn_setting = (Button) findViewById(R.id.btn_setting);
        btn_uninstall = (Button) findViewById(R.id.btn_uninstall);
        progress_view = (ProgressView) findViewById(R.id.progress_view);
        btn_start.setOnClickListener(this);
        btn_about.setOnClickListener(this);
        btn_theme.setOnClickListener(this);
        btn_setting.setOnClickListener(this);
        btn_uninstall.setOnClickListener(this);
    }

    public void startService() {
        int actionType = Utils.getPrefInt(this, Constant.ACTION_TYPE);
        if (actionType == 0) {
            LockPhoneFunction function = new LockPhoneFunction(this);
            if (!function.isDeviceAdminActive())
                function.enableAdminPolicy();
        }
        startService(new Intent(this, OneTouchService.class));
        progress_view.setVisibility(View.VISIBLE);
        btn_start.setText("STOP");
    }

    public void uninstall() {
        LockPhoneFunction function = new LockPhoneFunction(this);
        if (function.isDeviceAdminActive()) {
            function.disableAdminPolicy();
        }
        //uninstall apps
        Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                btn_start = (Button) v;
                if (btn_start.getText().toString().equalsIgnoreCase("START")) {
                    startService();
                    onStartedService();
                }
                else {
                    OneTouchService.stop();
                    onStoppedService();
                }
                break;
            case R.id.btn_setting:
                showFunctionDialog();
                break;
            case R.id.btn_theme:
                // Show an Ad
                boolean showAd = startAppAd.showAd(new AdDisplayListener() {

                    /**
                     * Callback when Ad has been hidden
                     * @param ad
                     */
                    @Override
                    public void adHidden(Ad ad) {
                        Applog.v("adHidden");
                        // Run second activity right after the ad was hidden
                        Intent intent = new Intent(OneActivity.this, ThemeActivity.class);
                        startActivity(intent);
                    }

                    /**
                     * Callback when ad has been displayed
                     * @param ad
                     */
                    @Override
                    public void adDisplayed(Ad ad) {
                        Applog.v("adDisplayed");
                    }

                    /**
                     * Callback when ad has been clicked
                     */
                    @Override
                    public void adClicked(Ad arg0) {
                        Applog.v("AD_CLICKED");
                    }

                    /*@Override
                    public void adNotDisplayed(Ad ad) {
                        Applog.e("AD_ERROR");
                        Intent intent = new Intent(OneActivity.this, ThemeActivity.class);
                        startActivity(intent);
                    }*/
                });
                if (!showAd) {
                    Intent intent = new Intent(OneActivity.this, ThemeActivity.class);
                    startActivity(intent);
                }

                break;
            case R.id.btn_about:
                startActivity(new Intent(this, AboutActivity.class));
                break;
            case R.id.btn_uninstall:
                uninstall();
                break;
        }
    }

    private void showFunctionDialog() {
        Dialog.Builder builder = new SimpleDialog.Builder(R.style.SimpleDialogLight) {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                OneTouchService.setOneTouch(getSelectedIndex());
                Utils.putPrefValue(OneActivity.this, Constant.ACTION_TYPE, getSelectedIndex());
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };
        int actionType = Utils.getPrefInt(OneActivity.this, Constant.ACTION_TYPE);
        ((SimpleDialog.Builder) builder).items(new String[]{"Lock phone", "Home key", "Flashlight"}, actionType)
                .title("Function key")
                .positiveAction("OK")
                .negativeAction("CANCEL");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    @Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }

}
