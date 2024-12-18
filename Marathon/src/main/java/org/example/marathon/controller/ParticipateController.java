package org.example.marathon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Participate;
import org.example.marathon.service.ParticipateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/Participate")
public class ParticipateController {

    @Autowired
    private ParticipateService participateService;

    /**
     * 选手报名接口
     */
    @PostMapping("/add_participate")
    public Result addParticipate(@RequestBody Participate participate) {
        log.info("选手报名: {}", participate);
        boolean isAdded = participateService.addParticipate(participate);
        return isAdded ? Result.success() : Result.error("报名失败");
    }

    /**
     * 取消报名接口
     */
    @DeleteMapping("/delete_participate")
    public Result deleteParticipate(@RequestParam("player_Id") Integer playerId,
                                    @RequestParam("event_Id") Integer eventId) {
        log.info("取消报名: playerId={}, eventId={}", playerId, eventId);
        boolean isDeleted = participateService.deleteParticipate(playerId, eventId);
        return isDeleted ? Result.success() : Result.error("取消报名失败");
    }
}
