package com.example.springbootdemo.lombok;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ChildrenDTO extends LombokDTO{
    private String name;
}
