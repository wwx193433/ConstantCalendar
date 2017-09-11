package com.sky.model.menu.almanac;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-9-4.
 */
public class AnnotationAdapter extends BaseAdapter {

    private Context context;
    private List<AlmanacInfo> data;
    private LayoutInflater inflater;
    private LayoutInflater inflater2;
    public AnnotationAdapter(Context context, List<AlmanacInfo> data){
        this.context = context;
        this.data = data;
        this.inflater = LayoutInflater.from(context);
        this.inflater2 = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 父view
        View view = inflater.inflate(R.layout.annotion_item, parent, false);

        //名词解释
        AlmanacInfo almanacInfo = data.get(position);

        int type= almanacInfo.getType();
        TextView ano_name = (TextView) view.findViewById(R.id.ano_name);
        TextView ano_yi = (TextView) view.findViewById(R.id.ano_yi);
        TextView ano_ji = (TextView) view.findViewById(R.id.ano_ji);
        switch(type){
            case 0:
                ano_name.setText(" 宜忌");
                ano_yi.setVisibility(View.VISIBLE);
                break;
            case 1:
                ano_name.setVisibility(View.GONE);
                ano_ji.setVisibility(View.VISIBLE);
                break;
            default:
                ano_name.setText(" "+almanacInfo.getName());
                break;
        }

        List<Map<String, String>> itemList = almanacInfo.getItems();
        LinearLayout ano_items = (LinearLayout) view.findViewById(R.id.ano_items);
        ano_items.removeAllViews();
        for(int i = 0;i<itemList.size();i++){
            // 子view
            View item = inflater2.inflate(R.layout.almanac_item, null, false);
            TextView ano_item_key = (TextView) item.findViewById(R.id.ano_item_key);
            TextView ano_item_value = (TextView) item.findViewById(R.id.ano_item_value);
            if(type == 0){
                ano_item_key.setTextColor(Color.parseColor("#008000"));
            }else if(type==1){
                ano_item_key.setTextColor(Color.parseColor("#E62119"));
            }
            ano_item_key.setText(itemList.get(i).get("key"));
            ano_item_value.setText(itemList.get(i).get("value"));
            ano_items.addView(item);
        }
        return view;
    }
}
