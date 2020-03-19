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
 * @create 2020-03-11 11:00
 */
@Component("IpcStatusInfoJob")
@DisallowConcurrentExecution
public class IpcStatusInfoJob {
    private final NetConnectProcessService netConnectProcessService;
    private final IpcStatusInfoService ipcStatusInfoService;
    private final IpcStatusProcessService ipcStatusProcessService;
    private final QuartzCommonController quartzCommonController;

    @Autowired
    public IpcStatusInfoJob(NetConnectProcessService netConnectProcessService, IpcStatusInfoService ipcStatusInfoService, IpcStatusProcessService ipcStatusProcessService, QuartzCommonController quartzCommonController) {
        this.netConnectProcessService = netConnectProcessService;
        this.ipcStatusInfoService = ipcStatusInfoService;
        this.ipcStatusProcessService = ipcStatusProcessService;
        this.quartzCommonController = quartzCommonController;
    }

    @ApiOperation("后台自动扫描工控机等设备数据任务")
    @Transactional
    public void ipcStatusInfoTask() {
        System.out.println(new Date() + "    IpcStatusInfoJob执行");
       /* // 获取参数
        JobDataMap jobDataMap = arg0.getJobDetail().getJobDataMap();*/
        // 业务逻辑
        try {
            // 获取状态为0且不为null的主控机状态信息表
            QueryWrapper<IpcStatusInfo> IpcStatusInfoQueryWrapper = new QueryWrapper<>();
            IpcStatusInfoQueryWrapper.eq("status",0).or().isNull("status");
            List<IpcStatusInfo> ipcStatusInfoList = ipcStatusInfoService.list(IpcStatusInfoQueryWrapper);
            List<IpcStatusProcess> ipcStatusProcessList = ipcStatusProcessService.list();
            // 遍历主控机状态信息表同步到Process表里面并修改info表状态为1
            for(IpcStatusInfo ipcStatusInfo : ipcStatusInfoList){
                for(IpcStatusProcess ipcStatusProcess : ipcStatusProcessList){
                    if(ipcStatusInfo.getIp().equals(ipcStatusProcess.getIp())){
                        quartzCommonController.saveIpcStatusHis(ipcStatusProcess);
                        ipcStatusProcessService.removeById(ipcStatusProcess);
                    }
                }
                IpcStatusProcess ipcStatusProcess = new IpcStatusProcess();
                ipcStatusProcess.setId(null);
                ipcStatusProcess.setCode(ipcStatusInfo.getCode());
                ipcStatusProcess.setIp(ipcStatusInfo.getIp());
                ipcStatusProcess.setBelongGantry(ipcStatusInfo.getBelongGantry());
                ipcStatusProcess.setGantryIp(ipcStatusInfo.getGantryIp());
                ipcStatusProcess.setMac(ipcStatusInfo.getMac());
                ipcStatusProcess.setCount(ipcStatusInfo.getCount());
                ipcStatusProcess.setCountAlive(ipcStatusInfo.getCountAlive());
                ipcStatusProcess.setAppInfo(ipcStatusInfo.getAppInfo());
                ipcStatusProcess.setNet(ipcStatusInfo.getNet());
                ipcStatusProcess.setStatus(0);
                ipcStatusProcess.setExfield1(ipcStatusInfo.getExfield1());
                ipcStatusProcess.setExfield2(ipcStatusInfo.getExfield2());
                if(ipcStatusProcessService.save(ipcStatusProcess)){
                    ipcStatusInfo.setStatus(1);
                    ipcStatusInfoService.updateById(ipcStatusInfo);
                }
            }
            // 与异常表进行处理分析，并存放到历史表中，清空主控机process表
            QueryWrapper<NetConnectProcess> netConnectProcessQueryWrapper = new QueryWrapper<>();
            netConnectProcessQueryWrapper.eq("status",0).or().isNull("status");
            List<NetConnectProcess> netConnectProcessList = netConnectProcessService.list(netConnectProcessQueryWrapper);
            for(IpcStatusInfo ipcStatusInfo : ipcStatusInfoList){
                for(NetConnectProcess netConnectProcess : netConnectProcessList){
                    if(ipcStatusInfo.getIp().equals(netConnectProcess.getIp())){
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
