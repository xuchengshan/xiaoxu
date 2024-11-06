package com.example.springbootdemo.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.example.springbootdemo.easyexcel.Income;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@RestController()
@RequestMapping("/easy")
public class EasyexcelController {


    @GetMapping("/down")
    public void download( HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("哈哈火锅啊啊啊啊.xlsx", "UTF-8"));
        response.setCharacterEncoding("utf-8");
        ExcelWriterBuilder write = EasyExcel.write(response.getOutputStream());
        write.registerWriteHandler(new AutoIndexWriteHandler());
        try (ExcelWriter writer = write
                .withTemplate(new ClassPathResource("excel/consumption_export_day_merchant_income.xlsx").getInputStream())
                .build()) {
            writer.fill(getExportData(), FillConfig.builder().forceNewRow(true).build(), EasyExcel.writerSheet(0).build());
        } catch (Exception e) {
            System.out.println(e);
        }
    }


    public static void main(String[] args) {
        Consumer f = s -> System.out.println(s);
        f.accept("a");
        Income income = new Income();
        String consumerDate = income.getConsumerDate();

        Supplier supplier = () -> income.getConsumerDate();
    }

    @GetMapping("/ger")
    public void ger() throws IOException {
        File file = new File("导出_时段日营收统计_20241024.xlsx");
        ExcelWriterBuilder write = EasyExcel.write(file);
        ExcelWriter writer = write
                .withTemplate(new ClassPathResource("excel/consumption_export_day_merchant_income.xlsx").getInputStream())
                .build();
        writer.fill(getExportData(), FillConfig.builder().forceNewRow(true).build(), EasyExcel.writerSheet(0).build());
        writer.close();
    }

    private List<Income> getExportData() {
        List<Income> re = new ArrayList<>();
        Income income = new Income();
        income.setMerchantName("-");
        income.setConsumerDate("2024");
        income.setConsumptionPersonCount(10L);
        income.setConsumptionCost("100.9");
        income.setConsumptionTime(111L);
        income.setOnlyConsumptionTime(111L);

        Income income2 = new Income();
        income2.setConsumerDate("2025");
        income2.setMerchantName("测试商户");
        income2.setConsumptionPersonCount(12L);
        income2.setConsumptionCost("100.19");
        income2.setConsumptionTime(10L);
        income2.setOnlyConsumptionTime(100L);
        re.add(income);
        re.add(income2);
        return re;


    }

}
