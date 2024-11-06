package com.example.springbootdemo.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class testv2Controller {

    @PostMapping("/test/v2/getById")
    public String getById(@RequestBody Student student) {
        return student.getName();
    }
}
