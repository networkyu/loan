package com.loan.controller.client;

import com.loan.controller.client.viewobject.ClientPaginationVO;
import com.loan.controller.client.viewobject.ClientVO;
import com.loan.controller.client.viewobject.IntegralVO;
import com.loan.error.BussinessException;
import com.loan.error.EmBussinessError;
import com.loan.model.client.ClientModel;
import com.loan.model.client.IntegralModel;
import com.loan.response.CommonReturnType;
import com.loan.service.client.ClientService;
import com.loan.validator.ValidationResult;
import com.loan.validator.ValidatorImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * 客户管理功能
 */
@Controller
@RequestMapping("/client")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*")
public class ClientController {

    @Autowired
    private ValidatorImpl validator ;

    @Autowired
    private ClientService clientService;
    // 添加用户
    @RequestMapping(value = "/add",method = {RequestMethod.POST})
    @ResponseBody
    public CommonReturnType addClient(@RequestParam(name = "name") String name, @RequestParam(name="tel") String tel, @RequestParam(name="idCardNumber") String idCardNumber,@RequestParam(name="eMail") String eMail) throws UnsupportedEncodingException, NoSuchAlgorithmException, BussinessException {
        // 验证参数
        ClientModel clientModel = new ClientModel();
        clientModel.setName(name);
        clientModel.setTel(tel);
        clientModel.seteMail(eMail);
        clientModel.setIdentityNumber(idCardNumber);
        ValidationResult result = validator.validate(clientModel);
        if (result.isHasErrors() || clientModel==null){
            throw new BussinessException(EmBussinessError.PARAMETER_VALIDATION_ERROR,result.getErrMsg());
        }
        if(!clientService.addClient(clientModel)){
            throw new BussinessException(EmBussinessError.UNKNOWN_ERROR,result.getErrMsg());
        }
        return CommonReturnType.create(null);
    }
    //获取用户借出积分和借入积分排序{id:2,name:王小，data:{}}
    @RequestMapping(value = "/listintegra",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listIntegra(){
        IntegralModel[] models = clientService.listIntegral();
        IntegralVO[] integralVOS = convertIntegralVoFrom(models);
        return CommonReturnType.create(integralVOS);
    }
    // 通过积分模型返回给前端的视图模型
    public IntegralVO[] convertIntegralVoFrom(IntegralModel[] integralModels){
        IntegralVO[] integralVOs = new IntegralVO[integralModels.length];
        // 通过
        for (int i=0;i<integralModels.length;i++){
            IntegralModel integralModel = integralModels[i];
            Integer clientId = integralModel.getClient();
            IntegralVO integralVO = new IntegralVO();
            integralVO.setClient(clientId);
            // 重点：添加姓名。
            integralVO.setName(clientService.getNameByClientId(clientId));
            if (integralModel.getEnterValue()==null){
                integralVO.setEnterValue(0); //将null变为可视化0
            } else {
                integralVO.setEnterValue(integralModel.getEnterValue());
            }
            if (integralModel.getOutValue()==null){
                integralVO.setOutValue(0);
            } else {
                integralVO.setOutValue(integralModel.getOutValue());
            }
            integralVOs[i] = integralVO;
        }
        return integralVOs;
    }
    /**
     *返回给前端客户模型。
     */
    @RequestMapping(value = "/listclient",method = {RequestMethod.GET})
    @ResponseBody
    public CommonReturnType listClient(@RequestParam(name = "maxId") Integer maxId,@RequestParam(name = "limit") Integer limit){
        // 获取用户模型。
        ClientModel[] clientModels = clientService.getClientModelsLimitItem(maxId,limit);
        // 查询数据库中总共有多少条数据。
        Integer sum = clientService.clientSum();
        ClientPaginationVO clientPVO = new ClientPaginationVO();
        clientPVO.setSum(sum);
        clientPVO.setMaxId(clientModels[clientModels.length-1].getId());
        clientPVO.setClientVOS(this.convertFromClientModelArr(clientModels));
        return CommonReturnType.create(clientPVO);
    }

    /**
     * 将客户模型转换为前端用户视图模型。
     * @param clientModels
     * @return
     */
    public ClientVO[] convertFromClientModelArr(ClientModel[] clientModels){
        ClientVO[] clientVOS = new ClientVO[clientModels.length];
        for (int i = 0;i<clientModels.length;i++){
            ClientVO clientVO = new ClientVO();
            BeanUtils.copyProperties(clientModels[i],clientVO);
            String IDNmuber = clientModels[i].getIdentityNumber();
            if (IDNmuber.length()>5){
                clientVO.setIdentityTailNumber(IDNmuber.substring(IDNmuber.length()-4,IDNmuber.length()));
            }
            clientVOS[i] = clientVO;
            clientVOS[i].setAvatarUrl("");/*从其他服务获取头像未写。*/
        }
        return clientVOS;
    }



}
