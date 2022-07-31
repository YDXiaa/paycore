package paydemo.dao.dbmodel;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @auther YDXiaa
 * <p>
 * 支付差错.
 */
@Getter
@Setter
@ToString(callSuper = true)
@TableName("T_PAY_ERROR")
public class PayErrorDO extends BaseDO {

    /**
     * 支付单.
     */
    private String payNo;

    /**
     * 支付资金单.
     */
    private String payFundNo;

    /**
     * 关联支付单.
     */
    private String relationPayNo;

    /**
     * 差错类型.
     */
    private String errorType;

    /**
     * 差错处理方式.
     */
    private String errorProcessType;

    /**
     * 差错处理状态.
     */
    private String errorProcessStatus;

}
