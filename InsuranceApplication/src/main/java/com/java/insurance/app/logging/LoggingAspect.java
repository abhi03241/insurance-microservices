package com.java.insurance.app.logging;

import com.java.insurance.app.exception.InsuranceCustomException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import static com.java.insurance.app.constants.AppConstant.COLON;
import static com.java.insurance.app.constants.AppConstant.COMMA;
import static com.java.insurance.app.constants.AppConstant.ERROR_CODE;
import static com.java.insurance.app.constants.AppConstant.EXCEPTION;
import static com.java.insurance.app.constants.AppConstant.MESSAGE;
import static com.java.insurance.app.constants.AppConstant.METHOD_FINISHED;
import static com.java.insurance.app.constants.AppConstant.METHOD_STARTED;
import static com.java.insurance.app.constants.PointCutConstant.BEAN_POINTCUT_PATH;
import static com.java.insurance.app.constants.PointCutConstant.EXCEPTION_POINTCUT_PATH;

@Order(0)
@Aspect
@Configuration
@Slf4j
public class LoggingAspect {
    @Around(value = BEAN_POINTCUT_PATH)
    public Object logMethods(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Logger classLogger = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass());
        classLogger.info(METHOD_STARTED + COLON + proceedingJoinPoint.getSignature().getName());
        Object result = proceedingJoinPoint.proceed();
        classLogger.info(METHOD_FINISHED + COLON + proceedingJoinPoint.getSignature().getName());
        return result;
    }

    @Around(value = EXCEPTION_POINTCUT_PATH)
    public Object logExceptions(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final Logger classLogger = LoggerFactory.getLogger(proceedingJoinPoint.getTarget().getClass());
        Exception e = (Exception) proceedingJoinPoint.getArgs()[0];
        if (e instanceof InsuranceCustomException) {
            classLogger.error(EXCEPTION + COLON + e.getClass() + COMMA + ERROR_CODE + COLON + ((InsuranceCustomException) e).getErrorCode() + COMMA + MESSAGE + COLON + e.getMessage());
        } else {
            classLogger.error(EXCEPTION + COLON + e.getClass() + COMMA + MESSAGE + COLON + e.getMessage());
        }
        return proceedingJoinPoint.proceed();
    }
}
