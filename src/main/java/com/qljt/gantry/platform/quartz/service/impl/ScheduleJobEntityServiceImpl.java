package com.qljt.gantry.platform.quartz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.common.utils.support.Constant;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobEntity;
import com.qljt.gantry.platform.quartz.mapper.ScheduleJobEntityMapper;
import com.qljt.gantry.platform.quartz.service.intf.ScheduleJobEntityService;
import com.qljt.gantry.platform.quartz.utils.ScheduleUtils;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author huchenggong
 * @create 2020-03-16 17:00
 */
@Service
public class ScheduleJobEntityServiceImpl extends ServiceImpl<ScheduleJobEntityMapper, ScheduleJobEntity> implements ScheduleJobEntityService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private ScheduleJobEntityMapper scheduleJobEntityMapper;
    @PostConstruct
    public void init() {
        QueryWrapper<ScheduleJobEntity> scheduleJobEntityQueryWrapper = new QueryWrapper<>();
        List<ScheduleJobEntity> scheduleJobList = scheduleJobEntityMapper.selectList(scheduleJobEntityQueryWrapper);
        for (ScheduleJobEntity scheduleJob : scheduleJobList) {
            CronTrigger cronTrigger = ScheduleUtils.getCronTrigger(scheduler, scheduleJob.getJobId());

            if (cronTrigger == null)
                ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
            else
                ScheduleUtils.updateScheduleJob(scheduler, scheduleJob);
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean insert(ScheduleJobEntity scheduleJobEntity) {
        try {
            scheduleJobEntity.setStatus(Integer.valueOf(Constant.ScheduleStatus.NORMAL.getValue()));
            scheduleJobEntityMapper.insert(scheduleJobEntity);
            ScheduleJobEntity job = scheduleJobEntityMapper.selectOne(new QueryWrapper<ScheduleJobEntity>().eq("bean_name",scheduleJobEntity.getBeanName()));
            scheduleJobEntity.setJobId(job.getJobId());
            ScheduleUtils.createScheduleJob(scheduler, scheduleJobEntity);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Boolean deleteBatch(Long[] jobIds) {
        try {
            for (Long jobId : jobIds) {
                ScheduleUtils.deleteScheduleJob(scheduler, jobId);
            }
            scheduleJobEntityMapper.deleteBatchIds(Arrays.asList(jobIds));
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean delete(Long jobId) {
        try {
            ScheduleUtils.deleteScheduleJob(scheduler, jobId);
            scheduleJobEntityMapper.deleteById(jobId);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean updateQuartz(ScheduleJobEntity scheduleJobEntity) {
        try {
            ScheduleUtils.updateScheduleJob(scheduler, scheduleJobEntity);
            scheduleJobEntityMapper.updateById(scheduleJobEntity);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean pause(Long[] jobIds) {
        try {
            for(Long jobId : jobIds){
                ScheduleUtils.pauseJob(scheduler,jobId);
                ScheduleJobEntity scheduleJobEntity = scheduleJobEntityMapper.selectById(jobId);
                scheduleJobEntity.setStatus(Constant.ScheduleStatus.PAUSE.getValue());
                scheduleJobEntityMapper.updateById(scheduleJobEntity);
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean resume(Long[] jobIds) {
        try {
            for(Long jobId : jobIds){
                ScheduleUtils.resumeJob(scheduler,jobId);
                ScheduleJobEntity scheduleJobEntity = scheduleJobEntityMapper.selectById(jobId);
                scheduleJobEntity.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
                scheduleJobEntityMapper.updateById(scheduleJobEntity);
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public Boolean run(Long[] jobIds) {
        try {
            for(Long jobId : jobIds){
                ScheduleUtils.run(scheduler,scheduleJobEntityMapper.selectById(jobId));
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
}
