package org.example.marathon.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//自动生成get set toString方法，自动生成构造函数
@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("player")
public class Player {
    @TableId(value = "id", type = IdType.AUTO)  //设置了ID递增
    private Integer id;  // ID
    @TableField("name")
    private String name;  // 玩家姓名
    @TableField("gender")
    private String gender;  // 性别
    @TableField("age")
    private Integer age;  // 年龄
    @TableField("id_number")
    private String idNumber;  // 身份证号
    @TableField("region")
    private String region;  // 所在区域
    @TableField("telephone_number")
    private String telephoneNumber;  // 电话号码
    @TableField("password")
    private String password;  // 密码
}
