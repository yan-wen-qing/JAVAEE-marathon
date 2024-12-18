package org.example.marathon.domain.po;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("event")
public class Event {
    @TableId(type = IdType.AUTO)  // 指定自增策略
    private Integer id;           // 自增ID
    private String category;      // 赛事类别
    private String name;          // 赛事名称
    private Date startDate;       // 报名开始日期
    private Date endDate;         // 报名结束日期
    private Date eventDate;       // 赛事具体日期
    private Integer scale;        // 赛事规模
}
