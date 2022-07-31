package paydemo;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import paydemo.biz.route.RouteBiz;
import paydemo.common.BizTypeEnum;
import paydemo.common.SysConstant;
import paydemo.dao.dbmodel.ChannelDetailFeeDO;
import paydemo.dao.dbmodel.ChannelDetailInfoDO;
import paydemo.dao.dbmodel.ChannelInfoDO;
import paydemo.manager.db.ChannelDetailInfoManager;
import paydemo.manager.db.ChannelInfoManager;
import paydemo.manager.model.ChannelDetailInfoBO;
import paydemo.manager.model.PayBaseInfoBO;
import paydemo.manager.remote.thirdpay.impl.directwechat.WechatCancelAdapter;
import paydemo.manager.remote.thirdpay.impl.directwechat.WechatPayAdapter;
import paydemo.manager.remote.thirdpay.impl.directwechat.WechatPayQueryAdapter;
import paydemo.util.FlagsEnum;

import java.math.BigDecimal;

/**
 * @auther YDXiaa
 * <p>
 * 路由渠道信息.
 */
public class RouteChannelTest extends BaseSpringBootSupport{

    @Autowired
    private ChannelInfoManager channelInfoManager;

    @Autowired
    private ChannelDetailInfoManager channelDetailInfoManager;

    @Autowired
    private RouteBiz routeBiz;


    @Test
    public void addChannelInfo(){

//        ChannelInfoDO channelInfoDO = new ChannelInfoDO();
//
//        channelInfoDO.setChannelNo("DIRECT_WECHAT");
//        channelInfoDO.setChannelName("直连微信");
//        channelInfoDO.setChannelDesc("直连微信业务");
//        channelInfoDO.setChannelContactName("微信商务");
//        channelInfoDO.setChannelContactPhone("test");
//        channelInfoDO.setChannelWeight(-1L);
//        channelInfoDO.setNoServiceDesc("NORMAL");
//        channelInfoDO.setNoServiceBeginTime(null);
//        channelInfoDO.setNoServiceEndTime(null);
//        channelInfoDO.setChannelStatus("OPEN");
//
//        channelInfoManager.addChannelInfo(channelInfoDO);

        ChannelInfoDO channelInfoDO = new ChannelInfoDO();

        channelInfoDO.setChannelNo("ADAPAY_WECHAT");
        channelInfoDO.setChannelName("汇付聚合支付-微信");
        channelInfoDO.setChannelDesc("汇付聚合支付-微信");
        channelInfoDO.setChannelContactName("汇付聚合支付商务");
        channelInfoDO.setChannelContactPhone("test");
        channelInfoDO.setChannelWeight(-1L);
        channelInfoDO.setNoServiceDesc("NORMAL");
        channelInfoDO.setNoServiceBeginTime(null);
        channelInfoDO.setNoServiceEndTime(null);
        channelInfoDO.setChannelStatus("OPEN");

        channelInfoManager.addChannelInfo(channelInfoDO);
    }

