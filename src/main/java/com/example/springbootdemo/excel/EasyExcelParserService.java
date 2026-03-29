package com.example.springbootdemo.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * EasyExcel解析服务（增强版）
 * 结合 EasyExcel 流式读取 + POI 格式化能力
 */
public class EasyExcelParserService {

    /**
     * 解析Excel文件并返回所有单元格数据
     *
     * @param filePath Excel文件路径
     * @return 单元格数据列表
     */
    public List<CellData> parseExcel(String filePath) {
        CellDataListener listener = new CellDataListener();

        try {
            // 先用 POI 创建 Workbook（用于获取格式化值）
            InputStream poiStream = Files.newInputStream(Paths.get(filePath));
            Workbook poiWorkbook = WorkbookFactory.create(poiStream);

            // 重新打开流给 EasyExcel 使用
            InputStream easyExcelStream = Files.newInputStream(Paths.get(filePath));

            // 将 POI Workbook 传递给监听器
            listener.setPoiWorkbook(poiWorkbook);

            // headRowNumber(0) 表示从第0行开始读取（不跳过表头）
            ExcelReader excelReader = EasyExcel.read(easyExcelStream, listener)
                .headRowNumber(0)
                .build();
            ReadSheet sheet = EasyExcel.readSheet(0).build();
            excelReader.read(sheet);
            excelReader.finish();

            easyExcelStream.close();
            poiStream.close();
            poiWorkbook.close();
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败: " + e.getMessage(), e);
        }

        return listener.getCellDataList();
    }

    /**
     * 解析Excel文件(InputStream)并返回所有单元格数据
     * 注意：此方式无法使用 POI 格式化，降级为原始值
     *
     * @param inputStream Excel文件输入流
     * @return 单元格数据列表
     */
    public List<CellData> parseExcel(InputStream inputStream) {
        CellDataListener listener = new CellDataListener();

        try {
            // InputStream 方式降级为 EasyExcel 原始值（不保留格式）
            ExcelReader excelReader = EasyExcel.read(inputStream, listener)
                .headRowNumber(0)
                .build();
            ReadSheet sheet = EasyExcel.readSheet(0).build();
            excelReader.read(sheet);
            excelReader.finish();
        } catch (Exception e) {
            throw new RuntimeException("Excel解析失败: " + e.getMessage(), e);
        }

        return listener.getCellDataList();
    }

    /**
     * 解析Excel文件并打印所有单元格数据
     *
     * @param filePath Excel文件路径
     */
    public void parseAndPrint(String filePath) {
        List<CellData> cellDataList = parseExcel(filePath);

        System.out.println("=== 开始解析 Excel (EasyExcel + POI) ===");
        for (CellData cellData : cellDataList) {
            System.out.printf("[%d,%d] %s (%s)%n",
                cellData.getRowIndex(),
                cellData.getColumnIndex(),
                cellData.getValue(),
                cellData.getRawType());
        }
        System.out.println("=== 解析完成 ===");
    }
}