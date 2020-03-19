package com.qljt.gantry.platform.quartz.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qljt.gantry.datasycn.bean.*;
import com.qljt.gantry.datasycn.service.intf.*;
import com.qljt.gantry.platform.quartz.controller.QuartzCommonController;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author huchenggong
 * @create 2020-03-09 11:00
 */
@Component("NetConnectInfoJob")
@DisallowConcurrentExecution
public class NetConnectInfoJob {
    private final NetConnectInfoService netConnectInfoService;
    private final NetConnectProcessService netConnectProcessService;
    private final IpcStatusProcessService ipcStatusProcessService;
    private final GantryStatusProcessService gantryStatusProcessService;
    private final IpDeviceRelService ipDeviceRelService;
    private final QuartzCommonController quartzCommonController;

    @Autowired
    public NetConnectInfoJob(NetConnectInfoService netConnectInfoService, NetConnectProcessService netConnectProcessService, IpcStatusProcessService ipcStatusProcessService, GantryStatusProcessService gantryStatusProcessService, IpDeviceRelService ipDeviceRelService, QuartzCommonController quartzCommonController) {
        this.netConnectInfoService = netConnectInfoService;

        this.netConnectProcessService = netConnectProcessService;
        this.ipcStatusProcessService = ipcStatusProcessService;
        this.gantryStatusProcessService = gantryStatusProcessService;
        this.ipDeviceRelService = ipDeviceRelService;
        this.quartzCommonController = quartzCommonController;
    }

    @ApiOperation("后台自动扫描门架等设备数据任务")
    @Transactional
    public void netConnectInfoTask() {
        System.out.println(new Date() + "    NetConnectInfoJob执行");
        // 获取参数
        /*JobDataMap jobDataMap = arg0.getJobDetail().getJobDataMap();*/
        // 业务逻辑
        try {
            QueryWrapper<NetConnectInfo> netConnectInfoQueryWrapper = new QueryWrapper<>();
            QueryWrapper<NetConnectProcess> netConnectProcessQueryWrapper = new QueryWrapper<>();
            netConnectProcessQueryWrapper.eq("status",0).or().isNull("status");
            netConnectInfoQueryWrapper.eq("status",0).or().isNull("status");
            List<NetConnectInfo> netConnectInfoList = netConnectInfoService.list(netConnectInfoQueryWrapper);
            List<NetConnectProcess> netConnectProcessList = netConnectProcessService.list(netConnectProcessQueryWrapper);
            for(NetConnectInfo netConnectInfo : netConnectInfoList){
                for(NetConnectProcess netConnectProcess : netConnectProcessList){
                    if(netConnectInfo.getIp().equals(netConnectProcess.getIp())){
                        if(quartzCommonController.saveNetConnectHis(netConnectProcess)){
                            netConnectProcessService.removeById(netConnectProcess);
                        }
                    }
                }
                NetConnectProcess netConnectProcess = new NetConnectProcess();
                netConnectProcess.setId(null);
                netConnectProcess.setIp(netConnectInfo.getIp());
                netConnectProcess.setType(netConnectInfo.getType());
                netConnectProcess.setErrorInfo(netConnectInfo.getErrorInfo());
                netConnectProcess.setErrorType(netConnectInfo.getErrorType());
                netConnectProcess.setStatus(0);
                if(netConnectProcessService.save(netConnectProcess)){
                    netConnectInfo.setStatus(1);
                    netConnectInfoService.updateById(netConnectInfo);
                }
                QueryWrapper<IpDeviceRel> ipDeviceRelQueryWrapper = new QueryWrapper<>();
                ipDeviceRelQueryWrapper.eq("IP",netConnectInfo.getIp());
                IpDeviceRel ipDeviceRel = ipDeviceRelService.getOne(ipDeviceRelQueryWrapper);
                if(!StringUtils.isEmpty(ipDeviceRel.getType())){
                    String type = ipDeviceRel.getType();
                    if("S".equals(type)){
                        QueryWrapper<GantryStatusProcess> gantryStatusProcessQueryWrapper = new QueryWrapper<>();
                        gantryStatusProcessQueryWrapper.eq("status",0);
                        List<GantryStatusProcess> gantryStatusProcessList = gantryStatusProcessService.list(gantryStatusProcessQueryWrapper);
                        for(GantryStatusProcess gantryStatusProcess : gantryStatusProcessList){
                            if(netConnectInfo.getIp().equals(gantryStatusProcess.getIp())){
                                if(quartzCommonController.saveGantryStatusHis(gantryStatusProcess)){
                                    gantryStatusProcessService.removeById(gantryStatusProcess);
                                }
                            }
                        }
                    }else if("I".equals(type)){
                        QueryWrapper<IpcStatusProcess> ipcStatusProcessQueryWrapper = new QueryWrapper<>();
                        ipcStatusProcessQueryWrapper.eq("status",0);
                        List<IpcStatusProcess> ipcStatusProcessList = ipcStatusProcessService.list(ipcStatusProcessQueryWrapper);
                        for(IpcStatusProcess ipcStatusProcess : ipcStatusProcessList){
                            if(netConnectInfo.getIp().equals(ipcStatusProcess.getIp())){
                                if(quartzCommonController.saveIpcStatusHis(ipcStatusProcess)){
                                    ipcStatusProcessService.removeById(ipcStatusProcess);
                                }
                            }
                        }
                    }else {
                        throw new RuntimeException("该IP没有找到指定的类型！");
                    }
                }else {
                    throw new RuntimeException("类型为空！！！");
                }
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            e.printStackTrace();
        }
    }


}
