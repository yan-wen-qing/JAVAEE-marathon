package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Player;
import org.example.marathon.mapper.PlayerMapper;
import org.example.marathon.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    public Integer add(Player player) {
        // 检查数据库中是否存在相同的身份证号
        QueryWrapper<Player> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id_number", player.getIdNumber());
        Player existingPlayer = this.getOne(queryWrapper);

        // 如果已经存在相同的身份证号，直接返回 0
        if (existingPlayer != null) {
            return null;
        }

        // 保存 player 对象到数据库，mybatisplus自动生成的方法
        boolean isSaved = this.save(player);

        // 如果保存成功，返回保存后的 player id
        if (isSaved) {
            return player.getId();  // 获取插入记录后的自增id
        } else {
            return null;  // 保存失败时返回 null
        }
    }

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
