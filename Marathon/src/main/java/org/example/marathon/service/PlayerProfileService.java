package org.example.marathon.service;

import org.example.marathon.domain.po.Player;

public interface PlayerProfileService {
    /**
     * 根据ID查询用户信息
     * @param id 用户ID
     * @return 用户信息
     */
    Player getPlayerById(Integer id);

    /**
     * 更新用户信息
     * @param player 用户信息
     * @return 是否更新成功
     */
    boolean updatePlayer(Player player);
} 