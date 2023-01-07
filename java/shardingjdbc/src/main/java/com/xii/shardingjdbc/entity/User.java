package com.xii.shardingjdbc.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_user")
public class User {
    // 主键
    @TableId
    private Integer id;
    // 用户名
    private String nickname;
    private Integer password;
    // 年龄
    private Integer age;
    /**
     * 性别 0代表女 1代表男
     */
    private Integer sex;
}

