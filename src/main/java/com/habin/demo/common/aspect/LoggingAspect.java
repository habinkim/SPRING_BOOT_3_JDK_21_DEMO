package com.habin.demo.common.aspect;

import com.surgepay.api.v3.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    @Pointcut(
            "within(@org.springframework.stereotype.Repository *)" +
                    " || within(@org.springframework.stereotype.Service *)" +
                    " || within(@org.springframework.web.bind.annotation.RestController *)"
    )
    public void springBeanPointcut() {
    }

    @Pointcut("within(com.habin.demo.*.repository..*)" + " || within(com.surgepay.api.v3.*.service..*)" + " || within(com.surgepay.api.v3.*.controller..*)")
    public void applicationPackagePointcut() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchApi() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteApi() {
    }

    @Pointcut("getApi() || postApi() || putApi() || patchApi() || deleteApi()")
    public void restApi() {
    }

    @Pointcut("execution(* com.surgepay.api.v3.*.repository.*.*(..))")
    public void jpaQuery() {
    }

    /**
     * Retrieves the {@link Logger} associated to the given {@link JoinPoint}.
     *
     * @param joinPoint join point we want the logger for.
     * @return {@link Logger} associated to the given {@link JoinPoint}.
     */
    private Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, ApiException e) {
        logger(joinPoint)
                .error(
                        "Exception in {}() with cause = '{}' and exception = '{}'",
                        joinPoint.getSignature().getName(),
                        e.getCause() != null ? e.getCause() : "NULL",
                        e.getRealMessage(), e
                );
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut() && springBeanPointcut()", throwing = "e")
    public void logAfterThrowing(JoinPoint joinPoint, Exception e) {
        if (!(e instanceof ApiException)) {
            logger(joinPoint)
                    .error(
                            "Exception in {}() with cause = '{}' and exception = '{}'",
                            joinPoint.getSignature().getName(),
                            e.getCause() != null ? e.getCause() : "NULL",
                            e.getMessage(), e
                    );
        }
    }

    @Around("restApi() || jpaQuery()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        StopWatch stopWatch = new StopWatch("LogExecutionTime Aop");

        stopWatch.start();
        Object proceed = joinPoint.proceed();
        stopWatch.stop();

        String msg = joinPoint.getSignature().getDeclaringType() + "." +
                joinPoint.getSignature().getName() + " : " +
                "running time = " +
                stopWatch.getTotalTimeMillis() + "ms";
        logger(joinPoint).info(msg);

        return proceed;
    }

}
