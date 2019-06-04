package com.loan.model.client;

import lombok.Data;
import lombok.ToString;

import java.util.Date;
@Data
@ToString
/**
 * 只需要总和，如果需要单独项目，可以通过客户号再去查询。
 */
public class IntegralModel {

    private Integer outValue;

    private Integer client;

    private Integer enterValue;

}
