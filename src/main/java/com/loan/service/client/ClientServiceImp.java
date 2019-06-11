package com.loan.service.client;

import com.loan.dao.ClientMapper;
import com.loan.dao.RepaymentIntegralMapper;
import com.loan.dataobject.Client;
import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 客户管理功能
 */
@Service
public class ClientServiceImp implements ClientService {
    @Autowired
    ClientMapper clientMapper;

    @Autowired
    RepaymentIntegralMapper repaymentIntegralMapper;

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

    /**
     * clientModel->client
     * @param clientModel
     * @return
     */
    private Client convertFromModel(ClientModel clientModel){
        Client client = new Client();
        BeanUtils.copyProperties(clientModel,client);
        return client;
    }

    /**
     * client->clientModel
     * @param client
     * @return
     */
    private ClientModel convertFromClient(Client client){
        ClientModel clientModel = new ClientModel();
        BeanUtils.copyProperties(client,clientModel);
        return clientModel;
    }

    /**
     * 获取客户总数。
     * @return
     */
    public Integer clientSum(){
        return clientMapper.selectCountId();
    }
    @Override
    public IntegralModel[] listIntegral() {
        // 不考虑分页查询的情况下：
        // 获得所有客户id的数组。
        List<Integer> clientIdList = clientMapper.selectId();
        Integer[] clients = clientIdList.toArray(new Integer[clientIdList.size()]);
        int clintMount = clients.length;
        IntegralModel[] integralModelArr = new IntegralModel[clintMount];
        for (int i = 0;i<clintMount;i++){
            IntegralModel model = new IntegralModel();
            model.setOutValue(repaymentIntegralMapper.selectOutValueByClient(clients[i]));
            model.setEnterValue(repaymentIntegralMapper.selectEnterValueByClient(clients[i]));
//            Map<String,BigDecimal> result = repaymentIntegralMapper.selectValueMapByClient(clients[i]);
//            if (result != null){
//                // 查询到记录
//                model.setEnterValue(result.get("sum(enter_value)"));
//                model.setOutValue(result.get("sum(out_value)"));
//            }
            model.setClient(clients[i]);
            integralModelArr[i] = model;
        }
        return integralModelArr;
    }

    @Override
    public String getNameByClientId(Integer id) {
        return clientMapper.selectName(id);
    }
    @Override
    public ClientModel[] getClientModelsLimitItem(Integer maxId,Integer limit) {
        List<Client> clients = clientMapper.selectByMaxIdAndLimit(maxId,limit);
        ClientModel[] clientModels = new ClientModel[clients.size()];
        for (int i=0;i<clients.size();i++){
            clientModels[i] = this.convertFromClient(clients.get(i));
        }
        return clientModels;
    }
}
