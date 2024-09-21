package com.example.springbootdemo.exception;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class AllExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());




    /**
     * 处理Get请求中 使用@Valid 验证路径中请求实体校验失败后抛出的异常，详情继续往下看代码
     *
     * @Validated @Valid仅对于表单提交有效，对于以json格式提交将会失效）
     */

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public HttpResult BindExceptionHandler(BindException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        List<String> msgList = new ArrayList<>();
        for (ObjectError allError : allErrors) {
            msgList.add(allError.getDefaultMessage());
        }
        return HttpResult.of(msgList.toString());
    }

    /**
     * @param e 异常类
     * @return 响应
     * @Validated @Valid 前端提交的方式为json格式有效
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public HttpResult MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
        List<String> msgList = new ArrayList<>();
        for (ObjectError allError : allErrors) {
            msgList.add(allError.getDefaultMessage());
        }
        return HttpResult.of(msgList.toString());
    }


    /**
     * 处理请求参数格式错误 @RequestParam上validate失败后抛出的异常是javax.validation.ConstraintViolationException
     *
     * @param ex
     * @return
     * @NotBlank @NotNull @NotEmpty
     */
    @ResponseBody
    @ExceptionHandler(value = ConstraintViolationException.class)
    public HttpResult ConstraintViolationExceptionHandler(ConstraintViolationException ex) {

        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();
        Iterator<ConstraintViolation<?>> iterator = constraintViolations.iterator();
        List<String> msgList = new ArrayList<>();
        while (iterator.hasNext()) {
            ConstraintViolation<?> cvl = iterator.next();
            msgList.add(cvl.getMessageTemplate());
        }
        return HttpResult.of((msgList.toString()));
    }



}

