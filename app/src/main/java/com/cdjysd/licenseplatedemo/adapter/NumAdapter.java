package com.cdjysd.licenseplatedemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cdjysd.licenseplatedemo.R;

import java.util.List;


public class NumAdapter extends BaseAdapter {
    LayoutInflater inflater;
    List<String> list;

    public NumAdapter(Context context, List<String> list) {
        inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_num, parent, false);
            holder.num = convertView.findViewById(R.id.item_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.num.setText(list.get(position));

        return convertView;
    }

    class ViewHolder {
        public TextView num;
    }
}
