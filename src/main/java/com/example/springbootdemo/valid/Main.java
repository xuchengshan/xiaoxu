package com.example.springbootdemo.valid;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;


public class Main {

    public static void main(String[] args) {

        SomeRequestObject someRequestObject = new SomeRequestObject();
        someRequestObject.setOrganizationNo("AAA");
        someRequestObject.setOrganizationName(" ");

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<SomeRequestObject>> violations = validator.validate(someRequestObject);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<SomeRequestObject> violation : violations) {
                System.out.println(violation.getMessage());
            }
            // 抛出异常或处理校验失败的情况
            throw new RuntimeException("Validation failed");
        }
        // 校验通过
        System.out.println("Validation succeeded");
    }
}
