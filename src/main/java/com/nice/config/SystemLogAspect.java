package com.nice.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.nice.annotation.Log;
import com.nice.domain.SystemLog;
import com.nice.domain.User;
import com.nice.service.SystemLogService;
import org.aopalliance.intercept.Joinpoint;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.UUID;

@Aspect
@Component
public class SystemLogAspect {

    /**
     * 注入Service用于把日志保存数据库
     */
    @Resource
    private SystemLogService systemLogService;

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemLogAspect.class);

    /**
     * Controller层切点
     */
    @Pointcut("execution (* com.nice.controller..*.*(..))")
    public void controllerAspect(){
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     * @param joinPoint
     */
    @Before("controllerAspect()")
    public void doBefore(JoinPoint joinPoint){
        System.out.println("===========执行controller前置通知===========");
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("before " + joinPoint);
        }
    }

    /**
     * 配置controller环绕通知，使用在方法aspect()上注册的切入点
     * @param joinPoint
     */
    @Around("controllerAspect()")
    public void around(JoinPoint joinPoint){
        System.out.println("=================开始执行controller环绕通知================");
        long start = System.currentTimeMillis();
        try{
            ((ProceedingJoinPoint) joinPoint).proceed();
            long end = System.currentTimeMillis();
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("around " + joinPoint + "\tUse time : " + (end - start) + " ms!");
            }
            System.out.println("==========结束执行controller环绕通知============");
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            if (LOGGER.isInfoEnabled()){
                LOGGER.info("around " + joinPoint + "\tUse time : " + ( end - start ) + "ms with exception" + e.getMessage() );
            }
        }
    }

    /**
     * 后置通知 用于拦截Controller层记录用户的操作
     * @param joinPoint 切点
     */
    @After("controllerAspect()")
    public void after(JoinPoint joinPoint){
        User user = new User();
        user.setId(1L);
        user.setName("nice");
        String ip = "127.0.0.1";
        try{
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)){
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length) {
                        operationType = method.getAnnotation(Log.class).operationType();
                        operationName = method.getAnnotation(Log.class).operationName();
                        break;
                    }
                }
            }
            //==========控制台输出============
            System.out.println("========controller后置通知开始=======");
            System.out.println("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()") + "." + operationType);
            System.out.println("方法描述:" + operationName);
            System.out.println("请求人:" + user.getName());
            System.out.println("请求IP:" + ip);
            //=========数据库日志========
            SystemLog log = new SystemLog();
     //       log.setId(Long.valueOf(UUID.randomUUID().toString()));
            log.setDescription(operationName);
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()" + "." + operationType));
            log.setLogType((long)0);
            log.setRequestId(ip);
            log.setExceptioncode(null);
            log.setExceptionDetail(null);
            log.setParams(null);
            log.setCreateBy(user.getName());
            log.setCreateDate(new Date());
            //保存数据库
            systemLogService.insert(log);
            System.out.println("========controller后置通知结束=========");
        } catch (ClassNotFoundException e) {
            //记录本地异常日志
            LOGGER.error("==后置通知异常==");
            LOGGER.error("异常信息:{}",e.getMessage());
        }
    }

    /**
     * 配置后置返回通知，使用在方法aspect()上注册的切入点
     * @param joinPoint
     */
    @AfterReturning("controllerAspect()")
    public void afterReturn(JoinPoint joinPoint){
        System.out.println("========执行controller后置返回通知========");
        if (LOGGER.isInfoEnabled()){
            LOGGER.info("afterReturn" + joinPoint);
        }
    }


    @AfterThrowing(pointcut = "controllerAspect()",throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint,Throwable e){
        User user = new User();
        user.setId(1L);
        user.setName("张三");
        String ip = "127.0.0.1";
        String params = "";
        if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0){
            for (int i = 0; i < joinPoint.getArgs().length; i++) {
                 params += JsonUtil.getJsonStr(joinPoint.getArgs()[i]) + ";" ;

            }
        }
        try{
            String targetName = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            Object[] arguments = joinPoint.getArgs();
            Class targetClass = Class.forName(targetName);
            Method[] methods = targetClass.getMethods();
            String operationType = "";
            String operationName = "";
            for (Method method : methods) {
                if (method.getName().equals(methodName)){
                    Class[] clazzs = method.getParameterTypes();
                    if (clazzs.length == arguments.length){
                        operationType = method.getAnnotation(Log.class).operationType();
                        operationName = method.getAnnotation(Log.class).operationName();
                        break;
                    }
                }
            }
            //======控制台输出=======
            System.out.println("========异常通知开始=========");
            System.out.println("异常代码:" + e.getClass().getName());
            System.out.println("异常信息:" + e.getMessage());
            System.out.println("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName()
            + "()") + "." + operationType );
            System.out.println("方法描述:" + operationName);
            System.out.println("请求人:" + user.getName());
            System.out.println("请求IP:" + ip);
            System.out.println("请求参数:" + params);
            //========数据库日志========
            SystemLog log = new SystemLog();
          //  log.setId(Long.valueOf(UUID.randomUUID().toString()));
            log.setDescription(operationName);
            log.setExceptioncode(e.getClass().getName());
            log.setLogType((long) 1);
            log.setExceptionDetail(e.getMessage());
            log.setMethod((joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
            log.setParams(params);
            log.setCreateBy(user.getName());
            log.setCreateDate(new Date());
            log.setRequestId(ip);
            //保存数据库
            systemLogService.insert(log);
            System.out.println("=========异常通知结束=========");
        } catch (ClassNotFoundException ex) {
            //记录本地异常日志
            LOGGER.error("==异常通知异常==");
            LOGGER.error("异常信息:{}",ex.getMessage());
        }
            //记录本地异常日志
            LOGGER.error("异常方法:()异常代码:()异常信息:()参数:()",joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(),e.getClass().getName(),e.getMessage(),params );
    }


}
