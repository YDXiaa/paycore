package paydemo;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.LockSupport;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.*;
import paydemo.facade.PayServiceFacade;
import paydemo.facade.model.PayFundRequestDTO;
import paydemo.facade.model.PayRequestDTO;

/**
 * @auther YDXiaa
 *
 * 支付测试.
 */
public class PayTest extends BaseSpringBootSupport{

    @Autowired
    private PayServiceFacade payServiceImpl;

    @Test
    public void testPay(){

        PayRequestDTO payRequestDTO = new PayRequestDTO();
        payRequestDTO.setRequestBizNo(UUID.randomUUID().toString());
        payRequestDTO.setRequestDate(new Date());
        payRequestDTO.setBizLine(BizLineEnum.MALL.getBizCode());
        payRequestDTO.setPayAmt(300L);
        payRequestDTO.setPayType(PayTypeEnum.PAY.getPayTypeCode());
        payRequestDTO.setOrderRemark("农夫山泉矿泉水");
        payRequestDTO.setPayerLoginNo("wx_xxxx");
        payRequestDTO.setPayerUserNo("3000000000");
        payRequestDTO.setPayerCustomerNo("6000000000000");


        List<PayFundRequestDTO> payFundRequestDTOList = Lists.newArrayList();

        PayFundRequestDTO alipayFundRequestDTO = new PayFundRequestDTO();
        alipayFundRequestDTO.setPayAmt(100L);
        alipayFundRequestDTO.setPayTool(PayToolEnum.THIRD_PAY.getPayToolCode());
        alipayFundRequestDTO.setPaySubTool(PaySubToolEnum.WECHATPAY.getPaySubToolCode());
        alipayFundRequestDTO.setPayType(PayTypeEnum.PAY.getPayTypeCode());
        alipayFundRequestDTO.setBizType(BizTypeEnum.PC_ONLINE.getBizTypeCode());
        alipayFundRequestDTO.setExtAuthUserId("wx_xxxxxxxxx");


        PayFundRequestDTO marktingFundRequestDTO = new PayFundRequestDTO();
        marktingFundRequestDTO.setPayAmt(200L);
        marktingFundRequestDTO.setPayTool(PayToolEnum.MARKETING.getPayToolCode());
        marktingFundRequestDTO.setPaySubTool(PaySubToolEnum.POINTS.getPaySubToolCode());
        marktingFundRequestDTO.setPayType(PayTypeEnum.PAY.getPayTypeCode());
        marktingFundRequestDTO.setCouponNo("C99999999999");

        payFundRequestDTOList.add(alipayFundRequestDTO);
        payFundRequestDTOList.add(marktingFundRequestDTO);

        payRequestDTO.setPayFundRequestDTOList(payFundRequestDTOList);

        payServiceImpl.pay(payRequestDTO);

//        LockSupport.park();
    }
}
