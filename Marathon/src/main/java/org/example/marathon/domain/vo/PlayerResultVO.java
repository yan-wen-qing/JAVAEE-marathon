package org.example.marathon.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerResultVO {
    private String eventName;    // 比赛名称
    private Integer gunResult;   // 枪声成绩（秒）
    private Integer netResult;   // 净成绩（秒）
    private Integer genderRank;  // 性别排名
    private Integer rank;        // 总排名
}