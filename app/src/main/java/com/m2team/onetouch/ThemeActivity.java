package com.m2team.onetouch;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.m2team.onetouch.main.OneTouchService;
import com.m2team.onetouch.main.adapter.CustomGridAdapter;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.app.SimpleDialog;
import com.rey.material.widget.EditText;
import com.rey.material.widget.Slider;


public class ThemeActivity extends ActionBarActivity {

    private static CustomGridAdapter adapter;
    int iconId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void setTitle(View v) {
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog, null);
        final EditText edt_title = (EditText) inflate.findViewById(R.id.edt_title);
        final EditText edt_msg = (EditText) inflate.findViewById(R.id.edt_msg);
        Dialog.Builder builder = new SimpleDialog.Builder() {

            @Override
            protected Dialog onBuild(Context context, int styleId) {
                Dialog dialog = super.onBuild(context, styleId);
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                return dialog;
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                Utils.putPrefValue(getApplicationContext(), Constant.TITLE_NOTI, edt_title.getText().toString());
                Utils.putPrefValue(getApplicationContext(), Constant.MSG_NOTI, edt_msg.getText().toString());
                super.onPositiveActionClicked(fragment);

            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                super.onNegativeActionClicked(fragment);
            }
        };

        builder.title("Notification")
                .positiveAction("OK")
                .negativeAction("CANCEL")
                .contentView(inflate);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void setMsg(View v) {
        Utils.putPrefValue(getApplicationContext(), Constant.MSG_NOTI, "Hello Vietnam");
    }

    public void changeIcon(View v) {

        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_icon, null);
        GridView gridViewIcon = (GridView) inflate.findViewById(R.id.grid_icon);
        if (adapter == null)
            adapter = new CustomGridAdapter(this);
        gridViewIcon.setAdapter(adapter);
        gridViewIcon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iconId = Integer.parseInt(String.valueOf(id));
                Utils.putPrefValue(getApplicationContext(), Constant.ICON_ID, iconId);
                OneTouchService.changeIcon(iconId);
                adapter.visibleIconAdd(view);
            }
        });
        Dialog.Builder builder = new SimpleDialog.Builder() {

            @Override
            protected Dialog onBuild(Context context, int styleId) {
                Dialog dialog = super.onBuild(context, styleId);
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                return dialog;
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                OneTouchService.changeIcon(iconId);
                Utils.putPrefValue(getApplicationContext(), Constant.ICON_ID, iconId);
                super.onPositiveActionClicked(fragment);

            }

        };

        builder.title("Icon")
                .positiveAction("OK")
                .contentView(inflate);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void setIconSize(View v) {
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_size, null);
        final ImageView previewIcon = (ImageView) inflate.findViewById(R.id.preview_icon);
        int iconId = Utils.getPrefInt(getApplicationContext(), Constant.ICON_ID);
        if (iconId == 0) iconId = R.drawable.ic_star;
        previewIcon.setImageResource(iconId);
        final Slider slSize = (Slider) inflate.findViewById(R.id.slider_size);
        final Slider slider_opacity = (Slider) inflate.findViewById(R.id.slider_opacity);
        int size = Utils.getPrefInt(getApplicationContext(), Constant.SIZE);
        if (size == 0) size = 64;
        int opacity = Utils.getPrefInt(getApplicationContext(), Constant.OPACITY);
        if (opacity == 0) opacity = 255;
        slSize.setValue(size, false);
        slider_opacity.setValue(opacity, false);
        slSize.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {

            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                OneTouchService.changeIconSize(newValue, 1);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
                layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
                layoutParams.width = newValue;
                layoutParams.height = newValue;
                previewIcon.setLayoutParams(layoutParams);
            }
        });
        slider_opacity.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                OneTouchService.changeIconSize(newValue, 2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    previewIcon.setImageAlpha(newValue);
                else
                    previewIcon.setAlpha(newValue / 255);
            }
        });
        Dialog.Builder builder = new SimpleDialog.Builder() {

            @Override
            protected Dialog onBuild(Context context, int styleId) {
                Dialog dialog = super.onBuild(context, styleId);
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                return dialog;
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                int value = slSize.getValue();
                Utils.putPrefValue(getApplicationContext(), Constant.SIZE, value);
                OneTouchService.changeIconSize(value, 1);
                value = slider_opacity.getValue();
                Utils.putPrefValue(getApplicationContext(), Constant.OPACITY, value);
                OneTouchService.changeIconSize(value, 2);
                super.onPositiveActionClicked(fragment);
            }

        };

        builder.title("Size and opacity")
                .positiveAction("OK")
                .contentView(inflate);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
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
