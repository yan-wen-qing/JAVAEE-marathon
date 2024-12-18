package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Weather;
import org.example.marathon.mapper.WeatherMapper;
import org.example.marathon.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherServiceImpl extends ServiceImpl<WeatherMapper, Weather> implements WeatherService {

    @Autowired
    private WeatherMapper weatherMapper;

    /**
     * 根据 event_id 获取所有天气信息
     */
    @Override
    public List<Weather> getWeatherByEventId(Integer eventId) {
        QueryWrapper<Weather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", eventId);
        return weatherMapper.selectList(queryWrapper);
    }

    /**
     * 插入或更新天气信息
     */
    @Override
    public boolean upsertWeather(Weather weather) {
        QueryWrapper<Weather> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("event_id", weather.getEventId())
                .eq("time", weather.getTime());

        // 检查是否存在记录
        Weather existingWeather = weatherMapper.selectOne(queryWrapper);
        if (existingWeather != null) {
            // 更新已有记录
            weather.setEventId(existingWeather.getEventId());
            weather.setTime(existingWeather.getTime());
            return weatherMapper.update(weather, queryWrapper) > 0;
        } else {
            // 插入新记录
            return weatherMapper.insert(weather) > 0;
        }
    }
}
