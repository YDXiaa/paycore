package paydemo.service.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import paydemo.common.exception.PayException;
import paydemo.common.exception.ResponseCodeEnum;
import paydemo.facade.model.Response;

/**
 * @auther YDXiaa
 * <p>
 * service层响应对象切面.
 */
@Slf4j
@Aspect
@Order(0)
@Component
public class ResponseAspect {

    @Pointcut("execution(public * paydemo.service.*.*(..))")
    public void serviceResponsePointCut() {
    }

    @Around("serviceResponsePointCut()")
    public Object lock(ProceedingJoinPoint joinPoint) {

        try {
            // requestObj.
            Object request = joinPoint.getArgs()[0];

            log.info("请求参数:{}", request);

            Object response = joinPoint.proceed();

            log.info("响应结果:{}", response);

        } catch (PayException payException) {

            log.info("发生异常:", payException);
            return Response.createFailResponse(payException.getResponseCode().getRespCode(),
                    payException.getResponseCode().getRespCode());

        } catch (Throwable throwable) {

            log.info("发生异常:", throwable);
            return Response.createFailResponse(ResponseCodeEnum.SYS_ERROR.getRespCode(),
                    ResponseCodeEnum.SYS_ERROR.getRespDesc());
        }

        // unknown exception Response.
        return Response.createFailResponse(ResponseCodeEnum.SYS_ERROR.getRespCode(),
                ResponseCodeEnum.SYS_ERROR.getRespDesc());
    }

}
