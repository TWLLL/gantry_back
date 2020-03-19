package com.qljt.gantry.platform.quartz.service.intf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobEntity;

/**
 * @author huchenggong
 * @create 2020-03-16 17:00
 */
public interface ScheduleJobEntityService extends IService<ScheduleJobEntity> {

    Boolean insert(ScheduleJobEntity scheduleJobEntity);

    Boolean deleteBatch(Long[] jobIds);

    Boolean delete(Long jobId);

    Boolean updateQuartz(ScheduleJobEntity scheduleJobEntity);

    Boolean pause(Long[] jobIds);

    Boolean resume(Long[] jobIds);

    Boolean run(Long[] jobIds);
}