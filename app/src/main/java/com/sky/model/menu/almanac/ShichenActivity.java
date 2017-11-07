package com.sky.model.menu.almanac;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontTextview;

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
    private IconFontTextview iconBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shichen);
        initViews();

        Map<String, Map<String, String>> shichenmap = am.getShichenMap();
        getData(shichenmap);
        adapter = new ShichenAdapter(this, dataList);
        scListView.setAdapter(adapter);

        iconBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
        iconBackBtn = (IconFontTextview) this.findViewById(R.id.iconBackBtn);
        am = (Almanac) getIntent().getSerializableExtra("almanac");
    }


}
