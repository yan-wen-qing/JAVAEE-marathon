package org.example.marathon.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Weather;
import org.example.marathon.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * 根据 event_id 获取所有天气信息
     */
    @GetMapping("/get_weather")
    public Result getWeather(@RequestParam("event_id") Integer eventId) {
        log.info("查询 event_id={} 的天气信息", eventId);
        List<Weather> weatherList = weatherService.getWeatherByEventId(eventId);
        return weatherList != null && !weatherList.isEmpty()
                ? Result.success(weatherList)
                : Result.error("未找到对应的天气信息");
    }

    /**
     * 插入或更新天气信息（upsert）
     */
    @PostMapping("/upsert_weather")
    public Result upsertWeather(@RequestBody Weather weather) {
        log.info("插入或更新天气信息: {}", weather);
        boolean isUpserted = weatherService.upsertWeather(weather);
        return isUpserted ? Result.success() : Result.error("天气信息更新失败");
    }
}
