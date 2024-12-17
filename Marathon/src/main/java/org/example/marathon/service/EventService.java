package org.example.marathon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.marathon.domain.po.Event;

import java.util.List;

public interface EventService extends IService<Event> {
    List<Event> getAllEventsSortedByDate();
}
