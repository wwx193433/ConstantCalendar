package com.sky.plug.wheel.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class WheelTextAdapter extends AbstractWheelTextAdapter {
    private List<String> list;

    public WheelTextAdapter(Context context, List<String> list) {
        super(context);
        this.list = list;
    }

    @Override
    public void setStyle(String font, int maxsize, int minsize) {
        super.setStyle(font, maxsize, minsize);
    }

    @Override
    public void setCurrentIndex(int currentIndex) {
        super.setCurrentIndex(currentIndex);
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view = super.getItem(index, cachedView, parent);
        return view;
    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public CharSequence getItemText(int index) {
        return list.get(index) + "";
    }
}