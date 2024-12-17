package org.example.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.marathon.domain.po.Event;

@Mapper
public interface EventMapper extends BaseMapper<Event> {
}
