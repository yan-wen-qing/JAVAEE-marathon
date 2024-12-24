package org.example.marathon.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventVO {
    private Integer id;          // 赛事ID
    private String name;         // 赛事名称
    private String eventDate;    // 开赛时间
    private String category;     // 赛事类型（全马/半马）
    private ResultVO result;     // 抽签结果
}
