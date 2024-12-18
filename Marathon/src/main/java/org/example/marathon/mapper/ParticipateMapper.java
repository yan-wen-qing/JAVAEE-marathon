package org.example.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.marathon.domain.po.Participate;

@Mapper
public interface ParticipateMapper extends BaseMapper<Participate> {
    // 基于BaseMapper提供的基础CRUD功能
}
