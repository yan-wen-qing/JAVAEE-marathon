package org.example.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.marathon.domain.po.Event;

@Mapper
public interface EventMapper extends BaseMapper<Event> {
    // BaseMapper 提供了基本的 CRUD 功能
}
