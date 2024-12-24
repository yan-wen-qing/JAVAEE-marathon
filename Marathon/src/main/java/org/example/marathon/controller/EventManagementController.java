package org.example.marathon.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.service.ParticipateService;
import org.example.marathon.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/event")
public class EventManagementController {

    @Autowired
    private ParticipateService participateService;

    @Autowired
    private ResultService resultService;

    @PostMapping("/{eventId}/drawLots")
    public Result drawLots(@PathVariable Integer eventId, @RequestParam Integer totalParticipants) {
        try {
            participateService.drawLots(eventId, totalParticipants);
            return Result.success("抽签完成，中签名单已更新");
        } catch (IllegalStateException e) {
            log.warn(e.getMessage());
            return Result.error(e.getMessage());
        } catch (Exception e) {
            log.error("抽签失败: {}", e.getMessage());
            return Result.error("抽签失败，请稍后重试");
        }
    }

    @GetMapping("/participants")
    public Result getParticipants(@RequestParam Integer eventId) {
        try {
            List<Map<String, Object>> participants = participateService.getParticipantsByEventId(eventId);
            return Result.success(participants);
        } catch (Exception e) {
            log.error("获取参赛者失败: {}", e.getMessage());
            return Result.error("获取参赛者列表失败，请稍后重试");
        }
    }

    @GetMapping("/player/history")
    public Result getPlayerHistory(@RequestParam Integer playerId) {
        try {
            List<Map<String, Object>> history = participateService.getPlayerHistory(playerId);
            return Result.success(history);
        } catch (Exception e) {
            log.error("获取选手历史成绩失败: {}", e.getMessage());
            return Result.error("获取选手历史成绩失败，请稍后重试");
        }
    }

    @PostMapping("/submitPacers")
    public Result submitPacers(@RequestBody Map<String, Object> request) {
        try {
            Integer eventId = Integer.valueOf(request.get("eventId").toString());
            List<Integer> pacerIds = (List<Integer>) request.get("pacerIds");
            participateService.submitPlayers(eventId, pacerIds, "pacer", "pacerIsChosen");
            return Result.success("配速员已选拔完成");
        } catch (Exception e) {
            log.error("配速员提交失败: {}", e.getMessage());
            return Result.error("配速员名单上传失败，请稍后重试");
        }
    }

    @PostMapping("/submitAids")
    public Result submitAids(@RequestBody Map<String, Object> request) {
        try {
            Integer eventId = Integer.valueOf(request.get("eventId").toString());
            List<Integer> aidIds = (List<Integer>) request.get("aidIds");
            participateService.submitPlayers(eventId, aidIds, "aid", "aidIsChosen");
            return Result.success("急救跑者已选拔完成");
        } catch (Exception e) {
            log.error("急救跑者提交失败: {}", e.getMessage());
            return Result.error("急救跑者名单上传失败，请稍后重试");
        }
    }
    @PostMapping("/submitGrades")
    public Result submitGrades(@RequestBody List<org.example.marathon.domain.po.Result> results) {

        for (org.example.marathon.domain.po.Result result : results) {
            log.info("提交成绩: {}", result);
        }
        boolean isAdded = resultService.addResults(results);
        if (isAdded) {
            return Result.success();
        } else {
            return Result.error("提交成绩失败");
        }

    }

}