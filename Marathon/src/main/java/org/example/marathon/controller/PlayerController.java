package org.example.marathon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Event;
import org.example.marathon.domain.po.Player;
import org.example.marathon.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @PostMapping("/Auth/login_player")
    public Result login(@RequestBody Player player) {
        log.info("用户登录");
        Integer id = playerService.login(player);
        log.info("id={}", id);
        if (id == null) {
            return Result.error("错误"); // 返回姓名和密码错误
        }
        else if (id == -1) {
            return Result.error("未注册");  // 返回未注册错误
        }
        return Result.success(id);  // 返回 JWT 令牌
    }

    @GetMapping("/Player/get_by_playerId")
    public Result getPlayerById(@RequestParam("id") Integer id) {
        log.info("获取ID为 {} 的用户信息", id);
        Player player = playerService.getPlayerById(id);
        return player != null ? Result.success(player) : Result.error("未找到用户信息");
    }
}
