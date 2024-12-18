package org.example.marathon.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.Result;
import org.example.marathon.domain.po.PlayerResult;
import org.example.marathon.service.TopPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/TopPlayers")
public class TopPlayerController {

    @Autowired
    private TopPlayerService topPlayerService;

    /**
     * 获取前N名玩家信息
     */
    @GetMapping("/get_top_players")
    public Result getTopPlayers(@RequestParam("event_id") Integer eventId,
                                @RequestParam("topN") Integer topN,
                                @RequestParam(value = "gender", required = false) String gender,
                                @RequestParam(value = "sortOrder", defaultValue = "asc") String sortOrder) {
        log.info("获取 event_id={}，gender={} 的前 {} 名玩家，排序方式为 {}", eventId, gender, topN, sortOrder);
        List<PlayerResult> topPlayers = topPlayerService.getTopPlayers(eventId, topN, gender, sortOrder);
        return Result.success(topPlayers);
    }
}
