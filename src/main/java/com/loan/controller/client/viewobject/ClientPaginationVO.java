package com.loan.controller.client.viewobject;

import lombok.Data;
import lombok.ToString;

/**
 * 查询用户时候，返回的分页数据。
 */
@ToString
@Data
public class ClientPaginationVO {
    // 总共有多少条数据。
    private Integer sum;
    // 已获取到的数据的最大ID
    private Integer maxId;
    // 客户模型。
    private ClientVO[] clientVOS;
}
