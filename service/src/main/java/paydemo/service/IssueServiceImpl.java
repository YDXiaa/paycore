package paydemo.service;

import org.apache.dubbo.config.annotation.Service;
import paydemo.facade.IssueServiceFacade;
import paydemo.facade.model.IssuePayRequestDTO;
import paydemo.facade.model.PayResponseDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 代付服务 代付打款到用户支付宝、微信账户、银行卡中.
 * 例如支付宝账户转账接口:https://opendocs.alipay.com/open/02byuo
 * 微信转账.
 */
@Service
public class IssueServiceImpl implements IssueServiceFacade {

    @Override
    public Response<PayResponseDTO> issuePay(IssuePayRequestDTO requestDTO) {
        return null;
    }
}
