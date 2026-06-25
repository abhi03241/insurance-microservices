package com.java.insurance.app.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PointCutConstant {
    public static final String REST_CONTROLLER_POINTCUT = "within(@org.springframework.web.bind.annotation.RestController *)";
    public static final String SERVICE_POINTCUT = "within(@org.springframework.stereotype.Service *)";
    public static final String REPOSITORY_POINTCUT = "within(@org.springframework.stereotype.Repository *)";
    public static final String CONTROLLER_ADVICE_POINTCUT = "within(@org.springframework.web.bind.annotation.ControllerAdvice *)";
    public static final String ALL_POINTCUT = "controllerPointcut() || servicePointcut() || repositoryPointcut() || exceptionPointcut()";
    public static final String BEAN_POINTCUT_PATH = "com.java.insurance.app.logging.ApplicationPointcuts.beanPointcut()";
    public static final String EXCEPTION_POINTCUT_PATH = "com.java.insurance.app.logging.ApplicationPointcuts.exceptionPointcut()";
}
