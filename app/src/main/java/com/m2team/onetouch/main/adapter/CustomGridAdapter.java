package com.m2team.onetouch.main.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.m2team.onetouch.R;

import java.util.ArrayList;
import java.util.List;

public class CustomGridAdapter extends BaseAdapter {
    Context context;
    TypedArray typedArray;
    List<ViewHolder> holders;

    public CustomGridAdapter(Context c) {
        context = c;
        typedArray = c.getResources().obtainTypedArray(R.array.arr_icons);
        holders = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return typedArray.length();
    }

    @Override
    public Integer getItem(int position) {
        return typedArray.getResourceId(position, 0);
    }

    @Override
    public long getItemId(int position) {
        return typedArray.getResourceId(position, 0);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {// if current item is not showing --> must re-init it
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_dialog_each_icon, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) view.findViewById(R.id.icon);
            holder.iconAdd = (ImageView) view.findViewById(R.id.icon_add);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.image.setBackgroundResource(getItem(position));
        holder.iconAdd.setVisibility(View.INVISIBLE);
        view.setTag(holder);
        holders.add(holder);
        return view;
    }

    public void visibleIconAdd(View view) {
        for (ViewHolder viewHolder : holders) {
            if (viewHolder != null && viewHolder.iconAdd != null)
                viewHolder.iconAdd.setVisibility(View.INVISIBLE);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.iconAdd.setVisibility(View.VISIBLE);

    }

    private static class ViewHolder {
        private ImageView image;
        private ImageView iconAdd;

    }
}
