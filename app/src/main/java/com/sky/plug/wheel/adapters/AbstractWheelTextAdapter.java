package com.sky.plug.wheel.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public abstract class AbstractWheelTextAdapter extends AbstractWheelAdapter {

    protected Context context;
    private int currentIndex = 0;
    private String font = "";
    private int maxsize = 24;
    private int minsize = 14;
    private ArrayList<View> arrayList = new ArrayList<>();
    private int visibleItems = 5;
    private int itemHeight = 50;
    Typeface thinFont = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL);;

    protected AbstractWheelTextAdapter(Context context) {
        this.context = context;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    public void setVisibleItems(int visibleItems) {
        this.visibleItems = visibleItems == 0 ? 5 : visibleItems;
    }

    public void setStyle(String font, int maxsize, int minsize) {
        this.font = font;
        this.maxsize = maxsize;
        this.minsize = minsize;
        if(null!=font && !font.equals("")){
            thinFont = Typeface.create(font, Typeface.NORMAL);
        }
    }

    public ArrayList<View> getTestViews() {
        return arrayList;
    }
    protected abstract CharSequence getItemText(int index);

    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {


        TextView textView = new TextView(context);
        if (!arrayList.contains(textView)) {
            arrayList.add(textView);
        }
        if(parent.getHeight()>0 && visibleItems>0){
            itemHeight = parent.getHeight() / visibleItems;
        }
        textView.setHeight(itemHeight);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getItemText(index));
        textView.setTypeface(thinFont);
        textView.setTextColor(Color.BLACK);
        if (index == currentIndex) {
            textView.setTextSize(maxsize);
        } else {
            textView.setTextSize(minsize);
        }
        return textView;
    }
}
