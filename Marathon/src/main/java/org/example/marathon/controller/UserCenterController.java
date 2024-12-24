package org.example.marathon.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Player;
import org.example.marathon.domain.vo.EventVO;
import org.example.marathon.domain.vo.PlayerResultVO;
import org.example.marathon.service.PlayerEventService;
import org.example.marathon.service.PlayerProfileService;
import org.example.marathon.service.PlayerResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Validated
public class UserCenterController {

    @Autowired
    private PlayerProfileService playerProfileService;

    @Autowired
    private PlayerEventService playerEventService;

    @Autowired
    private PlayerResultService playerResultService;

    // =============== 个人信息相关接口 ===============
    @GetMapping("/Auth/get_by_playerid")
    public Result getPlayerById(@RequestParam @NotNull(message = "用户ID不能为空") Integer Id) {
        try {
            log.info("根据ID查询用户信息: {}", Id);
            Player player = playerProfileService.getPlayerById(Id);
            return player != null ? Result.success(player) : Result.error("用户不存在");
        } catch (Exception e) {
            log.error("查询用户信息失败", e);
            return Result.error("查询失败");
        }
    }

    @PatchMapping("/Auth/update_player")
    public Result updatePlayer(@RequestBody @Valid Player player) {
        try {
            log.info("更新用户信息: {}", player);
            boolean success = playerProfileService.updatePlayer(player);
            return success ? Result.success() : Result.error("更新失败");
        } catch (Exception e) {
            log.error("更新用户信息失败", e);
            return Result.error("更新失败");
        }
    }

    // =============== 我的报名相关接口 ===============
    @GetMapping("/Player/get_events_by_playerid")
    public Result getEventsByPlayerId(@RequestParam @NotNull(message = "用户ID不能为空") Integer Id) {
        try {
            log.info("查询用户报名的赛事: {}", Id);
            List<EventVO> events = playerEventService.getEventsByPlayerId(Id);
            return Result.success(events);
        } catch (Exception e) {
            log.error("查询用户报名赛事失败", e);
            return Result.error("查询失败");
        }
    }

    @GetMapping("/Player/get_number_by_eventid_and_playerid")
    public Result getEventResult(
            @RequestParam @NotNull(message = "用户ID不能为空") Integer playerId,
            @RequestParam @NotNull(message = "赛事ID不能为空") Integer eventId) {
        try {
            log.info("查询用户抽签结果 - 用户ID: {}, 赛事ID: {}", playerId, eventId);
            String result = playerEventService.getEventResult(playerId, eventId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询抽签结果失败", e);
            return Result.error("查询失败");
        }
    }

    // =============== 我的成绩相关接口 ===============
    @GetMapping("/Result/search_history_result")
    public Result getHistoryResults(@RequestParam @NotNull(message = "用户ID不能为空") Integer playerId) {
        try {
            log.info("查询用户历史成绩: {}", playerId);
            List<PlayerResultVO> results = playerResultService.getHistoryResults(playerId);
            return Result.success(results);
        } catch (Exception e) {
            log.error("查询历史成绩失败", e);
            return Result.error("查询失败");
        }
    }

    @GetMapping("/Result/search_history_half_result")
    public Result getHalfResults(@RequestParam @NotNull(message = "用户ID不能为空") Integer playerId) {
        try {
            log.info("查询用户半马成绩: {}", playerId);
            List<PlayerResultVO> results = playerResultService.getResultsByCategory(playerId, "半马");
            return Result.success(results);
        } catch (Exception e) {
            log.error("查询半马成绩失败", e);
            return Result.error("查询失败");
        }
    }

    @GetMapping("/Result/search_history_full_result")
    public Result getFullResults(@RequestParam @NotNull(message = "用户ID不能为空") Integer playerId) {
        try {
            log.info("查询用户全马成绩: {}", playerId);
            List<PlayerResultVO> results = playerResultService.getResultsByCategory(playerId, "全马");
            return Result.success(results);
        } catch (Exception e) {
            log.error("查询全马成绩失败", e);
            return Result.error("查询失败");
        }
    }
}