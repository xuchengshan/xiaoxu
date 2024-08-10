package com.example.springbootdemo.spel;

import org.apache.dubbo.common.utils.Assert;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class Main {
    public static void main(String[] args) {
        Object user = new Object() {
            public String getName() {
                return "abc";
            }
        };
        EvaluationContext context = new StandardEvaluationContext(user);
        ExpressionParser parser = new SpelExpressionParser();
        context.setVariable("hname", "1234");
        String name = parser.parseExpression("#name").getValue(context, String.class);
        String hname = parser.parseExpression("#hname").getValue(context, String.class);
        String value = parser.parseExpression("getName()").getValue(context, String.class);

        Object blank = parser.parseExpression("#blank").getValue(context);

        System.out.println("name:"+name);
        System.out.println("hname:"+hname);
        System.out.println("value:"+value);
    }
}
