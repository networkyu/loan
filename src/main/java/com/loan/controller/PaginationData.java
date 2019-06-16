package com.loan.controller;

import lombok.Data;

@Data
public class PaginationData {
    // 总条数
    private Integer sum;
    // 当前为第几页
    private Integer page;
    // 返回的数据。
    private Object object;
}
