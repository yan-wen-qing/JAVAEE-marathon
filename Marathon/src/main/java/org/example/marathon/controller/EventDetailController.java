package org.example.marathon.controller;

import org.example.marathon.domain.po.Event;
import org.example.marathon.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event/detail")
public class EventDetailController {

    @Autowired
    private EventService eventService;

    /**
     * 获取所有赛事信息并按 eventDate 排序
     * @return 排序后的赛事列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        try {
            // 查询所有赛事信息并按 eventDate 排序
            List<Event> events = eventService.getAllEventsSortedByDate();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<Event> getEventById(@PathVariable Integer eventId) {
        Event event = eventService.getById(eventId);
        if (event != null) {
            return ResponseEntity.ok(event);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
