package com.loan.controller.client.viewobject;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class ClientVO {
    // ID
    private Integer id;
    // 头像地址。
    private String avatarUrl;

    private String name;
    private String tel;

    private String eMail;

    private String identityTailNumber;
}
