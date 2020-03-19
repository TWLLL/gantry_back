package com.qljt.gantry.platform.quartz.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qljt.gantry.common.utils.support.Constant;
import com.qljt.gantry.common.utils.support.ValidatorUtils;
import com.qljt.gantry.datasycn.bean.*;
import com.qljt.gantry.datasycn.service.intf.GantryStatusHisService;
import com.qljt.gantry.datasycn.service.intf.IpcStatusHisService;
import com.qljt.gantry.datasycn.service.intf.NetConnectHisService;
import com.qljt.gantry.platform.base.bean.ResultJson;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobEntity;
import com.qljt.gantry.platform.quartz.bean.ScheduleJobLogEntity;
import com.qljt.gantry.platform.quartz.service.intf.ScheduleJobEntityService;
import com.qljt.gantry.platform.quartz.service.intf.ScheduleJobLogEntityService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huchenggong
 * @create 2020-03-09 17:00
 */
@RequestMapping("/quartzCommon")
@RestController
public class QuartzCommonController {
    private final GantryStatusHisService gantryStatusHisService;
    private final IpcStatusHisService ipcStatusHisService;
    private final NetConnectHisService netConnectHisService;
    private final ScheduleJobEntityService scheduleJobService;
    private final ScheduleJobLogEntityService scheduleJobLogEntityService;
    @Autowired
    public QuartzCommonController(GantryStatusHisService gantryStatusHisService, IpcStatusHisService ipcStatusHisService, NetConnectHisService netConnectHisService, ScheduleJobEntityService scheduleJobService, ScheduleJobLogEntityService scheduleJobLogEntityService) {
        this.gantryStatusHisService = gantryStatusHisService;
        this.ipcStatusHisService = ipcStatusHisService;
        this.netConnectHisService = netConnectHisService;
        this.scheduleJobService = scheduleJobService;
        this.scheduleJobLogEntityService = scheduleJobLogEntityService;
    }

    @ApiOperation("门架新增历史公共方法")
    public Boolean saveGantryStatusHis(GantryStatusProcess gantryStatusProcess){
        GantryStatusHis gantryStatusHis = new GantryStatusHis();
        gantryStatusHis.setId(gantryStatusProcess.getId());
        gantryStatusHis.setName(gantryStatusProcess.getName());
        gantryStatusHis.setCode(gantryStatusProcess.getCode());
        gantryStatusHis.setIp(gantryStatusProcess.getIp());
        gantryStatusHis.setRecordNowData(gantryStatusProcess.getRecordNowData());
        gantryStatusHis.setStatus(0);
        gantryStatusHis.setMac(gantryStatusProcess.getMac());
        gantryStatusHis.setCountAlive(gantryStatusProcess.getCountAlive());
        gantryStatusHis.setAppInfo(gantryStatusProcess.getAppInfo());
        gantryStatusHis.setDns1(gantryStatusProcess.getDns1());
        gantryStatusHis.setDns2(gantryStatusProcess.getDns2());
        gantryStatusHis.setNet(gantryStatusProcess.getNet());
        gantryStatusHis.setExfield1(gantryStatusProcess.getExfield1());
        gantryStatusHis.setExfield2(gantryStatusProcess.getExfield2());
        return gantryStatusHisService.save(gantryStatusHis);
    }

    @ApiOperation("新增工控机历史公共方法")
    public Boolean saveIpcStatusHis(IpcStatusProcess ipcStatusProcess){
        IpcStatusHis ipcStatusHis = new IpcStatusHis();
        ipcStatusHis.setId(null);
        ipcStatusHis.setCode(ipcStatusProcess.getCode());
        ipcStatusHis.setIp(ipcStatusProcess.getIp());
        ipcStatusHis.setBelongGantry(ipcStatusProcess.getBelongGantry());
        ipcStatusHis.setGantryIp(ipcStatusProcess.getGantryIp());
        ipcStatusHis.setMac(ipcStatusProcess.getMac());
        ipcStatusHis.setCountAlive(ipcStatusProcess.getCountAlive());
        ipcStatusHis.setAppInfo(ipcStatusProcess.getAppInfo());
        ipcStatusHis.setNet(ipcStatusProcess.getNet());
        ipcStatusHis.setStatus(0);
        ipcStatusHis.setExfield1(ipcStatusProcess.getExfield1());
        ipcStatusHis.setExfield2(ipcStatusProcess.getExfield2());
        return ipcStatusHisService.save(ipcStatusHis);
    }

    @ApiOperation("异常信息存历史表公共方法")
    public Boolean saveNetConnectHis(NetConnectProcess netConnectProcess){
        NetConnectHis netConnectHis = new NetConnectHis();
        netConnectHis.setId(null);
        netConnectHis.setIp(netConnectProcess.getIp());
        netConnectHis.setType(netConnectProcess.getType());
        netConnectHis.setErrorType(netConnectProcess.getErrorType());
        netConnectHis.setErrorInfo(netConnectProcess.getErrorInfo());
        netConnectHis.setStatus(0);
        return netConnectHisService.save(netConnectHis);
    }


