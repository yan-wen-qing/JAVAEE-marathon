package org.example.marathon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.marathon.domain.po.Player;

import java.util.List;

@Mapper
public interface PlayerMapper extends BaseMapper<Player> {

}
