package com.sky.model.menu.almanac;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-26.
 */
public class AlmanacInfo {
    //类型
    private int type;
    private String name;
    private List<Map<String, String>> items;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, String>> items) {
        this.items = items;
    }
}
