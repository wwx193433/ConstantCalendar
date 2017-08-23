package com.sky.model.menu.clock;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sky.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-8-11.
 */
public class ClockDao implements ClockService {

    private DbHelper helper = null;

    public ClockDao(Context context) {
        // TODO Auto-generated constructor stub
        helper = new DbHelper(context);
    }

    @Override
    public boolean saveGetupData(Clock clock) {
        // TODO Auto-generated method stub
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "insert into t_clock_getup(title, time, sleep, cycle, bell, type, triggertime)"
                    + " values(?, ?, ?, ?, ?, ?, ?)";
            database = helper.getWritableDatabase();
            Object[] params = {clock.getTitle(), clock.getTime(), clock.getSleep(), clock.getCycle(), clock.getBell(), clock.getBellType(), clock.getTriggerTime()};
            database.execSQL(sql, params);
            Cursor cursor =database.rawQuery("select last_insert_rowid() from t_clock_getup", null);
            if(cursor.moveToFirst()){
                int id = cursor.getInt(0);
                clock.setId(id);
            }
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            helper.close(database);
        }
        return flag;
    }

    @Override
    public boolean deleteGetupById(int cgId) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from t_clock_getup where id = ?";
            database = helper.getWritableDatabase();
            Object[] params = {cgId};
            database.execSQL(sql, params);
            flag = true;
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            helper.close(database);
        }
        return flag;
    }

    @Override
    public List<Clock> queryGetupData() {
        List<Clock> clockList = new ArrayList<Clock>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_clock_getup";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while(cursor.moveToNext()){
                Clock clock = new Clock();
                clock.setId(cursor.getInt(cursor.getColumnIndex("id")));
                clock.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                clock.setTime(cursor.getString(cursor.getColumnIndex("time")));
                clock.setSleep(cursor.getString(cursor.getColumnIndex("sleep")));
                clock.setCycle(cursor.getString(cursor.getColumnIndex("cycle")));
                clock.setBell(cursor.getString(cursor.getColumnIndex("bell")));
                clock.setBellType(cursor.getString(cursor.getColumnIndex("type")));
                clock.setTriggerTime(cursor.getLong(cursor.getColumnIndex("triggertime")));
                clockList.add(clock);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            helper.close(database);
        }
        return clockList;
    }

    @Override
    public Clock queryClockById(int id) {
        Clock clock = new Clock();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_clock_getup where id = ?";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, new String[]{""+id});
            while(cursor.moveToNext()){
                clock.setId(cursor.getInt(cursor.getColumnIndex("id")));
                clock.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                clock.setTime(cursor.getString(cursor.getColumnIndex("time")));
                clock.setSleep(cursor.getString(cursor.getColumnIndex("sleep")));
                clock.setCycle(cursor.getString(cursor.getColumnIndex("cycle")));
                clock.setBell(cursor.getString(cursor.getColumnIndex("bell")));
                clock.setBellType(cursor.getString(cursor.getColumnIndex("type")));
                clock.setTriggerTime(cursor.getLong(cursor.getColumnIndex("triggertime")));
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            helper.close(database);
        }
        return clock;
    }

    @Override
    public void updateClock(Clock clock) {
        // TODO Auto-generated method stub
        SQLiteDatabase database = null;
        try {
            String sql = "update t_clock_getup set triggertime = ? where id = ?";
            database = helper.getWritableDatabase();
            Object[] params = {clock.getTriggerTime(), clock.getId()};
            database.execSQL(sql, params);
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            helper.close(database);
        }
    }
}
