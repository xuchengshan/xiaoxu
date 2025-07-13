package com.example.springbootdemo.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("team_person")
public class TeamPerson {

    @TableId
    private Long id;
    private String name;
    private String phone;
}