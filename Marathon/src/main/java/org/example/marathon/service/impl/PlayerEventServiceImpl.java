package org.example.marathon.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.po.Event;
import org.example.marathon.domain.po.Participate;
import org.example.marathon.domain.vo.EventVO;
import org.example.marathon.domain.vo.ResultVO;
import org.example.marathon.mapper.EventMapper;
import org.example.marathon.mapper.ParticipateMapper;
import org.example.marathon.service.PlayerEventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlayerEventServiceImpl implements PlayerEventService {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    private ParticipateMapper participateMapper;

    @Override
    public List<EventVO> getEventsByPlayerId(Integer playerId) {
        LambdaQueryWrapper<Participate> participateWrapper = new LambdaQueryWrapper<>();
        participateWrapper.eq(Participate::getPlayerId, playerId);
        List<Participate> participates = participateMapper.selectList(participateWrapper);

        return participates.stream()
                .map(participate -> {
                    Event event = eventMapper.selectById(participate.getEventId());
                    if (event == null) {
                        return null;
                    }

                    EventVO vo = new EventVO();
                    vo.setId(event.getId());
                    vo.setName(event.getName());
                    vo.setEventDate(event.getEventDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                    vo.setCategory(event.getCategory());

                    ResultVO resultVO = new ResultVO();
                    if (!event.getIsDrawn()) {
                        resultVO.setResponse("尚未抽签");
                        resultVO.setEventNumber(null);
                    } else {
                        String number = participate.getNumber();
                        if (number != null && !number.isEmpty()) {
                            resultVO.setResponse("已中签");
                            resultVO.setEventNumber(number);
                        } else {
                            resultVO.setResponse("未中签");
                            resultVO.setEventNumber(null);
                        }
                    }
                    vo.setResult(resultVO);

                    return vo;
                })
                .filter(vo -> vo != null)
                .collect(Collectors.toList());
    }

    @Override
    public String getEventResult(Integer playerId, Integer eventId) {
        LambdaQueryWrapper<Participate> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Participate::getPlayerId, playerId)
                .eq(Participate::getEventId, eventId);
        Participate participate = participateMapper.selectOne(wrapper);

        if (participate == null) {
            return "未报名";
        }

        Event event = eventMapper.selectById(eventId);
        if (event == null || !event.getIsDrawn()) {
            return "尚未抽签";
        }

        return participate.getNumber() == null ? "未中签" : participate.getNumber();
    }
}