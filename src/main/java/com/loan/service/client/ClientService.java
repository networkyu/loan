package com.loan.service.client;

import com.loan.controller.client.viewobject.ClientPreviewVO;
import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;

public interface ClientService {
    public boolean addClient(ClientModel clientModel);

    //查看客户是否有效，如果有效返回true
    public boolean clientValid(Integer clientId);

    public IntegralModel[] listIntegral();

    public String getNameByClientId(Integer id);

    // 获取从maxId->limit之间的客户model集合
    public ClientModel[] getClientModelsLimitItem(Integer maxId,Integer limit);
    // 返回客户总数。
    public Integer clientSum();
    // 通过页数和显示的条数返回客户model集合。
    public  ClientModel[] getClientModelsByPageAndLimit(Integer page,Integer limit);

    // 通过起始页和需要条数返回客户预览模型数组。
    public ClientPreviewVO[] getClientVos(Integer page,Integer limit);

}
