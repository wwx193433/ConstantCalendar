package com.sky.model.weather;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontButton;
import com.sky.plug.widget.IconFontTextview;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-10-11.
 */
public class CityAdapter extends BaseAdapter {
    private WeatherService weatherService;
    private Context context;
    private List<Map<String, Object>> weatherData;
    private boolean enableDeleteButton = false;
    public CityAdapter(Context context, List<Map<String, Object>> weatherData){
        this.context = context;
        this.weatherData = weatherData;
        weatherService = new WeatherDao(context);
    }

    public void setEnableDeleteButton(boolean enableDeleteButton) {
        this.enableDeleteButton = enableDeleteButton;
    }

    @Override
    public int getCount() {
        return weatherData.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if(null==convertView){
            convertView = LayoutInflater.from(context).inflate(R.layout.city_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.city_type_icon = (IconFontTextview) convertView.findViewById(R.id.city_type_icon);
            viewHolder.city_type = (TextView) convertView.findViewById(R.id.city_type);
            viewHolder.city_name = (TextView) convertView.findViewById(R.id.city_name);
            viewHolder.city_wendu = (TextView) convertView.findViewById(R.id.city_wendu);
            viewHolder.cityDeleteBtn = (IconFontButton) convertView.findViewById(R.id.cityDeleteBtn);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.city_type_icon.setText((Integer) weatherData.get(position).get("type_icon"));
        viewHolder.city_type.setText(weatherData.get(position).get("city_type").toString());
        viewHolder.city_name.setText(weatherData.get(position).get("city_name").toString());
        viewHolder.city_wendu.setText(weatherData.get(position).get("city_wendu").toString());
        if(!enableDeleteButton){
            viewHolder.cityDeleteBtn.setVisibility(View.GONE);
        }else{
            viewHolder.cityDeleteBtn.setVisibility(View.VISIBLE);
        }
        viewHolder.cityDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String weatherId = (String) weatherData.get(position).get("weather_id");
                if(weatherData.size()<2){
                    new  AlertDialog.Builder(context).setTitle("友情提示").setMessage("亲，你总不能删的一个都不剩吧").setPositiveButton("确定",null).show();
                    return;
                }
                boolean flag = weatherService.deleteMyCityByWeatherId(weatherId);
                if(flag){
                    weatherData.remove(position);
                    notifyDataSetChanged();
                }
            }
        });
        return convertView;
    }

    class ViewHolder{
        IconFontTextview city_type_icon;
        TextView city_type;
        TextView city_name;
        TextView city_wendu;
        IconFontButton cityDeleteBtn;
    }
}
