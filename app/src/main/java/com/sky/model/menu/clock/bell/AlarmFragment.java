package com.sky.model.menu.clock.bell;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sky.constantcalendar.R;
import com.sky.plug.widget.IconFontCheckBox;
import com.sky.plug.widget.MarqueeTextView;
import com.sky.system.RingtoneFactory;
import com.sky.util.Constant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-7.
 */
public class AlarmFragment extends Fragment {

    private ListView alarmview;
    private RingtoneFactory ringtoneFactory;
    private IconFontCheckBox checkBtn;
    private String  url, name;
    private int currentPosition = -1;

    private DataInterface dataInterface;
    //空白页面
    private LinearLayout empty;

    //音乐player
    private MediaPlayer player;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        //从activity传过来的Bundle
        Bundle bundle = getArguments();

        empty = (LinearLayout) view.findViewById(R.id.empty);
        alarmview = (ListView) view.findViewById(R.id.alarmview);
        ringtoneFactory = new RingtoneFactory(getActivity());

        List<Map<String, String>> data = ringtoneFactory.getRingtoneData();
        if (data.size() == 0) {
            empty.setVisibility(View.VISIBLE);
        } else {
            empty.setVisibility(View.GONE);
        }

        //判断歌曲是否存在
        url = bundle.getString("url");
        int p = checkExistItem(data, url);
        if(p<0){//如果不存在使用默认
            url = Constant.DEFAULT_RING_URL;
            name = Constant.DEFAULT_RING_NAME;
        }

        AlarmAdapter adapter = new AlarmAdapter(getActivity(), data, url);
        alarmview.setAdapter(adapter);

        if(p>0){
            //移动到选中位置
            alarmview.setSelection(p);
        }

        alarmview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                url = ((TextView) view.findViewById(R.id.bell_url)).getText().toString();

                //如果点击第二次则停止播放
                if (currentPosition == position && null != player && player.isPlaying()) {
                    player.stop();
                    return;
                }
                currentPosition = position;
                //选中条目
                checkBtn = (IconFontCheckBox) view.findViewById(R.id.checkBtn);
                checkBtn.setItemChecked(true);

                //播放音乐
                play(url);
                //回传选中的数据
                MarqueeTextView bellTextview = (MarqueeTextView) view.findViewById(R.id.bell_name);
                name = bellTextview.getText().toString();
            }
        });
    }

    /**
     * 检查歌曲是否存在
     * @param data
     * @param url
     * @return
     */
    private int checkExistItem(List<Map<String, String>> data, String url) {
        for(int i = 0;i<data.size();i++){
            if(data.get(i).get("bell_url").equals(url)){
                name = data.get(i).get("bell_name");
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (null != player) {
            player.stop();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != player) {
            player.stop();
        }
    }

    /**
     * 铃声播放
     *
     * @param url
     */
    public void play(String url) {
        if (null == player) {
            player = MediaPlayer.create(getActivity(), Uri.parse(url));
        }
        try {
            if (player.isPlaying()) {
                player.stop();
            }
            player.reset();
            player.setDataSource(getContext(), Uri.parse(url));//重新设置要播放的音频
            player.prepare();//预加载音频

        } catch (IOException e) {
            e.printStackTrace();
        }
        player.start();//开始播放
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                try {
                    mp.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onCheckData(DataInterface dataInterface){
        this.dataInterface = dataInterface;
    }

    public void sendData() {
        dataInterface.onDataCheckCallBack(name, url);
    }

    public interface DataInterface{
        void onDataCheckCallBack(String name, String url);
    }
}
