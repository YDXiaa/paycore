package paydemo.common;

/**
 * @auther YDXiaa
 * <p>
 * 系统常量.
 */
public final class SysConstant {

    /**
     * 系统用户.
     */
    public static final String SYS_USER = "SYSTEM";

    /**
     * 固定方法名.
     */
    public static final String FIXED_SERVICE_NAME = "service";

    /**
     * Dubbo Group.
     */
    public static final String DUBBO_GROUP = "PAY_GROUP";

    /**
     * Dubbo interface version.
     */
    public static final String DUBBO_CONSUMER_VERSION = "1.0.0";

    /**
     * Dubbo Conusmer TimeOut.
     */
    public static final int DUBBO_CONSUMER_TIME_OUT = 3000;

    /**
     * 系统默认任务执行次数
     */
    public static final Long JOB_DEFAULT_EXECUTE_TIMES = 5L;

    /**
     * 系统默认任务执行间隔 60S.
     */
    public static final Long JOB_DEFAULT_EXECUTE_INTERVAL = 60L;


}
