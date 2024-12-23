package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.po.Event;
import org.example.marathon.domain.po.Result;
import org.example.marathon.domain.vo.PlayerResultVO;
import org.example.marathon.mapper.EventMapper;
import org.example.marathon.mapper.ResultMapper;
import org.example.marathon.service.PlayerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerResultServiceImpl implements PlayerResultService {

    @Autowired
    private ResultMapper resultMapper;
    
    @Autowired
    private EventMapper eventMapper;

    @Override
    public List<PlayerResultVO> getHistoryResults(Integer playerId) {
        return getResultsByCategory(playerId, null);
    }

    @Override
    public List<PlayerResultVO> getResultsByCategory(Integer playerId, String category) {
        LambdaQueryWrapper<Result> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Result::getPlayerId, playerId);
        List<Result> results = resultMapper.selectList(wrapper);

        return results.stream()
            .map(result -> {
                Event event = eventMapper.selectById(result.getEventId());
                if (event == null || (category != null && !category.equals(event.getCategory()))) {
                    return null;
                }

                return new PlayerResultVO(
                    event.getName(),
                    result.getGunResult(),
                    result.getNetResult(),
                    result.getGenderRank(),
                    result.getPlayerRank()
                );
            })
            .filter(vo -> vo != null)
            .collect(Collectors.toList());
    }
} 