package paydemo.service;

import cn.hutool.core.bean.BeanUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.biz.PayQueryBiz;
import paydemo.facade.PayQueryServiceFacade;
import paydemo.facade.model.PayQueryRequestDTO;
import paydemo.facade.model.PayResponseDTO;
import paydemo.manager.model.PayRequestBO;
import paydemo.manager.model.PayResponseBO;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 支付查询服务.
 */
@Service
public class PayQueryServiceImpl implements PayQueryServiceFacade {

    @Autowired
    private PayQueryBiz payQueryBiz;

    @Override
    public Response<PayResponseDTO> payQuery(PayQueryRequestDTO requestDTO) {
        PayResponseBO payResponseBO = payQueryBiz.queryPayOrder(BeanUtil.copyProperties(requestDTO, PayRequestBO.class));
        return Response.createSuccessResponse(BeanUtil.copyProperties(payResponseBO, PayResponseDTO.class));
    }
}
