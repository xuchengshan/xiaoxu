package com.example.springbootdemo.excel;

import org.junit.jupiter.api.Test;

public class ExcelParserServiceTest {

    @Test
    public void testParseExcel() {
        ExcelParserService parser = new ExcelParserService();
        String filePath = "C:\\Users\\cheng\\Desktop\\111.xlsx";

        System.out.println("=== 开始解析 Excel ===");
        parser.parseAndPrint(filePath);
        System.out.println("=== 解析完成 ===");
    }
}