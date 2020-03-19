package com.qljt.gantry.platform.quartz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobLogEntity;
import com.qljt.gantry.platform.quartz.mapper.ScheduleJobLogEntityMapper;
import com.qljt.gantry.platform.quartz.service.intf.ScheduleJobLogEntityService;
import org.springframework.stereotype.Service;

/**
 * @author huchenggong
 * @create 2020-03-16 17:00
 */
@Service("scheduleJobLogEntityService")
public class ScheduleJobLogEntityServiceImpl extends ServiceImpl<ScheduleJobLogEntityMapper, ScheduleJobLogEntity> implements ScheduleJobLogEntityService {
}
