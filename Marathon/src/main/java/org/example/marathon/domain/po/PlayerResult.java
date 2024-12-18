package org.example.marathon.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// 自动生成 get/set、toString 方法，自动生成构造函数
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("result")
public class PlayerResult {
    @TableId(value = "id", type = IdType.AUTO)  // ID自增
    private Integer id;                // 记录ID

    @TableField("gun_result")
    private Integer gunResult;         // 枪声成绩

    @TableField("net_result")
    private Integer netResult;         // 净成绩

    @TableField("player_id")
    private Integer playerId;          // 玩家ID

    @TableField("event_id")
    private Integer eventId;           // 赛事ID

    @TableField("player_rank")
    private Integer playerRank;        // 综合排名

    @TableField("gender_rank")
    private Integer genderRank;        // 性别排名
}
