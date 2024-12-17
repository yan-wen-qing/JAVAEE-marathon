package org.example.marathon.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("event")
public class Event {
    @TableId(value = "id", type = IdType.AUTO)  // 设置了ID递增
    private Integer id;  // ID

    @TableField("category")
    private String category;  // 类型

    @TableField("name")
    private String name;  // 名称

    @TableField("start_date")
    private LocalDate startDate;  // 报名开始日期

    @TableField("end_date")
    private LocalDate endDate;  // 报名结束日期

    @TableField("event_date")
    private LocalDate eventDate;  // 比赛日期

    @TableField("scale")
    private Integer scale;  // 规模

    @TableField("is_drawn")
    private Boolean isDrawn;  // 是否抽签（默认为0）

    @TableField("pacer_is_chosen")
    private Boolean pacerIsChosen;  // 是否选择了配速员（默认为0）

    @TableField("aid_is_chosen")
    private Boolean aidIsChosen;  // 是否选择了急救跑者（默认为0）
}
