package org.example.marathon.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.marathon.domain.po.Player;
import org.example.marathon.mapper.PlayerMapper;
import org.example.marathon.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayerServiceImpl extends ServiceImpl<PlayerMapper, Player> implements PlayerService {
    @Autowired
    private PlayerMapper playerMapper;
    public Integer add(Player player){
        // 保存 player 对象到数据库，mybatisplus自动生成的方法，减少代码
        boolean isSaved = this.save(player);
        // 如果保存成功，返回保存后的 player id
        if (isSaved) {
            return player.getId();  // 获取插入记录后的自增id
        } else {
            return null;  // 保存失败时返回 null
        }
    }
}
