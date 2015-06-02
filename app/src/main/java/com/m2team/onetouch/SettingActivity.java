package com.m2team.onetouch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.m2team.onetouch.core.LockPhoneFunction;
import com.m2team.onetouch.main.OneTouchService;


public class SettingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void enableAdmin(View v) {
        LockPhoneFunction function = new LockPhoneFunction(this);
        function.enableAdminPolicy();
    }

    public void disableAdmin(View v) {
        LockPhoneFunction function = new LockPhoneFunction(this);
        function.disableAdminPolicy();
    }

    public void startService(View v) {
        startService(new Intent(this, OneTouchService.class));
    }

    public void oneTouch(View v) {
        OneTouchService.setOneTouch(0);
    }

    public void exit(View v) {
        OneTouchService.stop();
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
