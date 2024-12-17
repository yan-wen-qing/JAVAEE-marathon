package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.po.Player;
import org.example.marathon.mapper.PlayerMapper;
import org.example.marathon.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {
    @Autowired
    private PlayerMapper playerMapper;

    public Integer add(Player player) {
        log.info("正在保存 player 对象: {}", player);
        boolean isSaved = this.save(player);
        if (isSaved) {
            log.info("保存成功，生成的 ID: {}", player.getId());
            return player.getId();
        } else {
            log.error("保存失败");
            return null;
        }
    }
}

