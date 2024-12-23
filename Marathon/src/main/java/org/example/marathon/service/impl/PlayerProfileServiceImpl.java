package org.example.marathon.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.marathon.domain.po.Player;
import org.example.marathon.mapper.PlayerMapper;
import org.example.marathon.service.PlayerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PlayerProfileServiceImpl implements PlayerProfileService {
    
    @Autowired
    private PlayerMapper playerMapper;

    @Override
    public Player getPlayerById(Integer id) {
        return playerMapper.selectById(id);
    }

    @Override
    public boolean updatePlayer(Player player) {
        return playerMapper.updateById(player) > 0;
    }
} 