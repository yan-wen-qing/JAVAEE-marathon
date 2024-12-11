package org.example.marathon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.marathon.domain.po.Player;

public interface PlayerService extends IService<Player> {
    Integer add(Player player);

}
