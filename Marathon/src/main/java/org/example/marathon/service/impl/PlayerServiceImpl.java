package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Event;
import org.example.marathon.domain.po.Player;
import org.example.marathon.mapper.PlayerMapper;
import org.example.marathon.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    public Integer login(Player player) {
        // 使用身份证号查询玩家记录
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_number", player.getIdNumber());
        Player existingPlayer = this.getOne(queryWrapper);

        // 如果玩家不存在，返回 -1 表示身份证号不存在
        if (existingPlayer == null) {
            return -1;
        }

        // 验证姓名和密码是否匹配
        if (!existingPlayer.getName().equals(player.getName()) ||
                !existingPlayer.getPassword().equals(player.getPassword())) {
            return null;  // 姓名或密码不匹配
        }

        // 验证通过，返回玩家 ID
        return existingPlayer.getId();
    }

    @Override
    public Player getPlayerById(Integer id) {
        return playerMapper.selectById(id);
    }
}
