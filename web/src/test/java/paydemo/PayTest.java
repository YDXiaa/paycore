package paydemo;
import com.google.common.collect.Lists;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.BizLineEnum;
import paydemo.common.PaySubToolEnum;
import paydemo.common.PayToolEnum;
import paydemo.common.PayTypeEnum;
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


        List<PayFundRequestDTO> payFundRequestDTOList = Lists.newArrayList();

        PayFundRequestDTO alipayFundRequestDTO = new PayFundRequestDTO();
        alipayFundRequestDTO.setPayAmt(100L);
        alipayFundRequestDTO.setPayTool(PayToolEnum.THIRD_PAY.getPayToolCode());
        alipayFundRequestDTO.setPaySubTool(PaySubToolEnum.ALIPAY.getPaySubToolCode());
        alipayFundRequestDTO.setPayType(PayTypeEnum.PAY.getPayTypeCode());


        PayFundRequestDTO marktingFundRequestDTO = new PayFundRequestDTO();
        marktingFundRequestDTO.setPayAmt(200L);
        marktingFundRequestDTO.setPayTool(PayToolEnum.MARKETING.getPayToolCode());
        marktingFundRequestDTO.setPaySubTool(PaySubToolEnum.POINTS.getPaySubToolCode());
        marktingFundRequestDTO.setPayType(PayTypeEnum.PAY.getPayTypeCode());

        payFundRequestDTOList.add(alipayFundRequestDTO);
        payFundRequestDTOList.add(marktingFundRequestDTO);

        payRequestDTO.setPayFundRequestDTOList(payFundRequestDTOList);

        payServiceImpl.pay(payRequestDTO);
    }
}
