package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.BizLineEnum;
import paydemo.common.PayTypeEnum;
import paydemo.common.RefundTypeEnum;
import paydemo.facade.RefundServiceFacade;
import paydemo.facade.model.RefundRequestDTO;

import java.util.Date;
import java.util.UUID;

/**
 * @auther YDXiaa
 * <p>
 * 退款测试.
 */
public class RefundTest extends BaseSpringBootSupport {


    @Autowired
    private RefundServiceFacade refundServiceFacade;

    @Test
    public void testRefund(){

        RefundRequestDTO requestDTO = new RefundRequestDTO();
        requestDTO.setOrigRequestBizNo("980a47ed-50bc-4c25-9ef3-6e112fb88165");
        requestDTO.setOrigRequestDate(new Date());
        requestDTO.setOrigBizLine(BizLineEnum.MALL.getBizCode());
        requestDTO.setRefundType(RefundTypeEnum.REFUND_ORIG_PAY_TOOL.getRefundTypeCode());
        requestDTO.setRefundReason("测试退款");
        requestDTO.setRequestBizNo(UUID.randomUUID().toString());
        requestDTO.setRequestDate(new Date());
        requestDTO.setBizLine(BizLineEnum.MALL.getBizCode());
        requestDTO.setPayAmt(150L);
        requestDTO.setPayType(PayTypeEnum.REFUND.getPayTypeCode());


        refundServiceFacade.refund(requestDTO);

    }
}
