package com.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.*;

@RestController
public class testController implements testInterface{

    @Override
    public String getById(Student student) {
        return student.getName();
    }

    @Override
    public String getById(String name) {
        return name;
    }
}
