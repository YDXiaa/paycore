package paydemo;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.common.*;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.manager.db.PayOrderManager;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @auther YDXiaa
 * <p>
 * mybatis plus test.
 */
public class MybatisPlusTest extends BaseSpringBootSupport {

    @Autowired
    private PayOrderManager payOrderManager;

    @Test
    public void testInsertPayOrder(){

        PayOrderDO payOrderDO = new PayOrderDO();
        payOrderDO.setRequestBizNo(UUID.randomUUID().toString());
        payOrderDO.setRequestDate(new Date());
        payOrderDO.setBizLine(BizLineEnum.SCM.getBizCode());
        payOrderDO.setPayNo(UUID.randomUUID().toString());
        payOrderDO.setPayAmt(1L);
        payOrderDO.setRefundAmt(0L);
        payOrderDO.setPayType(PayTypeEnum.PAY.getPayTypeCode());
        payOrderDO.setPayStatus(PayStatusEnum.INIT.getStatusCode());
        payOrderDO.setPayCreateDate(new Date());
        payOrderDO.setPayCompletedDate(new Date());
        payOrderDO.setOrigPayNo(null);
        payOrderDO.setOrderRemark("testOrder");
        payOrderDO.setCreateUser(SysConstant.SYS_USER);
        payOrderDO.setCreateDate(new Date());
        payOrderDO.setUpdateUser(SysConstant.SYS_USER);
        payOrderDO.setUpdateDate(new Date());


        List<PayFundDO> payFundDOList = Lists.newArrayList();

        PayFundDO  payFundDO = new PayFundDO();
        payFundDO.setPayNo(payOrderDO.getPayNo());
        payFundDO.setPayFundNo(UUID.randomUUID().toString());
        payFundDO.setPayCreateDate(new Date());
        payFundDO.setPayCompletedDate(new Date());
        payFundDO.setOrigPayFundNo(null);
        payFundDO.setPayType(PayTypeEnum.PAY.getPayTypeCode());
        payFundDO.setPayAmt(1L);
        payFundDO.setRefundAmt(0L);
        payFundDO.setPayTool(PayToolEnum.THIRD_PAY.getPayToolCode());
        payFundDO.setPaySubTool(PaySubToolEnum.QQPAY.getPaySubToolCode());
        payFundDO.setPaySeq(1L);
        payFundDO.setPayStatus(PayStatusEnum.INIT.getStatusCode());
        payFundDO.setRevokeFlag("FALSE");
        payFundDO.setCreateUser(SysConstant.SYS_USER);
        payFundDO.setCreateDate(new Date());
        payFundDO.setUpdateUser(SysConstant.SYS_USER);
        payFundDO.setUpdateDate(new Date());

        payFundDOList.add(payFundDO);

        payOrderManager.addPayOrder(payOrderDO,payFundDOList);
    }

}
