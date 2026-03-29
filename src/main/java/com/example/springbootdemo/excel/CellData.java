package com.example.springbootdemo.excel;

import lombok.Data;

/**
 * Excel单元格数据模型
 */
@Data
public class CellData {
    /**
     * 行索引 (0-based)
     */
    private int rowIndex;

    /**
     * 列索引 (0-based)
     */
    private int columnIndex;

    /**
     * 单元格值
     * - 字符串: 原始输入 (如 "$100")
     * - 数值: 转字符串 (如 "100")
     * - 公式: 计算结果值
     */
    private String value;

    /**
     * 原始类型: FORMULA, NUMERIC, STRING, BLANK
     */
    private String rawType;

    public CellData(int rowIndex, int columnIndex, String value, String rawType) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.value = value;
        this.rawType = rawType;
    }
}