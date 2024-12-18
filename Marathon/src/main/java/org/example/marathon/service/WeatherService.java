package org.example.marathon.service;

import org.example.marathon.domain.po.Weather;

import java.util.List;

public interface WeatherService {
    List<Weather> getWeatherByEventId(Integer eventId); // 根据 event_id 查询天气信息
    boolean upsertWeather(Weather weather);            // 插入或更新天气信息
}
