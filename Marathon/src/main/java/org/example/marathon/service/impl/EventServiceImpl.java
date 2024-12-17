package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.marathon.domain.po.Event;
import org.example.marathon.mapper.EventMapper;
import org.example.marathon.service.EventService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImpl extends ServiceImpl<EventMapper, Event> implements EventService {

    @Override
    public List<Event> getAllEventsSortedByDate() {
        // 使用 QueryWrapper 按 eventDate 排序
        QueryWrapper<Event> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("event_date"); // 按照 event_date 升序排序
        return this.list(queryWrapper);
    }
}


