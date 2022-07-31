package paydemo.facade.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @auther YDXiaa
 * <p>
 * 差错处理对象.
 */
@Data
public class ErrorProcessRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id.
     */
    private Long id;

    /**
     * 关联资金单.
     */
    private String relationPayFundNo;

    /**
     * 差错处理类型.
     */
    private String errorProcessType;

}
