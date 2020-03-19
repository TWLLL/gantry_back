package com.qljt.gantry.platform.quartz.utils;

import com.qljt.gantry.common.Exception.RRException;
import com.qljt.gantry.common.utils.support.Constant;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobEntity;
import org.quartz.*;

public class ScheduleUtils {
    private static final String JOB_NAME = "TASK_";

    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey("TASK_" + jobId);
    }

    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey("TASK_" + jobId);
    }

    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("获取定时任务CronTrigger出现异常", e);
        }
    }

    public static void createScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(ScheduleJob.class).withIdentity(getJobKey(scheduleJob.getJobId())).build();

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = (CronTrigger) TriggerBuilder.newTrigger().withIdentity(getTriggerKey(scheduleJob.getJobId())).withSchedule(scheduleBuilder).build();

            jobDetail.getJobDataMap().put("JOB_PARAM_KEY", scheduleJob);

            scheduler.scheduleJob(jobDetail, trigger);

            if (scheduleJob.getStatus().intValue() == Constant.ScheduleStatus.PAUSE.getValue())
                pauseJob(scheduler, scheduleJob.getJobId());
        } catch (SchedulerException e) {
            throw new RRException("创建定时任务失败", e);
        }
    }

    public static void updateScheduleJob(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            TriggerKey triggerKey = getTriggerKey(scheduleJob.getJobId());

            CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(scheduleJob.getCronExpression())
                    .withMisfireHandlingInstructionDoNothing();

            CronTrigger trigger = getCronTrigger(scheduler, scheduleJob.getJobId());

            trigger = (CronTrigger) trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

            trigger.getJobDataMap().put("JOB_PARAM_KEY", scheduleJob);

            scheduler.rescheduleJob(triggerKey, trigger);

            if (scheduleJob.getStatus().intValue() == Constant.ScheduleStatus.PAUSE.getValue())
                pauseJob(scheduler, scheduleJob.getJobId());
        } catch (SchedulerException e) {
            throw new RRException("更新定时任务失败", e);
        }
    }

    public static void run(Scheduler scheduler, ScheduleJobEntity scheduleJob) {
        try {
            JobDataMap dataMap = new JobDataMap();
            dataMap.put("JOB_PARAM_KEY", scheduleJob);

            scheduler.triggerJob(getJobKey(scheduleJob.getJobId()), dataMap);
        } catch (SchedulerException e) {
            throw new RRException("立即执行定时任务失败", e);
        }
    }

    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("暂停定时任务失败", e);
        }
    }

    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("暂停定时任务失败", e);
        }
    }

    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new RRException("删除定时任务失败", e);
        }
    }
}