package org.example.marathon.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.example.marathon.domain.po.Participate;

import java.util.List;
import java.util.Map;

public interface ParticipateService extends IService<Participate> {
    void drawLots(Integer eventId, Integer totalParticipants);
    void submitPacers(Integer eventId, List<Integer> pacerIds);
    void submitAids(Integer eventId, List<Integer> aidIds);
    List<Map<String, Object>> getParticipantsByEventId(Integer eventId);
    List<Map<String, Object>> getPlayerHistory(Integer playerId);
}
