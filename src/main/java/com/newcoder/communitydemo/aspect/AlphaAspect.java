package com.newcoder.communitydemo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

//@Component
//@Aspect
public class AlphaAspect {

    /**
     * 第一个* 返回类型
     * 第二个* 类
     * 第三个* 方法
     * (..) 参数列表
     */
    @Pointcut("execution(* com.newcoder.communitydemo.service.*.*(..))")
    public void pointcut() {

    }

    @Before("pointcut()")
    public void before() {
        System.out.println("before");
    }

    @After("pointcut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("pointcut()")
    public void afterReturning() {
        System.out.println("afterReturning");
    }

    @AfterThrowing("pointcut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("aroundBefore");
        Object obj = joinPoint.proceed(); // 调用原始对象的方法,前后添加代码
        System.out.println("aroundAfter");
        return obj;
    }

}
