package com.example.springbootdemo.controller;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Student {

    @NotBlank(message = "name不能为空")
    private String name;

}
