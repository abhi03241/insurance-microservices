package com.java.insurance.app.logging;

import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;

import static com.java.insurance.app.constants.PointCutConstant.ALL_POINTCUT;
import static com.java.insurance.app.constants.PointCutConstant.CONTROLLER_ADVICE_POINTCUT;
import static com.java.insurance.app.constants.PointCutConstant.REPOSITORY_POINTCUT;
import static com.java.insurance.app.constants.PointCutConstant.REST_CONTROLLER_POINTCUT;
import static com.java.insurance.app.constants.PointCutConstant.SERVICE_POINTCUT;

@Configuration
public class ApplicationPointcuts {

    @Pointcut(REST_CONTROLLER_POINTCUT)
    public void controllerPointcut() {

    }

    @Pointcut(SERVICE_POINTCUT)
    public void servicePointcut() {

    }

    @Pointcut(REPOSITORY_POINTCUT)
    public void repositoryPointcut() {

    }

    @Pointcut(CONTROLLER_ADVICE_POINTCUT)
    public void exceptionPointcut() {

    }

    @Pointcut(ALL_POINTCUT)
    public void beanPointcut() {

    }
}
