package org.example.marathon.service;

import org.example.marathon.domain.po.Event;

import java.util.List;

public interface EventService {
    Integer addEvent(Event event);       // 添加赛事
    List<Event> getAllEvents();          // 获取所有赛事
    Event getEventById(Integer id);      // 根据ID获取赛事
    boolean updateEvent(Event event);    // 更新赛事信息
    boolean deleteEvent(Integer id);     // 删除赛事信息
}
