package com.qljt.gantry.platform.quartz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qljt.gantry.datasycn.bean.*;
import com.qljt.gantry.datasycn.service.intf.*;
import com.qljt.gantry.platform.quartz.controller.QuartzCommonController;
import io.swagger.annotations.ApiOperation;
import org.quartz.DisallowConcurrentExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author huchenggong
 * @create 2020-03-10 11:00
 */
@Component("GantryStatusInfoJob")
@DisallowConcurrentExecution
public class GantryStatusInfoJob {
    private final NetConnectProcessService netConnectProcessService;
    private final GantryStatusInfoService gantryStatusInfoService;
    private final GantryStatusProcessService gantryStatusProcessService;
    private final QuartzCommonController quartzCommonController;

    @Autowired
    public GantryStatusInfoJob(NetConnectProcessService netConnectProcessService, GantryStatusInfoService gantryStatusInfoService, GantryStatusProcessService gantryStatusProcessService, QuartzCommonController quartzCommonController) {
        this.netConnectProcessService = netConnectProcessService;
        this.gantryStatusInfoService = gantryStatusInfoService;
        this.gantryStatusProcessService = gantryStatusProcessService;
        this.quartzCommonController = quartzCommonController;
    }

    @ApiOperation("后台自动扫描门架等设备数据任务")
    @Transactional
    public void gantryStatusInfoTask() {
        System.out.println(new Date() + "    GantryStatusInfoJob执行");
       /* // 获取参数
        JobDataMap jobDataMap = arg0.getJobDetail().getJobDataMap();*/
        // 业务逻辑
        try {
            // 获取状态为0且不为null的门架状态信息表
            QueryWrapper<GantryStatusInfo> gantryStatusInfoQueryWrapper = new QueryWrapper<>();
            gantryStatusInfoQueryWrapper.eq("status",0).or().isNull("status");
            List<GantryStatusInfo> gantryStatusInfoList = gantryStatusInfoService.list(gantryStatusInfoQueryWrapper);
            List<GantryStatusProcess> gantryStatusProcessList = gantryStatusProcessService.list();
            // 遍历门架状态信息表同步到Process表里面并修改info表状态为1
            for (GantryStatusInfo gantryStatusInfo : gantryStatusInfoList) {
                for(GantryStatusProcess gantryStatusProcess : gantryStatusProcessList) {
                    if (gantryStatusInfo.getIp().equals(gantryStatusProcess.getIp())) {
                        if(quartzCommonController.saveGantryStatusHis(gantryStatusProcess)){
                            gantryStatusProcessService.removeById(gantryStatusProcess);
                        }
                    }
                }
                GantryStatusProcess gantryStatusProcess = new GantryStatusProcess();
                gantryStatusProcess.setId(null);
                gantryStatusProcess.setName(gantryStatusInfo.getName());
                gantryStatusProcess.setCode(gantryStatusInfo.getCode());
                gantryStatusProcess.setIp(gantryStatusInfo.getIp());
                gantryStatusProcess.setRecordNowData(gantryStatusInfo.getRecordNowData());
                gantryStatusProcess.setRsuDate(gantryStatusInfo.getRsuDate());
                gantryStatusProcess.setLprDate(gantryStatusInfo.getLprDate());
                gantryStatusProcess.setStatus(0);
                gantryStatusProcess.setMac(gantryStatusInfo.getMac());
                gantryStatusProcess.setCount(gantryStatusInfo.getCount());
                gantryStatusProcess.setCountAlive(gantryStatusInfo.getCountAlive());
                gantryStatusProcess.setAppInfo(gantryStatusInfo.getAppInfo());
                gantryStatusProcess.setIsPing(gantryStatusInfo.getIsPing());
                gantryStatusProcess.setDns1(gantryStatusInfo.getDns1());
                gantryStatusProcess.setDns2(gantryStatusInfo.getDns2());
                gantryStatusProcess.setNet(gantryStatusInfo.getNet());
                gantryStatusProcess.setExfield1(gantryStatusInfo.getExfield1());
                gantryStatusProcess.setExfield2(gantryStatusInfo.getExfield2());
                if(gantryStatusProcessService.save(gantryStatusProcess)){
                    gantryStatusInfo.setStatus(1);
                    gantryStatusInfoService.updateById(gantryStatusInfo);
                }

            }

            // 与异常表进行处理分析，并存放到历史表中
            QueryWrapper<NetConnectProcess> netConnectProcessQueryWrapper = new QueryWrapper<>();
            netConnectProcessQueryWrapper.eq("status",0).or().isNull("status");
            List<NetConnectProcess> netConnectProcessList = netConnectProcessService.list(netConnectProcessQueryWrapper);
                for(GantryStatusInfo gantryStatusInfo : gantryStatusInfoList){
                    for(NetConnectProcess netConnectProcess : netConnectProcessList){
                        if(gantryStatusInfo.getIp().equals(netConnectProcess.getIp())){
                            netConnectProcessService.removeById(netConnectProcess);
                            quartzCommonController.saveNetConnectHis(netConnectProcess);
                        }
                    }
                }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
    }
}
