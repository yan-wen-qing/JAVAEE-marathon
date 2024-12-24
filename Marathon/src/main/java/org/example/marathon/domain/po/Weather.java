package org.example.marathon.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("weather")
public class Weather {
    private Integer eventId;            // 赛事ID
    private Integer time;               // 时间
    private Integer temperature;        // 温度
    private String weatherCondition;    // 天气状况
    private Integer whetherToProceed;   // 是否进行
}
