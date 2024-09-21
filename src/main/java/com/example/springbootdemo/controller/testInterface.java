package com.example.springbootdemo.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/test/v1")
public interface testInterface {

    @PostMapping("/getById")
    String getById(@RequestBody @Valid Student student);



}
