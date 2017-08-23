package com.sky.model.menu.clock.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky.constantcalendar.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-14.
 */
public class ClockAdapter extends BaseAdapter {

    private List<Map<String, String>> list;
    private Activity activity;
    private LayoutInflater inflater;

    public ClockAdapter(Activity activity, List<Map<String, String>> list){
        this.activity = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
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
        ViewHolder holder = null;
        if (convertView == null) {
            holder=new ViewHolder();
            convertView = inflater.inflate(R.layout.clock_item, parent, false);
            holder.clock_time = (TextView)convertView.findViewById(R.id.clock_time);
            holder.clock_name = (TextView)convertView.findViewById(R.id.clock_name);
            holder.clock_date = (TextView)convertView.findViewById(R.id.clock_date);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }


        holder.clock_time.setText(list.get(position).get("clock_time"));
        holder.clock_name.setText(list.get(position).get("clock_name"));
        holder.clock_date.setText(list.get(position).get("clock_date"));
        return convertView;
    }

    public final class ViewHolder{
        public TextView clock_time;
        public TextView clock_name;
        public TextView clock_date;
    }
}
