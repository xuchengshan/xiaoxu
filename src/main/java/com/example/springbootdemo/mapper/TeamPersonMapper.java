package com.example.springbootdemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootdemo.entity.TeamPerson;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TeamPersonMapper extends BaseMapper<TeamPerson> {
}