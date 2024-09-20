package com.example.springbootdemo.valid;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class SomeRequestObject {
    /**
     * 组织编号
     */
    @NotBlank(message = "组织编号不能为空")
    private String organizationNo;


    /**
     * 组织名称
     */
    @NotBlank(message = "班级名称不能为空")
    @Length(max = 10, message = "班级名称最多10个字符")
    @Pattern(regexp = "^[\\u4e00-\\u9fa5a-zA-Z0-9]+$", message = "只支持中英文和数字")
    private String organizationName;
}
