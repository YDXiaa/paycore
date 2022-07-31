package paydemo.facade;

import paydemo.facade.model.IssuePayRequestDTO;
import paydemo.facade.model.PayResponseDTO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 代付打款到用户支付宝、微信账户中.
 */
public interface IssueServiceFacade {


    /**
     * 代付转账.
     *
     * @param requestDTO 请求对象.
     * @return 支付响应.
     */
    Response<PayResponseDTO> issuePay(IssuePayRequestDTO requestDTO);


}
