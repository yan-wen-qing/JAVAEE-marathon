package org.example.marathon.service;

import org.example.marathon.domain.vo.PlayerResultVO;

import java.util.List;

public interface PlayerResultService {
    /**
     * 获取用户所有比赛成绩
     * @param playerId 用户ID
     * @return 成绩列表
     */
    List<PlayerResultVO> getHistoryResults(Integer playerId);

    /**
     * 根据比赛类型获取用户成绩
     * @param playerId 用户ID
     * @param category 比赛类型
     * @return 成绩列表
     */
    List<PlayerResultVO> getResultsByCategory(Integer playerId, String category);
}