    @Test
    public void addChannelDetailInfo(){

        ChannelDetailInfoDO channelDetailInfoDO = new ChannelDetailInfoDO();
        channelDetailInfoDO.setChannelNo("DIRECT_WECHAT");
        channelDetailInfoDO.setChannelDetailNo("DIRECT_WECHAT_REVOKE_QUERY");
        channelDetailInfoDO.setChannelDetailName("直连微信支付冲正查询场景");
        channelDetailInfoDO.setPayType("REVOKE");
        channelDetailInfoDO.setBizType(BizTypeEnum.ORDER_QUERY.getBizTypeCode());
        channelDetailInfoDO.setPayTool("THIRD_PAY");
        channelDetailInfoDO.setPaySubTool("WECHATPAY");
        channelDetailInfoDO.setMinPayAmt(-1L);
        channelDetailInfoDO.setMaxPayAmt(-1L);
        channelDetailInfoDO.setClassPackageName(WechatPayQueryAdapter.class.getCanonicalName());
        channelDetailInfoDO.setChannelSeqLen(32L);
        channelDetailInfoDO.setMethodName(SysConstant.FIXED_SERVICE_NAME);
        channelDetailInfoDO.setFeeFlag(FlagsEnum.FALSE.getFlagCode());
        channelDetailInfoDO.setDiscountFlag(FlagsEnum.FALSE.getFlagCode());
        channelDetailInfoDO.setChannelDetailStatus("OPEN");

//        ChannelDetailFeeDO channelDetailFeeDO = new ChannelDetailFeeDO();
//        channelDetailFeeDO.setChannelNo("DIRECT_WECHAT");
//        channelDetailFeeDO.setChannelDetailNo("DIRECT_WECHAT_PAY");
//        channelDetailFeeDO.setFeeCalMode("SINGLE");
//        channelDetailFeeDO.setBillingType("RATE");
//        channelDetailFeeDO.setBillingValue(new BigDecimal("0.06")); // 单笔6%
//        channelDetailFeeDO.setFeeBeginAmt(1L);
//        channelDetailFeeDO.setFeeEndAmt(999999999L);
//        channelDetailFeeDO.setFeeMinAmt(1L);
//        channelDetailFeeDO.setFeeMaxAmt(9999999999L);
//        channelDetailFeeDO.setFeeCalAccuracy("ROUND");
//
//        channelDetailInfoManager.addChannelDetailInfo(channelDetailInfoDO,channelDetailFeeDO);

//        ChannelDetailInfoDO channelDetailInfoDO = new ChannelDetailInfoDO();
//        channelDetailInfoDO.setChannelNo("ADAPAY_WECHAT");
//        channelDetailInfoDO.setChannelDetailNo("ADAPAY_WECHAT_PAY");
//        channelDetailInfoDO.setChannelDetailName("汇付聚合支付-微信-支付");
//        channelDetailInfoDO.setPayType("PAY");
//        channelDetailInfoDO.setBizType("PC_ONLINE");
//        channelDetailInfoDO.setPayTool("THIRD_PAY");
//        channelDetailInfoDO.setPaySubTool("WECHATPAY");
//        channelDetailInfoDO.setMinPayAmt(1L);
//        channelDetailInfoDO.setMaxPayAmt(999999999L);
//        channelDetailInfoDO.setClassPackageName(WechatPayQueryAdapter.class.getCanonicalName());
//        channelDetailInfoDO.setChannelSeqLen(32L);
//        channelDetailInfoDO.setMethodName(SysConstant.FIXED_SERVICE_NAME);
//        channelDetailInfoDO.setFeeFlag(FlagsEnum.TRUE.getFlagCode());
//        channelDetailInfoDO.setDiscountFlag(FlagsEnum.TRUE.getFlagCode());
//        channelDetailInfoDO.setChannelDetailStatus("OPEN");
//
//        ChannelDetailFeeDO channelDetailFeeDO = new ChannelDetailFeeDO();
//        channelDetailFeeDO.setChannelNo("ADAPAY_WECHAT");
//        channelDetailFeeDO.setChannelDetailNo("ADAPAY_WECHAT_PAY");
//        channelDetailFeeDO.setFeeCalMode("SINGLE");
//        channelDetailFeeDO.setBillingType("RATE");
//        channelDetailFeeDO.setBillingValue(new BigDecimal("0.03")); // 单笔6%
//        channelDetailFeeDO.setFeeBeginAmt(1L);
//        channelDetailFeeDO.setFeeEndAmt(999999999L);
//        channelDetailFeeDO.setFeeMinAmt(1L);
//        channelDetailFeeDO.setFeeMaxAmt(9999999999L);
//        channelDetailFeeDO.setFeeCalAccuracy("ROUND");

        channelDetailInfoManager.addChannelDetailInfo(channelDetailInfoDO,null);
    }


    @Test
    public void routeTest(){

        PayBaseInfoBO payBaseInfoBO = new PayBaseInfoBO();
        payBaseInfoBO.setPayAmt(200L);
        payBaseInfoBO.setPayTool("THIRD_PAY");
        payBaseInfoBO.setPaySubTool("WECHATPAY");
        payBaseInfoBO.setBizType("PC_ONLINE");
        payBaseInfoBO.setPayType("PAY");

        ChannelDetailInfoBO channelDetailInfoBO = routeBiz.route(payBaseInfoBO);

        System.out.println(channelDetailInfoBO);
    }

}
