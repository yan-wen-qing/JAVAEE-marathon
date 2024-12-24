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
    @TableId // 主键字段
    private Integer playerId; // 选手 ID

    @TableField("event_id") // 联合主键的另一部分，普通字段
    private Integer eventId; // 赛事 ID

    @TableField("role")
    private String role; // 角色（普通跑者、慈善跑者、已中签、未中签）

    @TableField("number")
    private String number; // 参赛号码
}

