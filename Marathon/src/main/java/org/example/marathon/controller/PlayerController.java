package org.example.marathon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Player;
import org.example.marathon.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    static {
        System.out.println("PlayerController 已加载！");
    }
    @PostMapping("/Auth/add_player")
    public Result add(@RequestBody Player player) {
        log.info("收到 POST 请求: /Auth/add_player");
        log.info("接收到的 player 数据: {}", player);
        Integer id = playerService.add(player);
        log.info("保存后的 ID: {}", id);
        return Result.success(id);
    }
}
