package org.example.marathon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Event;
import org.example.marathon.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/Event") // 接口路径前缀
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * 添加新的赛事信息
     */
    @PostMapping("/add_event")
    public Result addEvent(@RequestBody Event event) {
        log.info("添加赛事信息: {}", event);
        Integer id = eventService.addEvent(event);
        return id != null ? Result.success(id) : Result.error("添加赛事失败");
    }

    /**
     * 获取所有赛事信息
     */
    @GetMapping("/get_all_event")
    public Result getAllEvents() {
        log.info("获取所有赛事信息");
        List<Event> events = eventService.getAllEvents();
        return Result.success(events);
    }

    /**
     * 根据ID获取赛事信息
     */
    @GetMapping("/get_by_id")
    public Result getEventById(@RequestParam("id") Integer id) {
        log.info("获取ID为 {} 的赛事信息", id);
        Event event = eventService.getEventById(id);
        return event != null ? Result.success(event) : Result.error("未找到赛事信息");
    }

    /**
     * 更新赛事信息
     */
    @PutMapping("/update_event")
    public Result updateEvent(@RequestBody Event event) {
        log.info("更新赛事信息: {}", event);
        boolean isUpdated = eventService.updateEvent(event);
        return isUpdated ? Result.success() : Result.error("更新赛事失败");
    }

    /**
     * 删除赛事信息
     */
    @DeleteMapping("/delete_event")
    public Result deleteEvent(@RequestParam("id") Integer id) {
        log.info("删除ID为 {} 的赛事信息", id);
        boolean isDeleted = eventService.deleteEvent(id);
        return isDeleted ? Result.success() : Result.error("删除赛事失败");
    }
}