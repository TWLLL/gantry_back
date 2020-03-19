package com.qljt.gantry.platform.quartz.utils;


import com.qljt.gantry.common.Exception.RRException;
import com.qljt.gantry.common.utils.support.SpringContextUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

public class ScheduleRunnable
        implements Runnable {
    private Object target;
    private Method method;
    private String params;

    public ScheduleRunnable(String beanName, String methodName, String params)
            throws NoSuchMethodException, SecurityException {
        this.target = SpringContextUtils.getBean(beanName);
        this.params = params;

        if (StringUtils.isNotBlank(params))
            this.method = this.target.getClass().getDeclaredMethod(methodName, new Class[]{String.class});
        else
            this.method = this.target.getClass().getDeclaredMethod(methodName, new Class[0]);
    }

    @Override
    public void run() {
        try {
            ReflectionUtils.makeAccessible(this.method);
            if (StringUtils.isNotBlank(this.params))
                this.method.invoke(this.target, new Object[]{this.params});
            else
                this.method.invoke(this.target, new Object[0]);
        } catch (Exception e) {
            throw new RRException("执行定时任务失败", e);
        }
    }
}