package com.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class testController implements testInterface{
    @Override
    public String getById(Student student) {
        return student.getName();
    }
}
