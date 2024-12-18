package org.example.marathon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.marathon.domain.po.Event;
import org.example.marathon.domain.po.Player;

public interface PlayerService extends IService<Player> {
    Integer login(Player player);
    Player getPlayerById(Integer id);      // 根据ID获取赛事

}
