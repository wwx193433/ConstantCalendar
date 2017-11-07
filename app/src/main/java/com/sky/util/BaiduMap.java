package com.sky.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.sky.model.weather.WeatherManager;

/**
 * Created by Administrator on 17-10-2.
 */
public class BaiduMap {

    private LocationClient mLocationClient;
    private LocationListener locationListener;
    private Context context;
    public BaiduMap(Context context){
        this.context = context;
        /**
         * 百度SDK定位
         */
        mLocationClient = new LocationClient(context);
        locationListener = new LocationListener();
        mLocationClient.registerLocationListener(locationListener);
        initLocation();
    }

    public void locateStart(){
        if(mLocationClient!=null && !mLocationClient.isStarted()){
            mLocationClient.start();
        }
    }

    public void locateStop(){
        if(mLocationClient!=null && mLocationClient.isStarted()){
            mLocationClient.stop();
        }
    }

    void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
        option.setCoorType("all");
        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps
        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位
        mLocationClient.setLocOption(option);
    }

    class LocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            String areaName = "";
            if (location.getLocType() == BDLocation.TypeGpsLocation) {//GPS定位
                Log.i("tags", "GPS定位。。。。。。。。。。。。");
                areaName = location.getCity();
            }else if(location.getLocType() == BDLocation.TypeNetWorkLocation){//网络定位
                areaName = location.getCity();
                Log.i("tags", "网络定位。。。。。。。。。。。。");
            }else if (location.getLocType() == BDLocation.TypeServerError) {//网络定位失败
                Log.i("tags", "网络定位失败。。。。。。。。。。。。");
            }else if(location.getLocType() == BDLocation.TypeNetWorkException){//当前网络不通
                Log.i("tags", "当前网络不通。。。。。。。。。。。。");
            }else if (location.getLocType() == BDLocation.TypeCriteriaException) {//用户没有授权
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                Log.i("tags", "用户没有授权。。。。。。。。。。。。");
            }
            sendLocationBroadCast(areaName);
        }
    }

    /**
     * 发送位置广播
     * @param areaName
     */
    public void sendLocationBroadCast(String areaName){
        locateStop();
        Intent intent = new Intent(WeatherManager.LOCATION_BCR);
        intent.putExtra("areaName", areaName);
        context.sendBroadcast(intent);
    }
}
