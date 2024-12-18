package org.example.marathon.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("participate")
public class Participate {

    @TableField("role")
    private String role;       // 角色：pacer, elite, normal, charity, first_aid

    @TableField("number")
    private String number;     // 选手号码

    @TableField("player_id")
    private Integer playerId;  // 玩家ID

    @TableField("event_id")
    private Integer eventId;   // 赛事ID
}
