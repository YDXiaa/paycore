package paydemo.manager.db;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paydemo.common.SysConstant;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.dao.dbmodel.PayJobDetailDO;
import paydemo.dao.dbmodel.PayOrderDO;
import paydemo.dao.mapper.PayOrderMapper;
import paydemo.dao.mapper.TableNameHelper;
import paydemo.util.PayStatusEnum;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @auther YDXiaa
 * 支付单业务.
 */
@Repository
public class PayOrderManager {

    @Autowired
    private PayOrderMapper payOrderMapper;

    @Autowired
    private PayFundManager payFundManager;

    @Autowired
    private PayJobDetailManager payJobDetailManager;

    /**
     * 插入订单.
     *
     * @param payOrderDO    支付单信息.
     * @param payFundDOList 资金单信息.
     */
    @Transactional(rollbackFor = Throwable.class)
    public void addPayOrder(PayOrderDO payOrderDO, List<PayFundDO> payFundDOList) {
        TableNameHelper.setShardingMark(payOrderDO.getPayNo());
        payOrderMapper.insert(payOrderDO);
        payFundManager.batchAddPayFund(payFundDOList);
    }


    /**
     * 查询订单信息.
     *
     * @param requestBizNo 请求业务流水号.
     * @param requestDate  请求日期.
     * @param bizLine      业务线.
     * @return 支付单信息.
     */
    public PayOrderDO queryPayOrder(String requestBizNo, Date requestDate, String bizLine) {

        LambdaQueryWrapper<PayOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayOrderDO::getRequestBizNo, requestBizNo)
                .eq(PayOrderDO::getBizLine, bizLine);

        // 设置动态分表.
        TableNameHelper.setShardingMark(requestDate);

        return payOrderMapper.selectOne(queryWrapper);
    }

    /**
     * 冲正场景下更新订单状态.
     *
     * @param payOrderDO                 支付单状态.
     * @param toClosePayFundList         订单设置CLOSE状态.
     * @param asynRevokePayJobDetailList 支付冲正单.
     */
    @Transactional(rollbackFor = Throwable.class)
    public void modifyRevokePayStatus(PayOrderDO payOrderDO, List<PayFundDO> toClosePayFundList, List<PayFundDO> toRevokePayFundList,
                                      List<PayJobDetailDO> asynRevokePayJobDetailList) {
        // 更新支付主单.
        modifyPayOrderStatus(payOrderDO, PayStatusEnum.PAYING);
        // 关闭资金单.
        payFundManager.batchModifyPayFund2Close(toClosePayFundList);
        // 冲正资金单.
        payFundManager.batchAddPayFund(toRevokePayFundList);
        // 新增冲正作业任务单.
        payJobDetailManager.batchAddJobDetail(asynRevokePayJobDetailList);
    }

    /**
     * 修改支付订单状态.
     *
     * @param payOrderDO        支付主单DO.
     * @param origPayStatusEnum 原支付状态,乐观锁标记位.
     */
    public int modifyPayOrderStatus(PayOrderDO payOrderDO, PayStatusEnum origPayStatusEnum) {

        TableNameHelper.setShardingMark(payOrderDO.getPayNo());
        LambdaUpdateWrapper<PayOrderDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PayOrderDO::getPayStatus, payOrderDO.getPayStatus())
                .set(Objects.nonNull(payOrderDO.getPayCompletedDate()), PayOrderDO::getPayCompletedDate, payOrderDO.getPayCompletedDate())
                .set(PayOrderDO::getUpdateDate, new Date())
                .set(PayOrderDO::getUpdateUser, SysConstant.SYS_USER)
                .eq(PayOrderDO::getPayNo, payOrderDO.getPayNo())
                .eq(PayOrderDO::getPayStatus, origPayStatusEnum); // 乐观锁.

        return payOrderMapper.update(payOrderDO, updateWrapper);
    }

    /**
     * 查询支付单.
     *
     * @param payNo 支付单号.
     * @return 支付单信息.
     */
    public PayOrderDO queryPayOrderByPayNo(String payNo) {

        LambdaQueryWrapper<PayOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayOrderDO::getPayNo, payNo);

        // 设置动态分表.
        TableNameHelper.setShardingMark(payNo);

        return payOrderMapper.selectOne(queryWrapper);

    }


    /**
     * 查询订单信息并对原订单加锁.
     *
     * @param requestBizNo 请求业务流水号.
     * @param requestDate  请求日期.
     * @param bizLine      业务线.
     * @return 支付单信息.
     */
    public PayOrderDO queryPayNoWithLock(String requestBizNo, Date requestDate, String bizLine) {
        LambdaQueryWrapper<PayOrderDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayOrderDO::getRequestBizNo, requestBizNo)
                .eq(PayOrderDO::getBizLine, bizLine)
                .last("for update");

        // 设置动态分表.
        TableNameHelper.setShardingMark(requestDate);

        return payOrderMapper.selectOne(queryWrapper);
    }

    /**
     * 添加退款单信息.
     *
     * @param refundOrder           退款单信息.
     * @param origModifyPayFundList 原支付资金单.
     * @param refundFundList     退款资金单.
     */
    public void addRefundOrder(PayOrderDO refundOrder, List<PayFundDO> origModifyPayFundList,
                               List<PayFundDO> refundFundList, List<PayJobDetailDO> refundJobDetailList) {

        TableNameHelper.setShardingMark(refundOrder.getPayNo());
        payOrderMapper.insert(refundOrder);
        payFundManager.batchAddPayFund(refundFundList);
        payFundManager.batchModifyRefundFund(origModifyPayFundList);
        payJobDetailManager.batchAddJobDetail(refundJobDetailList);
    }
}
