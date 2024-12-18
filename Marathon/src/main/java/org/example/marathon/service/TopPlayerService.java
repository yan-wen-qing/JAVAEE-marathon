package org.example.marathon.service;

import org.example.marathon.domain.po.PlayerResult;

import java.util.List;

public interface TopPlayerService {
    List<PlayerResult> getTopPlayers(Integer eventId, Integer topN, String gender,String sortOrder);
}
