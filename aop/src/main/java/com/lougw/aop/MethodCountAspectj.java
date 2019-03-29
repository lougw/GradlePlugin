package com.lougw.aop;

import android.os.Looper;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class MethodCountAspectj {
    private static final String TAG = MethodCountAspectj.class.getSimpleName();

    @Pointcut("call(* *(..))")
    public void invokeMethod() {

    }

    @Pointcut("call(* com.lougw.aop..*(..))")
    public void invokeWatch() {

    }

    @Pointcut("call(* android.content.ContentValues.*(..))")
    public void invokeContentValues() {

    }

    @Around("invokeMethod() && !invokeWatch() && !invokeContentValues()")
    public void aroundMethodExecution(final ProceedingJoinPoint joinPoint) {
        try {
            MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
            String className = methodSignature.getDeclaringType().getSimpleName();
            String methodName = methodSignature.getName();
            final StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            joinPoint.proceed();
            stopWatch.stop();
            stopWatch.setClassName(className);
            stopWatch.setMethodName(methodName);
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                AOPUtil.getInstance().save(stopWatch);
            }
            Log.d(TAG, buildLogMessage(className, methodName, stopWatch.getTotalTimeMillis()));
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    public static String buildLogMessage(String className, String methodName, long methodDuration) {
        StringBuilder message = new StringBuilder();
        message.append(" --> ");
        message.append(className);
        message.append(" --> ");
        message.append(methodName);
        message.append(" --> ");
        message.append("[  ");
        message.append(methodDuration);
        message.append("ms");
        message.append("  ]");
        return message.toString();
    }


}
