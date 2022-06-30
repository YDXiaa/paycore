package paydemo.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @auther YDXiaa
 * <p>
 * 任务处理状态.
 */
@Getter
@AllArgsConstructor
public enum ExecStatusEnum {

    /**
     * 等待启动阶段.
     */
    READY("READY", "任务准备启动阶段"),

    /**
     * 运行阶段.
     */
    WORKING("WORKING", "任务运行中"),

    /**
     * 任务执行成功.
     */
    SUCCESS("SUCCESS", "任务执行成功"),

    /**
     * 任务执行失败
     */
    FAIL("FAIL", "任务执行失败"),
    ;

    /**
     * execStatusCode.
     */
    private final String execStatusCode;

    /**
     * execStatusDesc.
     */
    private final String execStatusDesc;

}
