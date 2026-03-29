package com.example.springbootdemo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义Excel单元格监听器
 * 解决"$100"等特殊格式单元格读取丢失前缀的问题
 */
public class CustomCellListener extends AnalysisEventListener<Cell> {

    private final List<CellData> cellDataList = new ArrayList<>();
    private final DataFormatter dataFormatter = new DataFormatter();

    @Override
    public void invoke(Cell cell, AnalysisContext context) {
        int rowIndex = cell.getRowIndex();
        int columnIndex = cell.getColumnIndex();
        String value = readCellValue(cell);
        String rawType = cell.getCellType().name();

        CellData cellData = new CellData(rowIndex, columnIndex, value, rawType);
        cellDataList.add(cellData);
    }

    /**
     * 读取单元格值
     * - 公式: 返回缓存计算结果
     * - 数值: 保留格式前缀 (如 "$100")
     * - 字符串: 返回原始字符串
     */
    private String readCellValue(Cell cell) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case FORMULA:
                return readFormulaValue(cell);
            case NUMERIC:
                // 使用DataFormatter保留格式前缀 (如 "$100")
                return dataFormatter.formatCellValue(cell);
            case STRING:
                return cell.getStringCellValue();
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case BLANK:
                return "";
            case ERROR:
                return "#ERROR";
            default:
                return "";
        }
    }

    /**
     * 读取公式单元格的缓存计算结果
     */
    private String readFormulaValue(Cell cell) {
        try {
            CellType cachedType = cell.getCachedFormulaResultType();
            switch (cachedType) {
                case NUMERIC:
                    // 使用DataFormatter格式化数值结果
                    return dataFormatter.formatCellValue(cell);
                case STRING:
                    return cell.getStringCellValue();
                case BOOLEAN:
                    return String.valueOf(cell.getBooleanCellValue());
                case ERROR:
                    return "#ERROR";
                default:
                    return cell.getCellFormula();
            }
        } catch (Exception e) {
            // 无法读取缓存值, 返回公式文本
            return "#" + cell.getCellFormula();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完成后的回调
    }

    /**
     * 获取所有解析的单元格数据
     */
    public List<CellData> getCellDataList() {
        return cellDataList;
    }

    /**
     * 打印所有单元格数据
     */
    public void printAll() {
        for (CellData data : cellDataList) {
            System.out.printf("[%d,%d] %s (%s)%n",
                    data.getRowIndex(),
                    data.getColumnIndex(),
                    data.getValue(),
                    data.getRawType());
        }
    }
}