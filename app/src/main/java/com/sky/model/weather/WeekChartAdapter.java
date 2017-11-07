package com.sky.model.weather;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.constantcalendar.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-10-1.
 */
public class WeekChartAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, Object>> dataList;
    private int type;

    public WeekChartAdapter(Context context, List<Map<String, Object>> weekChartData, int type){
        this.context = context;
        this.dataList = weekChartData;
        this.type = type;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.chart_item, parent, false);
            holder = new ViewHolder();
            holder.wt_chart_weekday = (TextView) convertView.findViewById(R.id.wt_chart_weekday);
            holder.wt_chart_date = (TextView) convertView.findViewById(R.id.wt_chart_date);
            holder.wt_chart_typeicon = (ImageView) convertView.findViewById(R.id.wt_chart_typeicon);
            holder.wt_chart_type = (TextView) convertView.findViewById(R.id.wt_chart_type);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.wt_chart_weekday.setText((String) dataList.get(position).get("wt_chart_weekday"));
        holder.wt_chart_date.setText((String) dataList.get(position).get("wt_chart_date"));
        if(type==0){
            holder.wt_chart_typeicon.setImageResource((Integer) dataList.get(position).get("wt_chart_typeday_icon"));
            holder.wt_chart_type.setText((String) dataList.get(position).get("wt_chart_typeday"));
        }else{
            holder.wt_chart_typeicon.setImageResource((Integer) dataList.get(position).get("wt_chart_typenight_icon"));
            holder.wt_chart_type.setText((String) dataList.get(position).get("wt_chart_typenight"));
        }
        return convertView;
    }

    public class ViewHolder{
        private TextView wt_chart_weekday;
        private TextView wt_chart_date;
        private ImageView wt_chart_typeicon;
        private TextView wt_chart_type;
    }
}
