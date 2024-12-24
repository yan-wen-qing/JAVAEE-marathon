package org.example.marathon.domain.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("result")
public class Result {
    @TableId(value = "id", type = IdType.AUTO)  //设置了ID递增
    private Integer id;  // ID
    @TableField("gun_result")
    private Integer gunResult; // 枪声成绩
    @TableField("net_result")
    private Integer netResult;  // 网络成绩
    @TableField("player_id")
    private Integer playerId;  // 选手 ID
    @TableField("event_id")
    private Integer eventId;  // 赛事 ID
    @TableField("player_rank")
    private Integer playerRank;  // 全部选手排名
    @TableField("gender_rank")
    private Integer genderRank;  //性别排名
}