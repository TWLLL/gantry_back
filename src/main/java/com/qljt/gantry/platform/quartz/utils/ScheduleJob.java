package com.qljt.gantry.platform.quartz.utils;

import com.qljt.gantry.common.utils.support.SpringContextUtils;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobEntity;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobLogEntity;
import com.qljt.gantry.platform.quartz.service.intf.ScheduleJobLogEntityService;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ScheduleJob extends QuartzJobBean {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private ExecutorService service = Executors.newSingleThreadExecutor();

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        ScheduleJobEntity scheduleJob = (ScheduleJobEntity) context.getMergedJobDataMap().get("JOB_PARAM_KEY");
        ScheduleJobLogEntityService scheduleJobLogService = (ScheduleJobLogEntityService) SpringContextUtils.getBean("scheduleJobLogEntityService");
        ScheduleJobLogEntity log = new ScheduleJobLogEntity();
        log.setJobId(scheduleJob.getJobId());
        log.setBeanName(scheduleJob.getBeanName());
        log.setMethodName(scheduleJob.getMethodName());
        log.setParams(scheduleJob.getParams());
        long startTime = System.currentTimeMillis();
        try {
            this.logger.info("任务准备执行，任务ID：" + scheduleJob.getJobId());
            ScheduleRunnable task = new ScheduleRunnable(scheduleJob.getBeanName(), scheduleJob.getMethodName(), scheduleJob.getParams());
            Future future = this.service.submit(task);
            future.get();
            long times = System.currentTimeMillis() - startTime;
            log.setTimes(Integer.valueOf((int) times));
            log.setStatus(Integer.valueOf(0));
            this.logger.info("任务执行完毕，任务ID：" + scheduleJob.getJobId() + "  总共耗时：" + times + "毫秒");
        } catch (Exception e) {
            this.logger.error("任务执行失败，任务ID：" + scheduleJob.getJobId(), e);
            long times = System.currentTimeMillis() - startTime;
            log.setTimes(Integer.valueOf((int) times));
            log.setStatus(Integer.valueOf(1));
            log.setError(StringUtils.substring(e.toString(), 0, 2000));
        } finally {
            scheduleJobLogService.save(log);
        }
    }
}