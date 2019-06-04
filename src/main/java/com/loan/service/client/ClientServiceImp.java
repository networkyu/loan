package com.loan.service.client;

import com.loan.dao.ClientMapper;
import com.loan.dataobject.Client;
import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 客户管理功能
 */
@Service
public class ClientServiceImp implements ClientService {
    @Autowired
    ClientMapper clientMapper;

    @Override
    public boolean addClient(ClientModel clientModel) {
        Client client = convertFromModel(clientModel);
        if(clientMapper.insertSelective(client) < 1){
            return false;
        }
        return true;
    }

    @Override
    public boolean clientValid(Integer clientId) {
        Client client = clientMapper.selectByPrimaryKey(clientId);
        if (client == null){
            return false;
        }
        return true;
    }

    private Client convertFromModel(ClientModel clientModel){
        Client client = new Client();
        BeanUtils.copyProperties(clientModel,client);
        return client;
    }

    @Override
    public List<IntegralModel> listIntegral() {
        // 不考虑分页查询的情况下：
        // 获得所有客户id的数组。
        List<Integer> clientIdList = clientMapper.selectId();
        Integer[] clients = (Integer[]) clientIdList.toArray();
        int clintMount = clients.length;
        IntegralModel[] integralModelArr = new IntegralModel[clintMount];
        for (int i = 0;i<clintMount;i++){
            //通过id查询积分和
            int outValue = 3;
            int enterValue = 5;
            IntegralModel model = new IntegralModel();
            model.setOutValue(outValue);
            model.setEnterValue(enterValue);
            model.setClient(clients[i]);

        }
        return null;
    }
}
