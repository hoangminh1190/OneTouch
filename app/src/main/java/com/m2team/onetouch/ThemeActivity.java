package com.m2team.onetouch;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
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
import com.rey.material.widget.TextView;
import com.startapp.android.publish.StartAppAd;


public class ThemeActivity extends ActionBarActivity implements View.OnClickListener, Slider.OnPositionChangeListener {

    private static CustomGridAdapter adapter;
    int iconId;
    ImageView previewIcon;
    TextView tvIcon, tvSize, tvNotification;
    private StartAppAd startAppAd = new StartAppAd(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themes);
        tvIcon = (TextView) findViewById(R.id.changeIcon);
        tvSize = (TextView) findViewById(R.id.setIconSize);
        tvNotification = (TextView) findViewById(R.id.setTitle);
        tvIcon.setOnClickListener(this);
        tvSize.setOnClickListener(this);
        tvNotification.setOnClickListener(this);
    }

    public void setTitle() {
        final Context context = getApplicationContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.layout_dialog_notification, null);
        final EditText edt_title = (EditText) inflate.findViewById(R.id.edt_title);
        final EditText edt_msg = (EditText) inflate.findViewById(R.id.edt_msg);
        final EditText edt_hidden_msg = (EditText) inflate.findViewById(R.id.edt_msg_hidden);

        edt_title.setText(Utils.getPrefString(context, Constant.TITLE_NOTI));
        edt_msg.setText(Utils.getPrefString(context, Constant.MSG_NOTI));
        edt_hidden_msg.setText(Utils.getPrefString(context, Constant.MSG_HIDDEN_NOTI));

        Dialog.Builder builder = new SimpleDialog.Builder() {

            @Override
            protected Dialog onBuild(Context context, int styleId) {
                Dialog dialog = super.onBuild(context, styleId);
                dialog.layoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                return dialog;
            }

            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                Utils.putPrefValue(context, Constant.TITLE_NOTI, edt_title.getText().toString());
                Utils.putPrefValue(context, Constant.MSG_NOTI, edt_msg.getText().toString());
                Utils.putPrefValue(context, Constant.MSG_HIDDEN_NOTI, edt_hidden_msg.getText().toString());
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

    public void changeIcon() {
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
                super.onPositiveActionClicked(fragment);

            }

        };

        builder.title("Icon")
                .positiveAction("OK")
                .contentView(inflate);
        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    public void setIconSize() {
        View inflate = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_dialog_size, null);
        previewIcon = (ImageView) inflate.findViewById(R.id.preview_icon);
        final Slider slSize = (Slider) inflate.findViewById(R.id.slider_size);
        final Slider slider_opacity = (Slider) inflate.findViewById(R.id.slider_opacity);
        int iconId = Utils.getPrefInt(getApplicationContext(), Constant.ICON_ID);
        if (iconId == 0) iconId = R.drawable.ic_launcher;

        int size = Utils.getPrefInt(getApplicationContext(), Constant.SIZE);
        if (size == 0) {
            int[] dimensionScreen = Utils.getDimensionScreen(this);
            size = dimensionScreen[1];
            Utils.putPrefValue(getApplicationContext(), Constant.SIZE, size);
        }


        int opacity = Utils.getPrefInt(getApplicationContext(), Constant.OPACITY);
        if (opacity == 0) {
            opacity = 255;
            Utils.putPrefValue(getApplicationContext(), Constant.OPACITY, opacity);
        }

        previewIcon.setLayoutParams(refreshImageLayout(size));
        previewIcon.setImageResource(iconId);

        slSize.setValue(size, true);
        slSize.setOnPositionChangeListener(this);

        slider_opacity.setValue(opacity, true);
        slider_opacity.setOnPositionChangeListener(this);

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

    private RelativeLayout.LayoutParams refreshImageLayout(int newValue) {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(100, 100);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.width = newValue;
        layoutParams.height = newValue;
        return layoutParams;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.changeIcon:
                changeIcon();
                break;
            case R.id.setTitle:
                setTitle();
                break;
            case R.id.setIconSize:
                setIconSize();
                break;
        }
    }

    @Override
    public void onPositionChanged(Slider view, boolean from, float oldPos, float newPos, int oldValue, int newValue) {
        switch (view.getId()) {
            case R.id.slider_size:
                OneTouchService.changeIconSize(newValue, 1);
                previewIcon.setLayoutParams(refreshImageLayout(newValue));
                break;
            case R.id.slider_opacity:
                OneTouchService.changeIconSize(newValue, 2);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                    previewIcon.setImageAlpha(newValue);
                else
                    previewIcon.setAlpha(newValue / 255);
                break;
        }
    }

    /**
     * Part of the activity's life cycle, StartAppAd should be integrated here.
     */
    @Override
    public void onResume() {
        super.onResume();
        startAppAd.onResume();
    }

    /**
     * Part of the activity's life cycle, StartAppAd should be integrated here
     * for the home button exit ad integration.
     */
    @Override
    public void onPause() {
        super.onPause();
        startAppAd.onPause();
    }

    /*@Override
    public void onBackPressed() {
        startAppAd.onBackPressed();
        super.onBackPressed();
    }*/
}
