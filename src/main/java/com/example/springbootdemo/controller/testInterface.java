package com.example.springbootdemo.controller;


import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/test/v1")
public interface testInterface {

    @PostMapping("/getById")
    String getById(@RequestBody @Valid Student student);


    @GetMapping("/byId")
    String getById(@RequestParam("name")String name);



}
