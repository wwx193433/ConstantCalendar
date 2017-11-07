package com.sky.model.weather;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.sky.db.DbHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 17-10-11.
 */
public class WeatherDao implements WeatherService{
    private Context context;

    private DbHelper helper = null;

    public WeatherDao(Context context){
        this.context = context;
        helper = new DbHelper(context);
    }

    //查询
    @Override
    public List<City> queryAllCitys() {
        List<City> citys = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_weather_city";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String pinyin = cursor.getString(cursor.getColumnIndex("area_name_pinyin"));
                String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                String areaId = cursor.getString(cursor.getColumnIndex("area_id"));
                String areaName = cursor.getString(cursor.getColumnIndex("area_name"));
                String provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                String weatherId = cursor.getString(cursor.getColumnIndex("weather_id"));
                City city = new City(id, pinyin, cityName, areaId, areaName, provinceName, weatherId);
                citys.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close(database);
        }
        return citys;
    }

    @Override
    public List<City> queryHotCitys() {
        List<City> citys = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_hot_city";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String areaId = cursor.getString(cursor.getColumnIndex("area_id"));
                String areaName = cursor.getString(cursor.getColumnIndex("area_name"));
                String weatherId = cursor.getString(cursor.getColumnIndex("weather_id"));
                City city = new City();
                city.setId(id);
                city.setAreaId(areaId);
                city.setAreaName(areaName);
                city.setWeatherId(weatherId);
                citys.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close(database);
        }
        return citys;
    }

    @Override
    public List<City> searchCitysByKeyword(String keyword) {
        List<City> citys = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_weather_city where area_name like '"+keyword+"%' or area_name_pinyin like '"+keyword+"%'";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String areaId = cursor.getString(cursor.getColumnIndex("area_id"));
                String areaName = cursor.getString(cursor.getColumnIndex("area_name"));
                String weatherId = cursor.getString(cursor.getColumnIndex("weather_id"));
                String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                String provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                City city = new City();
                city.setId(id);
                city.setCityName(cityName);
                city.setProvinceName(provinceName);
                city.setAreaId(areaId);
                city.setAreaName(areaName);
                city.setWeatherId(weatherId);
                citys.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close(database);
        }
        return citys;
    }

    @Override
    public boolean saveMySelectedCity(String areaId) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "insert into t_my_city select null, area_id, area_name, weather_id from t_weather_city where t_weather_city.area_id = '"+areaId+"' and t_weather_city.area_id not in(select area_id from t_my_city)";
            database = helper.getWritableDatabase();
            database.execSQL(sql);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close(database);
        }
        return flag;
    }

    @Override
    public List<City> getMySelectedCitys() {
        List<City> citys = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_my_city";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, null);
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String areaId = cursor.getString(cursor.getColumnIndex("area_id"));
                String areaName = cursor.getString(cursor.getColumnIndex("area_name"));
                String weatherId = cursor.getString(cursor.getColumnIndex("weather_id"));
                City city = new City();
                city.setId(id);
                city.setAreaId(areaId);
                city.setAreaName(areaName);
                city.setWeatherId(weatherId);
                citys.add(city);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close(database);
        }
        return citys;
    }

    @Override
    public boolean deleteMyCityByWeatherId(String weatherId) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from t_my_city where weather_id = '"+weatherId+"'";
            database = helper.getWritableDatabase();
            database.execSQL(sql);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            helper.close(database);
        }
        return flag;
    }

    @Override
    public City queryCityByName(String name) {
        String tempName = "";
        if(name.endsWith("市")){
            tempName = name.substring(0,name.length() -1 );
        }
        City city = new City();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from t_weather_city where area_name = ?";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, new String[]{""+tempName});
            while(cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String provinceName = cursor.getString(cursor.getColumnIndex("province_name"));
                String cityName = cursor.getString(cursor.getColumnIndex("city_name"));
                String areaName = cursor.getString(cursor.getColumnIndex("area_name"));
                String areaId = cursor.getString(cursor.getColumnIndex("area_id"));
                String weatherId = cursor.getString(cursor.getColumnIndex("weather_id"));
                city.setId(id);
                city.setProvinceName(provinceName);
                city.setCityName(cityName);
                city.setAreaId(areaId);
                city.setAreaName(areaName);
                city.setWeatherId(weatherId);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally{
            helper.close(database);
        }
        return city;
    }
}
