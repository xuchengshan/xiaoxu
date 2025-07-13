package com.example.springbootdemo.service.impl;


import com.example.springbootdemo.entity.TeamPerson;
import com.example.springbootdemo.service.TeamPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TeamPersonServiceImpl implements TeamPersonService {

    @Autowired
    private TeamPersonMapper teamPersonMapper;

    @Override
    public TeamPerson getById(Long id) {
        return teamPersonMapper.selectById(id);
    }
}