package paydemo.manager.db;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import paydemo.common.SysConstant;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.dao.mapper.PayFundMapper;
import paydemo.dao.mapper.TableNameHelper;
import paydemo.manager.model.PayFundBO;
import paydemo.util.PayStatusEnum;

import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @auther YDXiaa
 * 支付资金单.
 */
@Repository
public class PayFundManager {

    @Autowired
    private PayFundMapper payFundMapper;


    /**
     * 批量落库订单信息.
     *
     * @param payFundList 订单信息列表.
     */
    @Transactional(rollbackFor = Throwable.class)
    public void batchAddPayFund(List<PayFundDO> payFundList) {
        payFundList.forEach(payFundDO -> {
            TableNameHelper.setShardingMark(payFundDO.getPayFundNo());
            payFundMapper.insert(payFundDO);
        });
    }

    /**
     * 更新未支付资金单到关闭状态.
     *
     * @param toClosePayFundList 待关闭订单.
     */
    @Transactional(rollbackFor = Throwable.class)
    public void batchModifyPayFund2Close(List<PayFundDO> toClosePayFundList) {

        toClosePayFundList.forEach(payFundDO -> {
            TableNameHelper.setShardingMark(payFundDO.getPayFundNo());
            LambdaUpdateWrapper<PayFundDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(PayFundDO::getPayStatus, payFundDO.getPayStatus())
                    .set(PayFundDO::getPayCompletedDate, payFundDO.getPayCompletedDate())
                    .set(PayFundDO::getUpdateDate, new Date())
                    .set(PayFundDO::getUpdateUser, SysConstant.SYS_USER)
                    .eq(PayFundDO::getPayFundNo, payFundDO.getPayFundNo());

            payFundMapper.update(payFundDO, updateWrapper);
        });
    }


    /**
     * 修改支付资金单状态.
     *
     * @param payFundBO 支付资金单单DO.
     * @return 修改数量.
     */
    public int modifyPayFundStatus(PayFundBO payFundBO, PayStatusEnum origPayStatusEnum) {

        PayFundDO payFundDO = BeanUtil.copyProperties(payFundBO, PayFundDO.class);

        TableNameHelper.setShardingMark(payFundDO.getPayFundNo());
        LambdaUpdateWrapper<PayFundDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(PayFundDO::getPayStatus, payFundDO.getPayStatus())
                .set(Objects.nonNull(payFundDO.getOutRequestSeqNo()), PayFundDO::getOutRequestSeqNo, payFundDO.getOutRequestSeqNo())
                .set(Objects.nonNull(payFundDO.getOutRespSeqNo()), PayFundDO::getOutRespSeqNo, payFundDO.getOutRespSeqNo())
                .set(Objects.nonNull(payFundDO.getChannelDetailNo()), PayFundDO::getChannelDetailNo, payFundDO.getChannelDetailNo())
                .set(Objects.nonNull(payFundDO.getChannelFeeAmt()), PayFundDO::getChannelFeeAmt, payFundDO.getChannelFeeAmt())
                .set(PayFundDO::getUpdateDate, new Date())
                .set(PayFundDO::getUpdateUser, SysConstant.SYS_USER)
                .eq(PayFundDO::getPayFundNo, payFundDO.getPayFundNo())
                .eq(PayFundDO::getPayStatus, origPayStatusEnum.getStatusCode()); // 乐观锁.

        return payFundMapper.update(payFundDO, updateWrapper);
    }

    /**
     * 查询支付资金单.
     *
     * @param payFundNo 支付资金单.
     * @return 支付资金单.
     */
    public PayFundDO queryPayFund(String payFundNo) {

        TableNameHelper.setShardingMark(payFundNo);

        LambdaQueryWrapper<PayFundDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayFundDO::getPayFundNo, payFundNo);

        return payFundMapper.selectOne(queryWrapper);
    }

    /**
     * 支付资金单.
     *
     * @param payNo 支付单号.
     * @return 支付资金单.
     */
    public List<PayFundDO> queryPayFundListByPayNo(String payNo) {

        LambdaQueryWrapper<PayFundDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(PayFundDO::getPayNo, payNo);

        TableNameHelper.setShardingMark(payNo);

        return payFundMapper.selectList(queryWrapper);
    }

    /**
     * 批量修改退款单信息.
     *
     * @param origModifyPayFundList 原支付信息.
     */
    public void batchModifyRefundFund(List<PayFundDO> origModifyPayFundList) {

        origModifyPayFundList.forEach(payFundDO -> {

            TableNameHelper.setShardingMark(payFundDO.getPayFundNo());
            LambdaUpdateWrapper<PayFundDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.set(PayFundDO::getRefundAmt, payFundDO.getRefundAmt())
                    .set(PayFundDO::getUpdateDate, new Date())
                    .set(PayFundDO::getUpdateUser, SysConstant.SYS_USER)
                    .eq(PayFundDO::getPayFundNo, payFundDO.getPayFundNo());

            payFundMapper.update(payFundDO, updateWrapper);
        });
    }
}
