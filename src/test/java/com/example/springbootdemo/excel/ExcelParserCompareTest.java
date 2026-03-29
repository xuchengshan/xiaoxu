package com.example.springbootdemo.excel;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * POI与EasyExcel解析结果对比测试
 */
public class ExcelParserCompareTest {

    private static final String TEST_FILE = "C:\\Users\\cheng\\Desktop\\111.xlsx";

    @Test
    public void testPoiParser() {
        System.out.println("\n========== POI 解析结果 ==========");
        ExcelParserService poiParser = new ExcelParserService();
        poiParser.parseAndPrint(TEST_FILE);
    }



    @Test
    public void testEasyExcelParser() {
        System.out.println("\n========== EasyExcel 解析结果 ==========");
        EasyExcelParserService easyParser = new EasyExcelParserService();
        easyParser.parseAndPrint(TEST_FILE);
    }

    @Test
    public void testCompareResults() {
        System.out.println("\n========== 结果对比 ==========");

        ExcelParserService poiParser = new ExcelParserService();
        EasyExcelParserService easyParser = new EasyExcelParserService();

        List<CellData> poiResult = poiParser.parseExcel(TEST_FILE);
        List<CellData> easyResult = easyParser.parseExcel(TEST_FILE);

        System.out.println("POI 解析单元格数量: " + poiResult.size());
        System.out.println("EasyExcel 解析单元格数量: " + easyResult.size());

        // 对比每个单元格
        int matchCount = 0;
        int mismatchCount = 0;

        for (int i = 0; i < Math.min(poiResult.size(), easyResult.size()); i++) {
            CellData poiCell = poiResult.get(i);
            CellData easyCell = easyResult.get(i);

            boolean rowMatch = poiCell.getRowIndex() == easyCell.getRowIndex();
            boolean colMatch = poiCell.getColumnIndex() == easyCell.getColumnIndex();
            boolean valueMatch = equalsIgnoreNull(poiCell.getValue(), easyCell.getValue());
            boolean typeMatch = equalsIgnoreNull(poiCell.getRawType(), easyCell.getRawType());

            if (rowMatch && colMatch && valueMatch && typeMatch) {
                matchCount++;
            } else {
                mismatchCount++;
                System.out.printf("差异 [%d,%d]:\n", poiCell.getRowIndex(), poiCell.getColumnIndex());
                System.out.printf("  POI:     value=%s, type=%s\n", poiCell.getValue(), poiCell.getRawType());
                System.out.printf("  EasyExcel: value=%s, type=%s\n", easyCell.getValue(), easyCell.getRawType());
            }
        }

        System.out.println("\n匹配数量: " + matchCount);
        System.out.println("差异数量: " + mismatchCount);

        if (mismatchCount == 0) {
            System.out.println("✅ 两种解析方式结果完全一致！");
        } else {
            System.out.println("⚠️ 存在差异，请检查格式化数值或公式单元格");
        }
    }

    private boolean equalsIgnoreNull(String a, String b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.equals(b);
    }
}