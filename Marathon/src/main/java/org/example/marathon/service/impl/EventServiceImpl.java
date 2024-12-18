package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Event;
import org.example.marathon.mapper.EventMapper;
import org.example.marathon.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Autowired
    private EventMapper eventMapper;

    @Override
    public Integer addEvent(Event event) {
        boolean isSaved = this.save(event);
        return isSaved ? event.getId() : null;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventMapper.selectList(null);
    }

    @Override
    public Event getEventById(Integer id) {
        return eventMapper.selectById(id);
    }

    @Override
    public boolean updateEvent(Event event) {
        return this.updateById(event);
    }

    @Override
    public boolean deleteEvent(Integer id) {
        return this.removeById(id);
    }
}
