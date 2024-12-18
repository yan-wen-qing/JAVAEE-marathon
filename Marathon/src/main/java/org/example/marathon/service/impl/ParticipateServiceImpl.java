package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Participate;
import org.example.marathon.mapper.ParticipateMapper;
import org.example.marathon.service.ParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ParticipateServiceImpl extends ServiceImpl<ParticipateMapper, Participate> implements ParticipateService {

    @Autowired
    private ParticipateMapper participateMapper;

    /**
     * 添加报名：检查重复报名
     */
    @Override
    public boolean addParticipate(Participate participate) {
        QueryWrapper<Participate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("player_id", participate.getPlayerId())
                .eq("event_id", participate.getEventId());

        // 检查是否存在记录，防止重复报名
        Participate existing = this.getOne(queryWrapper);
        if (existing != null) {
            log.warn("玩家ID={} 已报名赛事ID={}，不能重复报名", participate.getPlayerId(), participate.getEventId());
            return false;
        }

        return this.save(participate);
    }

    /**
     * 取消报名：根据 playerId 和 eventId 删除记录
     */
    @Override
    public boolean deleteParticipate(Integer playerId, Integer eventId) {
        QueryWrapper<Participate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("player_id", playerId)
                .eq("event_id", eventId);

        return this.remove(queryWrapper);
    }
}
