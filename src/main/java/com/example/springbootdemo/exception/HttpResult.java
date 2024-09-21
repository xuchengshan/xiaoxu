package com.example.springbootdemo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpResult<T> {

    // 响应码
    private Integer code;
    // 消息
    private String message;
    // 数据
    private T data;

    public static <T> HttpResult<T> of(int code, String message, T data) {
        return new HttpResult(code, message, data);
    }

    public static <T> HttpResult<T> of(String message) {
        return new HttpResult(-1, message, null);
    }
}
