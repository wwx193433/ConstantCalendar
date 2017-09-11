package com.sky.model.menu.almanac;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky.constantcalendar.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-9-6.
 */
public class ShichenAdapter extends BaseAdapter {

    private Context context;
    private List<Map<String, String>> dataList;
    private LayoutInflater inflater;

    public String[] sc_keys={"子", "丑", "寅", "卯", "辰", "巳", "午", "未", "申", "酉", "戌", "亥"};
    public String[] sc_names={"丙子时", "丁丑时", "戊寅时", "己卯时", "庚辰时", "辛巳时", "壬午时", "癸未时", "甲申时", "乙酉时", "丙戌时", "丁亥时"};
    public String[] sc_times={"23:00-00:59", "01:00-02:59", "03:00-04:59", "05:00-06:59", "07:00-08:59",
            "09:00-10:59", "11:00-12:59", "13:00-14:59", "15:00-16:59", "17:00-18:59", "19:00-20:59", "21:00-22:59"};
    public ShichenAdapter(Context context, List<Map<String, String>> dataList) {
        this.context = context;
        this.dataList = dataList;
        this.inflater = LayoutInflater.from(context);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView itemView = null;
        if(convertView == null){
            itemView = new ListItemView();
            convertView = this.inflater.inflate(R.layout.shichen_detail_item, parent, false);
            itemView.setShichen_key((TextView) convertView.findViewById(R.id.shichen_key));
            itemView.setShichen_name((TextView) convertView.findViewById(R.id.shichen_name));
            itemView.setShichen_time((TextView) convertView.findViewById(R.id.shichen_time));
            itemView.setShichen_chongsha((TextView) convertView.findViewById(R.id.shichen_chongsha));
            itemView.setShichen_jixiong((TextView) convertView.findViewById(R.id.shichen_jixiong));
            itemView.setShichen_caixi((TextView) convertView.findViewById(R.id.shichen_caixi));
            itemView.setShichen_yi((TextView) convertView.findViewById(R.id.shichen_yi));
            itemView.setShichen_ji((TextView) convertView.findViewById(R.id.shichen_ji));
            convertView.setTag(itemView);
        }else{
            itemView = (ListItemView)convertView.getTag();
        }
        Map<String, String> datamap = dataList.get(position);
        itemView.getShichen_key().setText(sc_keys[position]);
        itemView.getShichen_name().setText(sc_names[position]);
        itemView.getShichen_time().setText(sc_times[position]);
        itemView.getShichen_chongsha().setText(datamap.get("chongsha"));
        itemView.getShichen_jixiong().setText(datamap.get("jixiong"));
        itemView.getShichen_caixi().setText("财神" + datamap.get("cai") + " 喜神" + datamap.get("xi"));
        if(datamap.get("jixiong").equals("吉")){
            itemView.getShichen_jixiong().setBackgroundResource(R.drawable.ji_circle_back);
        }else{
            itemView.getShichen_jixiong().setBackgroundResource(R.drawable.xiong_circle_back);
        }
        itemView.getShichen_yi().setText(datamap.get("shiyi").equals("") ? "无" : datamap.get("shiyi"));
        itemView.getShichen_ji().setText(datamap.get("shiji").equals("") ? "无" : datamap.get("shiji"));
        return convertView;
    }

    class ListItemView{
        private TextView shichen_key;
        private TextView shichen_name;
        private TextView shichen_time;
        private TextView shichen_chongsha;
        private TextView shichen_jixiong;
        private TextView shichen_caixi;
        private TextView shichen_yi;
        private TextView shichen_ji;

        public TextView getShichen_key() {
            return shichen_key;
        }

        public void setShichen_key(TextView shichen_key) {
            this.shichen_key = shichen_key;
        }

        public TextView getShichen_name() {
            return shichen_name;
        }

        public void setShichen_name(TextView shichen_name) {
            this.shichen_name = shichen_name;
        }

        public TextView getShichen_time() {
            return shichen_time;
        }

        public void setShichen_time(TextView shichen_time) {
            this.shichen_time = shichen_time;
        }

        public TextView getShichen_chongsha() {
            return shichen_chongsha;
        }

        public void setShichen_chongsha(TextView shichen_chongsha) {
            this.shichen_chongsha = shichen_chongsha;
        }

        public TextView getShichen_jixiong() {
            return shichen_jixiong;
        }

        public void setShichen_jixiong(TextView shichen_jixiong) {
            this.shichen_jixiong = shichen_jixiong;
        }

        public TextView getShichen_caixi() {
            return shichen_caixi;
        }

        public void setShichen_caixi(TextView shichen_caixi) {
            this.shichen_caixi = shichen_caixi;
        }

        public TextView getShichen_yi() {
            return shichen_yi;
        }

        public void setShichen_yi(TextView shichen_yi) {
            this.shichen_yi = shichen_yi;
        }

        public TextView getShichen_ji() {
            return shichen_ji;
        }

        public void setShichen_ji(TextView shichen_ji) {
            this.shichen_ji = shichen_ji;
        }
    }
}
