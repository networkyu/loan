package com.loan.service.client;

import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;

public interface ClientService {
    public boolean addClient(ClientModel clientModel);

    //查看客户是否有效，如果有效返回true
    public boolean clientValid(Integer clientId);

    public IntegralModel[] listIntegral();

    public String getNameByClientId(Integer id);

    // 获取从maxId+limit之间的客户model集合
    public ClientModel[] getClientModelsLimitItem(Integer maxId,Integer limit);
    // 返回客户总数。
    public Integer clientSum();
}
