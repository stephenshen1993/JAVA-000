package com.stephenshen.raws.configuration;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName : DynamicDataSourceAspect  //类名
 * @Description : 动态数据源切换的切面，切 DAO 层，通过 DAO 层方法名判断使用哪个数据源，实现数据源切换  //描述
 * @Author : StephenShen  //作者
 * @Date: 2020-12-03 00:55  //时间
 */
@Aspect
@Component
public class DynamicDataSourceAspect {

    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * 查询前缀
     */
    private static String[] QUERY_PREFIX = {"select"};

    @Pointcut("execution( * com.stephenshen.raws.mapper.*.*(..))")
    public void daoAspect(){

    }

    /**
     * 切换数据源
     * @param point
     */
    @Before("daoAspect()")
    public void switchDataSource(JoinPoint point){
        Boolean isQueryMethod = isQueryMethod(point.getSignature().getName());
        if(isQueryMethod){
            DynamicDataSourceContextHolder.useSlaveDataSource();
            logger.info("Switch DataSource to [{}] in Method [{}]",
                    DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
        }
    }

    /**
     * 重置数据源
     * @param point
     */
    @After("daoAspect()")
    public void restoreDataSource(JoinPoint point){
        DynamicDataSourceContextHolder.clearDataSourceKey();
        logger.info("Restore DataSource to [{}] in Method [{}]",
                DynamicDataSourceContextHolder.getDataSourceKey(), point.getSignature());
    }

    /**
     * 是否是查询方法
     * @param methodName
     * @return
     */
    private Boolean isQueryMethod(String methodName) {
        for (String prefix : QUERY_PREFIX) {
            if (methodName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
