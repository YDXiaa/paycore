package paydemo.service;

import cn.hutool.core.bean.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.biz.PayBiz;
import paydemo.common.RedisKeyEnum;
import paydemo.facade.PayServiceFacade;
import paydemo.facade.model.PayResponseDTO;
import paydemo.facade.model.PayRequestDTO;
import paydemo.manager.model.PayFundBO;
import paydemo.manager.model.PayRequestBO;
import paydemo.manager.model.PayResponseBO;
import paydemo.service.aspect.RequiredLockControl;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 支付服务实现类.
 */
@Service
public class PayServiceImpl implements PayServiceFacade {

    @Autowired
    private PayBiz payBiz;

    /**
     * 支付接口.
     *
     * @param payRequestDTO 支付请求对象.
     * @return 支付响应对象.
     */
    @Override
    @RequiredLockControl(keyPrefix = RedisKeyEnum.PAYCORE_PAY, fields = {"requestBizNo"})
    public Response<PayResponseDTO> pay(PayRequestDTO payRequestDTO) {

        PayRequestBO payRequestBO = BeanUtil.copyProperties(payRequestDTO, PayRequestBO.class);
        payRequestBO.setPayFundBOList(BeanUtil.copyToList(payRequestDTO.getPayFundRequestDTOList(), PayFundBO.class));

        PayResponseBO payResponseBO = payBiz.execute(payRequestBO);

        PayResponseDTO payResponseDTO = BeanUtil.copyProperties(payResponseBO, PayResponseDTO.class);

        return Response.createSuccessResponse(payResponseDTO);
    }
}
