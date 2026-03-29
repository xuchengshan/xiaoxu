package com.example.springbootdemo.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * EasyExcel 单元格数据监听器（增强版）
 * 结合外部传入的 POI Workbook，实现格式化数值读取（如 $100）
 * 支持读取公式缓存的计算结果值
 */
public class CellDataListener extends AnalysisEventListener<Map<Integer, Object>> {

    private final List<CellData> cellDataList = new ArrayList<>();
    private final DataFormatter dataFormatter = new DataFormatter();

    // 外部传入的 POI Workbook（用于获取格式化值）
    private Workbook poiWorkbook;
    private Sheet poiSheet;

    /**
     * 设置 POI Workbook（由外部调用者传入）
     */
    public void setPoiWorkbook(Workbook workbook) {
        this.poiWorkbook = workbook;
    }

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        processRowData(data, context);
    }

    /**
     * 处理一行数据
     */
    private void processRowData(Map<Integer, Object> data, AnalysisContext context) {
        int rowIndex = context.readRowHolder().getRowIndex();

        // 确保底层 POI Sheet 已初始化
        if (poiSheet == null) {
            initPoiSheet(context);
        }

        data.forEach((colIndex, value) -> {
            String formattedValue;
            String rawType;

            // 尝试通过底层 POI Cell 获取格式化值
            if (poiSheet != null) {
                formattedValue = getPoiFormattedValue(rowIndex, colIndex, value);
                rawType = getPoiCellType(rowIndex, colIndex);
            } else {
                // 无法获取 POI 对象时，使用 EasyExcel 原始值
                formattedValue = formatCellValue(value);
                rawType = getRawType(value);
            }

            cellDataList.add(new CellData(rowIndex, colIndex, formattedValue, rawType));
        });
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完成
    }

    /**
     * 获取收集的单元格数据列表
     */
    public List<CellData> getCellDataList() {
        return cellDataList;
    }

    /**
     * 初始化底层 POI Sheet
     */
    private void initPoiSheet(AnalysisContext context) {
        if (poiWorkbook == null) {
            return;
        }
        try {
            int sheetIndex = context.readSheetHolder().getSheetNo();
            poiSheet = poiWorkbook.getSheetAt(sheetIndex);
        } catch (Exception e) {
            poiSheet = null;
        }
    }

    /**
     * 通过底层 POI Cell 获取格式化值
     * - 数值: 保留格式前缀 (如 "$100")
     * - 公式: 读取缓存的计算结果值（不重新计算）
     */
    private String getPoiFormattedValue(int rowIndex, int colIndex, Object easyExcelValue) {
        try {
            Row row = poiSheet.getRow(rowIndex);
            if (row == null) {
                return formatCellValue(easyExcelValue);
            }

            Cell poiCell = row.getCell(colIndex);
            if (poiCell == null) {
                return formatCellValue(easyExcelValue);
            }

            CellType cellType = poiCell.getCellType();

            // 公式单元格：读取缓存的计算结果值
            if (cellType == CellType.FORMULA) {
                return readFormulaCachedValue(poiCell);
            }

            // 数值单元格：使用 DataFormatter 保留格式前缀 (如 "$100")
            if (cellType == CellType.NUMERIC) {
                return dataFormatter.formatCellValue(poiCell);
            }

            // 其他类型：直接读取值
            return dataFormatter.formatCellValue(poiCell);
        } catch (Exception e) {
            return formatCellValue(easyExcelValue);
        }
    }

    /**
     * 读取公式单元格的缓存计算结果值
     * 注意: 使用缓存值而非重新评估，避免 RANDBETWEEN 等随机函数产生新值
     */
    private String readFormulaCachedValue(Cell cell) {
        try {
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
            // 无法读取缓存值，返回公式文本
            return "#" + cell.getCellFormula();
        }
    }

    /**
     * 获取 POI Cell 类型
     */
    private String getPoiCellType(int rowIndex, int colIndex) {
        try {
            Row row = poiSheet.getRow(rowIndex);
            if (row == null) {
                return "EMPTY";
            }

            Cell poiCell = row.getCell(colIndex);
            if (poiCell == null) {
                return "EMPTY";
            }

            return poiCell.getCellType().name();
        } catch (Exception e) {
            return "UNKNOWN";
        }
    }

    /**
     * 格式化单元格值（EasyExcel 原始值，降级方案）
     */
    private String formatCellValue(Object value) {
        if (value == null) {
            return "";
        }
        return String.valueOf(value);
    }

    /**
     * 获取 EasyExcel 原始类型
     */
    private String getRawType(Object value) {
        if (value == null) {
            return "EMPTY";
        }
        if (value instanceof Number) {
            return "NUMBER";
        }
        if (value instanceof Boolean) {
            return "BOOLEAN";
        }
        return "STRING";
    }
}