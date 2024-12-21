package org.example.marathon.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.example.marathon.domain.po.Participate;

import java.util.List;
import java.util.Map;

public interface ParticipateService extends IService<Participate> {
    void drawLots(Integer eventId, Integer totalParticipants);
    void submitPlayers(Integer eventId, List<Integer> playerIds, String role, String chosenField);
    List<Map<String, Object>> getParticipantsByEventId(Integer eventId);
    List<Map<String, Object>> getPlayerHistory(Integer playerId);
}
