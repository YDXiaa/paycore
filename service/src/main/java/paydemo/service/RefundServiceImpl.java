package paydemo.service;

import cn.hutool.core.bean.BeanUtil;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.biz.RefundBiz;
import paydemo.common.RedisKeyEnum;
import paydemo.facade.RefundServiceFacade;
import paydemo.facade.model.PayResponseDTO;
import paydemo.facade.model.RefundRequestDTO;
import paydemo.manager.model.PayResponseBO;
import paydemo.manager.model.RefundRequestBO;
import paydemo.service.aspect.RequiredLockControl;
import paydemo.util.Response;

/**
 * @auther YDXiaa
 * <p>
 * 退款服务.
 */
@Service
public class RefundServiceImpl implements RefundServiceFacade {

    @Autowired
    private RefundBiz refundBiz;

    @Override
    @RequiredLockControl(keyPrefix = RedisKeyEnum.PAYCORE_PAY, fields = {"requestBizNo"})
    public Response<PayResponseDTO> refund(RefundRequestDTO requestDTO) {

        RefundRequestBO refundRequestBO = BeanUtil.copyProperties(requestDTO, RefundRequestBO.class);

        PayResponseBO payResponseBO = refundBiz.execute(refundRequestBO);

        PayResponseDTO payResponseDTO = BeanUtil.copyProperties(payResponseBO, PayResponseDTO.class);

        return Response.createSuccessResponse(payResponseDTO);
    }
}
