package com.sky.model.menu.almanac;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.sky.constantcalendar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-26.
 */
public class ShichenActivity extends Activity {

    private ListView scListView;
    private Almanac am;
    private ShichenAdapter adapter;
    private List<Map<String, String>> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shichen);
        initViews();

        Map<String, Map<String, String>> shichenmap = am.getShichenMap();
        getData(shichenmap);
        adapter = new ShichenAdapter(this, dataList);
        scListView.setAdapter(adapter);
    }

    private void getData(Map<String, Map<String, String>> shichenmap) {
        dataList = new ArrayList<>();
        dataList.add(shichenmap.get("zishi"));
        dataList.add(shichenmap.get("choushi"));
        dataList.add(shichenmap.get("yinshi"));
        dataList.add(shichenmap.get("maoshi"));
        dataList.add(shichenmap.get("chenshi"));
        dataList.add(shichenmap.get("sishi"));
        dataList.add(shichenmap.get("wushi"));
        dataList.add(shichenmap.get("weishi"));
        dataList.add(shichenmap.get("shenshi"));
        dataList.add(shichenmap.get("youshi"));
        dataList.add(shichenmap.get("xushi"));
        dataList.add(shichenmap.get("haishi"));
    }

    private void initViews() {
        scListView = (ListView) this.findViewById(R.id.scListView);
        am = (Almanac) getIntent().getSerializableExtra("almanac");
    }


}
