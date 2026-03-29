package com.example.springbootdemo.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel解析服务
 * 使用POI直接读取单元格原始值
 */
public class ExcelParserService {

    private final DataFormatter dataFormatter = new DataFormatter();

    /**
     * 解析Excel文件并返回所有单元格数据
     *
     * @param filePath Excel文件路径
     * @return 单元格数据列表
     */
    public List<CellData> parseExcel(String filePath) {
        List<CellData> cellDataList = new ArrayList<>();
        try (InputStream inputStream = new FileInputStream(filePath);
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    int rowIndex = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();
                    String value = readCellValue(cell, workbook);
                    String rawType = cell.getCellType().name();

                    CellData cellData = new CellData(rowIndex, columnIndex, value, rawType);
                    cellDataList.add(cellData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败: " + e.getMessage(), e);
        }
        return cellDataList;
    }

    /**
     * 解析Excel文件(InputStream)并返回所有单元格数据
     *
     * @param inputStream Excel文件输入流
     * @return 单元格数据列表
     */
    public List<CellData> parseExcel(InputStream inputStream) {
        List<CellData> cellDataList = new ArrayList<>();
        try (Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                for (Cell cell : row) {
                    int rowIndex = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();
                    String value = readCellValue(cell, workbook);
                    String rawType = cell.getCellType().name();

                    CellData cellData = new CellData(rowIndex, columnIndex, value, rawType);
                    cellDataList.add(cellData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败: " + e.getMessage(), e);
        }
        return cellDataList;
    }

    /**
     * 解析Excel文件并打印所有单元格数据
     *
     * @param filePath Excel文件路径
     */
    public void parseAndPrint(String filePath) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath));
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            System.out.println("=== 开始解析 Excel ===");
            for (Row row : sheet) {
                for (Cell cell : row) {
                    int rowIndex = cell.getRowIndex();
                    int columnIndex = cell.getColumnIndex();
                    String value = readCellValue(cell, workbook);
                    String rawType = cell.getCellType().name();

                    System.out.printf("[%d,%d] %s (%s)%n", rowIndex, columnIndex, value, rawType);
                }
            }
            System.out.println("=== 解析完成 ===");
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败: " + e.getMessage(), e);
        }
    }

    /**
     * 读取单元格值
     * - 公式: 返回计算结果
     * - 数值: 保留格式前缀 (如 "$100")
     * - 字符串: 返回原始字符串
     */
    private String readCellValue(Cell cell, Workbook workbook) {
        CellType cellType = cell.getCellType();

        switch (cellType) {
            case FORMULA:
                return readFormulaValue(cell, workbook);
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
     * 注意: 使用缓存值而非重新评估，避免RANDBETWEEN等随机函数产生新值
     */
    private String readFormulaValue(Cell cell, Workbook workbook) {
        try {
            // 优先读取缓存的公式结果值（Excel保存时的值）
            CellType cachedType = cell.getCachedFormulaResultType();

            switch (cachedType) {
                case NUMERIC:
                    double numericValue = cell.getNumericCellValue();
                    // 如果是整数，不显示小数点
                    if (numericValue == (long) numericValue) {
                        return String.valueOf((long) numericValue);
                    }
                    return String.valueOf(numericValue);
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
}