package com.sky.model.weather;

import java.util.List;

/**
 * Created by Administrator on 17-10-11.
 */
public interface WeatherService {

    List<City> queryAllCitys();

    List<City> queryHotCitys();

    List<City> searchCitysByKeyword(String keyword);

    boolean saveMySelectedCity(String areaId);

    List<City> getMySelectedCitys();

    boolean deleteMyCityByWeatherId(String weatherId);

    City queryCityByName(String name);
}
