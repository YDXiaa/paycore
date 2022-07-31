package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 排序优先级权重.
 * <p>
 * 在路由因子增多的情况下，规则的维护会是一个噩梦。基于权重的路由则可以缓解这个问题。这种计算方式，简单说，就是对各个通道打分，分数最高的就命中。
 * 难点在于制定打分的标准以及计算公式。 比如可以从费率、优惠额度、QOS和使用率角度来评分，给优惠额度高一点的比重，这会导致高优惠额度的通道被优先命中。
 * 注意每个维度上的计算公式也不是一成不变的，比如使用率和QOS都是动态打分计算。权重的调整是一个挑战，需要在运行过程中不断的调试。
 * 路由是支付的核心模块，稳定性是第一要素，其次是性能，最后才是怎么省钱,避免在系统建设初期引入过于复杂的路由.
 */
@Getter
@AllArgsConstructor
public enum SortPriorityEnum {

    /**
     * 渠道支付优惠.
     */
    PAY_DISCOUNT(30, "渠道优惠"),

    /**
     * 手续费成本.
     */
    FEE_COST(100, "手续费成本"),

    /**
     * 成功率优先级.
     */
    SUCCESS_RATE(500, "通道成功率"),

    /**
     * 支付类型.
     */
    PAY_TYPE(200,"支付类型")

    ;

    /**
     * 排序优先级.
     */
    private final int sortPriorityCode;

    /**
     * 排序优先级描述.
     */
    private final String sortPriorityDesc;
}
