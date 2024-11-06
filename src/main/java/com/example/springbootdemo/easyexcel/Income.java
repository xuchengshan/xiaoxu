package com.example.springbootdemo.easyexcel;

import lombok.Data;

@Data
public class Income {

    private String serialNumber;

    private String merchantName;

    private String consumerDate;

    private Long consumptionPersonCount;

    private String consumptionCost;

    private Long consumptionTime;

    private Long onlyConsumptionTime;

}
