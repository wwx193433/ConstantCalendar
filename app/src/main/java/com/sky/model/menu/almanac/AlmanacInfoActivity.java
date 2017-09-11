package com.sky.model.menu.almanac;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.sky.constantcalendar.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-26.
 */
public class AlmanacInfoActivity extends Activity{

    private ListView atListView;
    private List<AlmanacInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.annotation);

        initViews();

        getData();
    }

    private void initViews() {
        data = new ArrayList<>();
        atListView = (ListView) this.findViewById(R.id.atListView);
        AnnotationAdapter adapter = new AnnotationAdapter(this, data);
        atListView.setAdapter(adapter);
    }

    public void getData() {
        AlmanacInfo almanacInfo = new AlmanacInfo();
        almanacInfo.setName("宜");
        almanacInfo.setType(0);
        List<Map<String, String>> items = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("key", "嫁娶：");
        map.put("value", "惺惺惜惺惺想寻寻寻寻寻寻寻寻寻");
        items.add(map);
        Map<String, String> map1 = new HashMap<>();
        map1.put("key", "嫁娶：");
        map1.put("value", "惺惺惜惺惺想寻寻寻寻寻寻寻寻寻");
        items.add(map1);
        Map<String, String> map2 = new HashMap<>();
        map2.put("key", "嫁娶：");
        map2.put("value", "惺惺惜惺惺想寻寻寻寻寻寻寻寻寻");
        items.add(map2);
        almanacInfo.setItems(items);
        data.add(almanacInfo);
        AlmanacInfo almanacInfo2 = new AlmanacInfo();
        almanacInfo2.setName("忌");
        almanacInfo2.setType(1);
        List<Map<String, String>> items2 = new ArrayList<>();
        Map<String, String> map4 = new HashMap<>();
        map4.put("key", "嫁娶：");
        map4.put("value", "惺惺惜惺惺想寻寻寻寻寻寻寻寻寻");
        items2.add(map4);
        Map<String, String> map5 = new HashMap<>();
        map5.put("key", "嫁娶：");
        map5.put("value", "惺惺惜惺惺想寻寻寻寻寻寻寻寻寻");
        items2.add(map5);
        Map<String, String> map6 = new HashMap<>();
        map6.put("key", "嫁娶：");
        map6.put("value", "惺惺惜惺惺想寻寻寻寻寻寻寻寻寻");
        items2.add(map6);
        almanacInfo2.setItems(items2);
        data.add(almanacInfo2);
    }
}
