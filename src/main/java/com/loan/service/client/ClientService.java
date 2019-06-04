package com.loan.service.client;

import com.loan.dataobject.Client;
import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;

import java.util.List;

public interface ClientService {
    public boolean addClient(ClientModel clientModel);

    //查看客户是否有效，如果有效返回true
    public boolean clientValid(Integer clientId);

    public List<IntegralModel> listIntegral();
}
