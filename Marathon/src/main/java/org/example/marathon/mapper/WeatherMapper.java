package org.example.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.marathon.domain.po.Weather;

@Mapper
public interface WeatherMapper extends BaseMapper<Weather> {
    // 使用 BaseMapper 提供的 CRUD 方法
}
