package com.sky.model.menu.clock.bell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontCheckBox;
import com.sky.plug.widget.IconFontTextview;
import com.sky.plug.widget.MarqueeTextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-9.
 */
public class MusicAdapter extends BaseAdapter {

    private List<Map<String, String>> mapList;
    private IconFontCheckBox checkBtn;
    private IconFontTextview musicIcon;
    private int specialColor, grayColor;
    boolean flag = false;
    private int tag = -1;
    private Context context;
    private String historyUrl = "";

    public MusicAdapter(Context context, List<Map<String, String>> mapList, String url) {
        this.context = context;
        this.mapList = mapList;
        //设置背景颜色
        specialColor = context.getResources().getColor(R.color.special);
        grayColor = context.getResources().getColor(R.color.black);
        historyUrl = url;
    }

    @Override
    public int getCount() {
        return mapList.size();
    }

    @Override
    public Object getItem(int position) {
        return mapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        LinearLayout linearLayout = (LinearLayout) convertView;
        if (linearLayout == null) {
            linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.bell_item, parent, false);
            holder = new ViewHolder();
            holder.checkbox = (IconFontCheckBox) linearLayout.findViewById(R.id.checkBtn);
            holder.musicIcon = (IconFontTextview) linearLayout.findViewById(R.id.musicIcon);
            holder.name = (MarqueeTextView) linearLayout.findViewById(R.id.bell_name);
            holder.size = (TextView) linearLayout.findViewById(R.id.bell_size);
            holder.url = (TextView) linearLayout.findViewById(R.id.bell_url);
            linearLayout.setTag(holder);
        } else {
            holder = (ViewHolder) linearLayout.getTag();
        }
        holder.name.setText(mapList.get(position).get("bell_name"));
        holder.size.setText(mapList.get(position).get("bell_size"));
        holder.url.setText(mapList.get(position).get("bell_url"));

        holder.checkbox.setTag(position);

        addListener(holder, position);//添加事件响应

        if (tag == -1) {
            if (historyUrl.equals(holder.url.getText().toString())) {
                tag = position;
            }
        }
        if (tag == position) {
            flag = true;
            holder.checkbox.setItemChecked(true);

        } else {
            flag = false;
            holder.checkbox.setItemChecked(false);
        }
        return linearLayout;
    }

    private void addListener(final ViewHolder holder, final int position) {
        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                // TODO Auto-generated method stub
                if(null!=checkBtn){
                    checkBtn.setItemChecked(false);
                }
                if(null!=musicIcon){
                    musicIcon.setTextColor(grayColor);
                }
                if (isChecked) {
                    flag = !flag;
                    checkBtn = holder.checkbox;
                    musicIcon = holder.musicIcon;
                    tag = position;
                    holder.musicIcon.setTextColor(specialColor);
                }
            }

        });
    }

    public class ViewHolder {
        private IconFontCheckBox checkbox;
        private IconFontTextview musicIcon;
        private MarqueeTextView name;
        private TextView size;
        private TextView url;
    }
}
