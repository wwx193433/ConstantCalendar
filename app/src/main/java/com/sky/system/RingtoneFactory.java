package com.sky.system;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.sky.constantcalendar.R;
import com.sky.util.Constant;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 17-8-7.
 */
public class RingtoneFactory {

    private Context context;

    public static final String ALARM = "alarms";
    public static final String NOTIFICATION = "notifications";
    public static final String RINGTONE = "ringtones";

    public RingtoneFactory(Context context) {
        this.context = context;
    }

    public Ringtone getDefaultRingtone(int type) {
        return RingtoneManager.getRingtone(context, RingtoneManager.getActualDefaultRingtoneUri(context, type));
    }

    public Uri getDefaultRingtoneUri(int type) {
        return RingtoneManager.getActualDefaultRingtoneUri(context, type);
    }


    public List<Ringtone> getRingtoneList(int type) {
        List<Ringtone> resArr = new ArrayList<Ringtone>();
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            resArr.add(manager.getRingtone(i));
        }
        return resArr;
    }

    public Ringtone getRingtone(int type, int pos) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(type);
        return manager.getRingtone(pos);
    }


    public List<String> getRingtoneTitleList(int type) {
        List<String> resArr = new ArrayList<String>();
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(type);
        Cursor cursor = manager.getCursor();
        if (cursor.moveToFirst()) {
            do {
                resArr.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX));
            } while (cursor.moveToNext());
        }
        return resArr;
    }

    public String getRingtoneUriPath(int type, int pos, String def) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(type);
        Uri uri = manager.getRingtoneUri(pos);
        return uri == null ? def : uri.toString();
    }

    public List<Map<String, String>> getRingtoneData2() {
        List<Map<String, String>> datas = new ArrayList<>();
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(RingtoneManager.TYPE_ALL);
        Cursor cursor = manager.getCursor();
        int count = cursor.getCount();
        for (int i = 0; i < count; i++) {
            Map<String, String> map = new HashMap<>();
            map.put("bell_name", manager.getRingtone(i).getTitle(context));
            datas.add(map);
        }
        return datas;
    }

    public List<Map<String, String>> getRingtoneData() {

        List<Map<String, String>> datas = new ArrayList<>();
        Map<String, String> defaultData = getDefaultData();
        List<Map<String, String>> alarmData = getDataByType(ALARM);
        List<Map<String, String>> notificationData = getDataByType(NOTIFICATION);
        List<Map<String, String>> ringtoneData = getDataByType(RINGTONE);
        datas.add(defaultData);
        datas.addAll(alarmData);
        datas.addAll(notificationData);
        datas.addAll(ringtoneData);
        return datas;
    }

    private List<Map<String, String>> getDataByType(String type) {
        String url = "/system/media/audio/" + type;
        List<Map<String, String>> datas = new ArrayList<>();
        File[] files = new File(url).listFiles();
        for (File f : files) {
            Map<String, String> map = new HashMap<>();
            map.put("bell_name", f.getName().substring(0, f.getName().lastIndexOf(".")));
            map.put("bell_size", getSize(f.length()));
            map.put("bell_url", url + "/" + f.getName());
            datas.add(map);
        }
        return datas;
    }

    public String getSize(long fileByte){
        DecimalFormat df = new DecimalFormat("#0.00");
        String size = "0kb";
        if (fileByte < 1024) {
            size = df.format((double) fileByte) + "B";
        } else if (fileByte > 1024 && fileByte < 1024 * 1024) {
            size = df.format((double) fileByte / (1024)) + "K";
        } else if (fileByte > (1024 * 1024) && fileByte < 1024 * 1024 * 1024) {
            size = df.format((double) fileByte / (1024 * 1024)) + "M";
        } else {
            size = df.format((double) fileByte / (1024 * 1024 * 1024)) + "G";
        }
        return size;
    }


    public Ringtone getRingtoneByUriPath(int type, String uriPath) {
        RingtoneManager manager = new RingtoneManager(context);
        manager.setType(type);
        Uri uri = Uri.parse(uriPath);
        return manager.getRingtone(context, uri);
    }
    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time / 1000 % 60 < 10) {
            return time / 1000 / 60 + ":0" + time / 1000 % 60;

        } else {
            return time / 1000 / 60 + ":" + time / 1000 % 60;
        }

    }

    public List<Map<String,String>> getMusicData() {
        List<Map<String, String>> datas = new ArrayList<>();

        Map<String, String> defaultData = getDefaultData();
        datas.add(defaultData);
        int hasWriteExternalStoragePermission = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteExternalStoragePermission == PackageManager.PERMISSION_GRANTED) {
            // 媒体库查询语句（写一个工具类MusicUtils）
            Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null,
                    null, MediaStore.Audio.AudioColumns.IS_MUSIC);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String song = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                    long size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));
                    Map<String, String> map = new HashMap<>();
                    map.put("bell_name", song);
                    map.put("bell_size", getSize(size));
                    map.put("bell_url", path);
                    datas.add(map);
                }
                // 释放资源
                cursor.close();
            }
        }
        return datas;
    }

    public Map<String,String> getDefaultData() {
        String url = Constant.DEFAULT_RING_URL;
        InputStream is = context.getResources().openRawResource(R.raw.clock);
        Map<String, String> map = new HashMap<>();
        try {
            long bytes = is.available();
            String size = getSize(bytes);
            map.put("bell_name", Constant.DEFAULT_RING_NAME);
            map.put("bell_size", size);
            map.put("bell_url", url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }
}
