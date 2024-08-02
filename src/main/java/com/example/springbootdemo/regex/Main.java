package com.example.springbootdemo.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    static Pattern FUNCTION_PATTERN = Pattern.compile("\\{(\\w*)\\{(.*?)}}");
    public static void main(String[] args) {
        String str = "添加人员“{{#dto.personName}}({{#_result.data.personNo}})”";
        // 定义正则表达式
        String regex = "\\{(.*?)\\{(.*?)\\}}";
        Pattern pattern = Pattern.compile(regex);
//        Matcher matcher = pattern.matcher(str);

        Matcher matcher = FUNCTION_PATTERN.matcher(str);

        while (matcher.find()) {
            String group = matcher.group(1);
            String group2 = matcher.group(2);
            System.out.println("group1:"+group + "," + "group2:" + group2);
            /**
             * group1:,group2:#dto.personName
             * group1:,group2:#_result.data.personNo
             */
        }

    }
}
