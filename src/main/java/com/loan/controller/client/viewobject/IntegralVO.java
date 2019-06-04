package com.loan.controller.client.viewobject;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class IntegralVO {
    private Integer outValue;

    private Integer client;

    private Integer enterValue;

    // 客户姓名
    private String name;
}