    @GetMapping("/list")
    @ApiOperation(value = "获取任务列表")
    public ResultJson getList(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit,@RequestParam("searchData") String searchData){
        Page<ScheduleJobEntity> scheduleJobPage = new Page<>(page,limit,true);
        QueryWrapper<ScheduleJobEntity> scheduleJobQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isEmpty(searchData)){
            scheduleJobQueryWrapper.orderByAsc("JOB_ID");
        }else{
            scheduleJobQueryWrapper.like("METHOD_NAME",searchData).orderByAsc("JOB_ID");
        }
        IPage<ScheduleJobEntity> iPage = scheduleJobService.page(scheduleJobPage,scheduleJobQueryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("list",iPage.getRecords());
        map.put("total",iPage.getTotal());
        return ResultJson.success(map);
    }

    @GetMapping("/logList")
    @ApiOperation(value = "获取日志列表")
    public ResultJson getLogList(@RequestParam("page") Integer page,@RequestParam("limit") Integer limit,@RequestParam("searchData") String searchData){
        Page<ScheduleJobLogEntity> scheduleJobLogEntityPage = new Page<>(page,limit,true);
        QueryWrapper<ScheduleJobLogEntity> scheduleJobLogEntityQueryWrapper = new QueryWrapper<>();
        if(StringUtils.isEmpty(searchData)){
            scheduleJobLogEntityQueryWrapper.orderByAsc("ID");
        }else{
            scheduleJobLogEntityQueryWrapper.like("METHOD_NAME",searchData).orderByAsc("ID");
        }
        IPage<ScheduleJobLogEntity> iPage = scheduleJobLogEntityService.page(scheduleJobLogEntityPage,scheduleJobLogEntityQueryWrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("list",iPage.getRecords());
        map.put("total",iPage.getTotal());
        return ResultJson.success(map);
    }

    @PostMapping("/addQuartz")
    @ApiOperation(value = "新增任务")
    public ResultJson addQuartz(@RequestBody Map<String,Object> map){
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        scheduleJobEntity.setBeanName(map.get("beanName").toString());
        scheduleJobEntity.setMethodName(map.get("methodName").toString());
        scheduleJobEntity.setParams(map.get("params").toString());
        scheduleJobEntity.setCronExpression(map.get("cronExpression").toString());
        scheduleJobEntity.setRemark(map.get("remark").toString());
        ValidatorUtils.validateEntity(scheduleJobEntity, new Class[0]);
        return ResultJson.success(scheduleJobService.insert(scheduleJobEntity));
    }

    @PostMapping("/updateQuartz")
    @ApiOperation(value = "修改任务")
    public ResultJson updateQuartz(@RequestBody Map<String,Object> map){
        ScheduleJobEntity scheduleJobEntity = new ScheduleJobEntity();
        scheduleJobEntity.setJobId(Long.valueOf(map.get("jobId").toString()));
        scheduleJobEntity.setBeanName(map.get("beanName").toString());
        scheduleJobEntity.setMethodName(map.get("methodName").toString());
        scheduleJobEntity.setParams(map.get("params").toString());
        if(map.get("status").toString().equals("0")){
            scheduleJobEntity.setStatus(Constant.ScheduleStatus.NORMAL.getValue());
        }else{
            scheduleJobEntity.setStatus(Constant.ScheduleStatus.PAUSE.getValue());
        }
        scheduleJobEntity.setCronExpression(map.get("cronExpression").toString());
        scheduleJobEntity.setRemark(map.get("remark").toString());
        ValidatorUtils.validateEntity(scheduleJobEntity, new Class[0]);
        return ResultJson.success(scheduleJobService.updateQuartz(scheduleJobEntity));
    }


    @PostMapping("/deleteQuartz")
    public ResultJson deleteQuartz(@RequestBody Map<String,Long> jobId) {
        //Boolean b = scheduleJobService.deleteBatch(jobId);
       Boolean b = scheduleJobService.delete(jobId.get("jobId"));
        return ResultJson.success(b);
    }

    @PostMapping("/stopQuartz")
    public ResultJson stopQuartz(@RequestBody Map<String,Long[]> jobIds) {
        Long[] jobId = jobIds.get("jobIds");
        Boolean b = scheduleJobService.pause(jobId);
        return ResultJson.success(b);
    }

    @PostMapping("/resumeQuartz")
    public ResultJson resumeQuartz(@RequestBody Map<String,Long[]> jobIds) {
        Long[] jobId = jobIds.get("jobIds");
        Boolean b = scheduleJobService.resume(jobId);
        return ResultJson.success(b);
    }

    @PostMapping("/runQuartz")
    public ResultJson runQuartz(@RequestBody Map<String,Long[]> jobIds) {
        Long[] jobId = jobIds.get("jobIds");
        Boolean b = scheduleJobService.run(jobId);
        return ResultJson.success(b);
    }






}
