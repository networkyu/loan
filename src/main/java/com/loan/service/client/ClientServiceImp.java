package com.loan.service.client;

import com.loan.controller.client.viewobject.ClientPreviewVO;
import com.loan.controller.client.viewobject.ClientVO;
import com.loan.controller.loan.viewobjct.LoanView;
import com.loan.dao.ClientMapper;
import com.loan.dao.RepaymentIntegralMapper;
import com.loan.dataobject.Client;
import com.loan.dataobject.Loan;
import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;
import com.loan.service.loan.LoanService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 客户管理功能
 */
@Service
public class ClientServiceImp implements ClientService {
    @Autowired
    ClientMapper clientMapper;

    @Autowired
    RepaymentIntegralMapper repaymentIntegralMapper;

    @Autowired
    LoanService loanService;

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
    public List<Integer> getIdByName(String name) {
        return clientMapper.selectIdByName(name);
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

    @Override
    public ClientModel[] getClientModelsByPageAndLimit(Integer page, Integer limit) {
        // 首先验证一下页面是否超出范围
        Integer total = clientSum();
        if (page > Math.ceil(total / limit)){
            page = (int)Math.ceil(total/limit);
        }
        List<Client> clients = clientMapper.selectByStartRowAndLimit((page-1)*limit,limit);
        ClientModel[] clientModels = new ClientModel[clients.size()];
        for (int i=0;i<clients.size();i++){
            clientModels[i] = this.convertFromClient(clients.get(i));
        }
        return clientModels;
    }

    @Override
    public ClientPreviewVO[] getClientVos(Integer page, Integer limit) {
        Integer total = clientSum();
        if (page > Math.ceil(total / limit)){
            page = (int)Math.ceil(total/limit);
        }
        List<Client> clients = clientMapper.selectByStartRowAndLimit((page-1)*limit,limit);
//        ClientVO[] clientVOS = new ClientVO[clients.size()];
        ClientPreviewVO[] clientPreviewVOS = new ClientPreviewVO[clients.size()];
        for (int i=0;i<clients.size();i++){
            // 设置客户模型
            clientPreviewVOS[i].setClientVO(convertClientVoFromClient(clients.get(i)));
            //查询出所有记录
            List<Loan> loans = loanService.getLoansByClientId(clients.get(i).getId());
            // 设置最新的一条借款记录
            Loan loan = loans.get(loans.size() -1);
            LoanView loanView = new LoanView();
            loanView.setLoanId(loan.getId());
            loanView.setBorrwoerName(clientMapper.selectName(loan.getBorrower()));
            loanView.setLenderName(clientMapper.selectName(loan.getLender()));
            loanView.setLoanTimeStr(loan.getBorrwwingTime());
            loanView.setMoney(loan.getMoney());
            clientPreviewVOS[i].setSimpleLoanView(loanView);

        }
        return new ClientPreviewVO[0];
    }
    // 从客户数据模型直接转到ClientVo
    public ClientVO convertClientVoFromClient(Client client){
        ClientVO clientVO = new ClientVO();
        BeanUtils.copyProperties(client,clientVO);
        String IDNmuber = client.getIdentityNumber();
        if (IDNmuber.length()>5){
            clientVO.setIdentityTailNumber(IDNmuber.substring(IDNmuber.length()-4,IDNmuber.length()));
        }
        clientVO.setAvatarUrl("");
        return clientVO;
    }

    @Override
    public List<Client> selectAll() {
        return clientMapper.selectAll();
    }
}
