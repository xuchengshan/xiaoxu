package com.example.springbootdemo.easyexcel;

import com.alibaba.excel.write.handler.RowWriteHandler;
import com.alibaba.excel.write.handler.context.RowWriteHandlerContext;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;

public class AutoIndexWriteHandler implements RowWriteHandler {

    private int serialNumber = 1; // 序号初始值
    @Override
    public void afterRowDispose(RowWriteHandlerContext context) {
        System.out.println(context.getRowIndex());
        System.out.println(context.getRelativeRowIndex());
        System.out.println(context.getHead());
//        System.out.println(context.getRow());
        // 跳过表头
        if (context.getHead()) {
            return;
        }
        CellStyle cellStyle1 = context.getRow().getSheet().getWorkbook().createCellStyle();
        cellStyle1.setAlignment(HorizontalAlignment.LEFT);
        // 添加序号
        Cell cell = context.getRow().createCell(0);
        cell.setCellStyle(cellStyle1);
        cell.setCellValue(serialNumber++);
    }
}
