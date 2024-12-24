package org.example.marathon.service.impl;

import org.example.marathon.domain.po.Result;
import org.example.marathon.mapper.TopPlayerMapper;
import org.example.marathon.service.TopPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopPlayerServiceImpl implements TopPlayerService {

    @Autowired
    private TopPlayerMapper topPlayerMapper;

    @Override
    public List<Result> getTopPlayers(Integer eventId, Integer topN, String gender,String sortOrder) {
        return topPlayerMapper.getTopPlayers(eventId, topN, gender,sortOrder);
    }
}
