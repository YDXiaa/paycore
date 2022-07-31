package paydemo.biz;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import paydemo.common.ErrorProcessStatusEnum;
import paydemo.common.ErrorProcessTypeEnum;
import paydemo.common.ErrorTypeEnum;
import paydemo.common.RemotePayResult;
import paydemo.dao.dbmodel.PayErrorDO;
import paydemo.dao.dbmodel.PayFundDO;
import paydemo.manager.db.PayErrorManager;
import paydemo.manager.db.PayFundManager;
import paydemo.manager.model.PayFundBO;
import paydemo.util.PayStatusEnum;

import java.util.Objects;

/**
 * @auther YDXiaa
 * <p>
 * 支付冲正.
 */
@Slf4j
@Service
public class PayRevokeBiz {

    @Autowired
    private PayFundManager payFundManager;

    @Autowired
    private PayProcessorFactory payProcessorFactory;

    @Autowired
    private PayErrorManager payErrorManager;

    /**
     * 冲正业务.
     *
     * @param payFundNo 支付资金单.
     * @return revoke.
     */
    public boolean revoke(String payFundNo) {

        try {

            log.info("冲正业务开始:{}", payFundNo);

            // 查询冲正资金单.
            PayFundDO payFundDO = payFundManager.queryPayFund(payFundNo);

            if (Objects.isNull(payFundDO)) {
                return true;
            }

            // 非初始状态视为处理成功
            // todo 冲正受理即认为成功.
            if (!PayStatusEnum.INIT.getStatusCode().equals(payFundDO.getPayStatus())) {
                return true;
            }

            // 发起冲正.
            PayFundBO payFundBO = BeanUtil.copyProperties(payFundDO, PayFundBO.class);
            RemotePayResult remotePayResult = payProcessorFactory.getPayProcessor(payFundDO.getPayTool()).revoke(payFundBO);

            // 响应处理.
            revokeResponseProcess(remotePayResult, payFundBO);

        } catch (Exception ex) {
            log.error("冲正异常,异常信息:{}", ExceptionUtil.stacktraceToString(ex));
        }

        return true;
    }

    /**
     * 结果处理.
     *
     * @param remotePayResult 远程支付结果.
     * @param payFundBO       支付资金单.
     */
    private void revokeResponseProcess(RemotePayResult remotePayResult, PayFundBO payFundBO) {

        if (StrUtil.equals(PayStatusEnum.FAIL.getStatusCode(), remotePayResult.getPayStatus())) {

            // 退款推定状态一般为SUCCESS防止资金损失处理,暂时挂差错,人工核查解决.
            PayErrorDO payErrorDO = new PayErrorDO();

            payErrorDO.setPayNo(payFundBO.getPayNo());
            payErrorDO.setPayFundNo(payFundBO.getPayFundNo());
            payErrorDO.setErrorType(ErrorTypeEnum.REVOKE_FAIL.getErrorTypeCode());
            payErrorDO.setErrorProcessType(ErrorProcessTypeEnum.QUERY_RESULT.getErrorProcessTypeCode());
            payErrorDO.setErrorProcessStatus(ErrorProcessStatusEnum.WAIT_PROCESS.getErrorProcessStatusCode());

            payErrorManager.addPayError(payErrorDO);
        }
    }
}
