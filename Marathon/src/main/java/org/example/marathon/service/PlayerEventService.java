package org.example.marathon.service;

import org.example.marathon.domain.vo.EventVO;
import java.util.List;

public interface PlayerEventService {
    /**
     * 获取用户报名的所有赛事
     * @param playerId 用户ID
     * @return 赛事列表
     */
    List<EventVO> getEventsByPlayerId(Integer playerId);

    /**
     * 获取用户在某个赛事的抽签结果
     * @param playerId 用户ID
     * @param eventId 赛事ID
     * @return 抽签结果
     */
    String getEventResult(Integer playerId, Integer eventId);
} 