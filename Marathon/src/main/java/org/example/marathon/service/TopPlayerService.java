package org.example.marathon.service;

import org.example.marathon.domain.po.Result;

import java.util.List;

public interface TopPlayerService {
    List<Result> getTopPlayers(Integer eventId, Integer topN, String gender, String sortOrder);
}
