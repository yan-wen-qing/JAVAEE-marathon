package org.example.marathon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Jwt;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.Player;
import org.example.marathon.service.PlayerService;
import org.example.marathon.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class PlayerController {
    @Autowired
    private PlayerService playerService;
    @PostMapping("/Auth/add_player")
    public Result add(@RequestBody Player player) {
        log.info("用户注册");
        Integer id = playerService.add(player);
        log.info("id={}", id);
        if (id == null) {
            return Result.error("身份证号已存在");
        }
        return Result.success(id);
    }
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
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",id);
        String jwt= JwtUtils.generateJwt(claims);
        Jwt jwt1=new Jwt();
        jwt1.setJwt(jwt);
        jwt1.setId(id);
        return Result.success(jwt1);  // 返回 JWT 令牌
    }
    @PostMapping("/Admin/login")
    public Result adminLogin() {
        log.info("管理员登录");
        Map<String,Object> claims=new HashMap<>();
        claims.put("id",0);
        String jwt= JwtUtils.generateJwt(claims);
        return Result.success(jwt);  // 返回 JWT 令牌
    }


}
