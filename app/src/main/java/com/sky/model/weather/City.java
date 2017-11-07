package com.sky.model.weather;

/**
 * Created by Administrator on 17-10-11.
 */
public class City {
    private String id;
    private String pinyin;
    private String cityName;
    private String areaId;
    private String areaName;
    private String provinceName;
    private String weatherId;

    public City(String id, String pinyin, String cityName, String areaId, String areaName, String provinceName, String weatherId) {
        this.id = id;
        this.pinyin = pinyin;
        this.cityName = cityName;
        this.areaId = areaId;
        this.areaName = areaName;
        this.provinceName = provinceName;
        this.weatherId = weatherId;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", pinyin='" + pinyin + '\'' +
                ", cityName='" + cityName + '\'' +
                ", areaId='" + areaId + '\'' +
                ", areaName='" + areaName + '\'' +
                ", provinceName='" + provinceName + '\'' +
                ", weatherId='" + weatherId + '\'' +
                '}';
    }

    public City(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public String getDetailAreaName() {
        String detailAreaName = this.getAreaName();
        if(!this.getAreaName().equals(this.getCityName())){
            detailAreaName += " - "+this.getCityName();
        }
        if(!this.getCityName().equals(this.getProvinceName())){
            detailAreaName += " - "+this.getProvinceName();
        }
        return detailAreaName;
    }

    public String getLocationAddr(){
        String detailAreaName = this.getAreaName();
        if(!this.getAreaName().equals(this.getCityName())){
            detailAreaName += "."+this.getCityName();
        }
        if(!this.getCityName().equals(this.getProvinceName())){
            detailAreaName += "."+this.getProvinceName();
        }
        return detailAreaName;
    }
}
