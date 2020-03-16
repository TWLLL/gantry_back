package com.qljt.gantry.platform.dept.controller;

import com.qljt.gantry.platform.base.bean.CodeMsg;
import com.qljt.gantry.platform.base.bean.ResultJson;
import com.qljt.gantry.platform.base.controller.BaseController;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.dept.service.intf.SysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-28 10:54
 */
@RestController
@RequestMapping({"/sys/user"})
@Api("用户相关接口")
public class UserController extends BaseController{

    @Autowired
    private SysUserService sysUserService;

    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST)
    @ResponseBody
    private ResultJson pageQuery(@RequestBody Map<String, Object> params){
        Map<String,Object> retMap = ((Map)params.get("listQuery"));
        List<UserEntity> userEntityList = new ArrayList<>();
        try {
            userEntityList = sysUserService.pageQuery(retMap);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(userEntityList);
    }

    @RequestMapping(value = "/addObj", method = RequestMethod.POST)
    @ResponseBody
    private ResultJson addObj(@RequestBody Map<String, Object> params){
        Map<String,Object> retMap = ((Map)params.get("form"));
        Integer addEntity = null;
        try {
            addEntity = sysUserService.addObj(retMap);
            if (addEntity == -1){
                return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs("该用户名已存在！"));
            }
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(addEntity);
    }

    @RequestMapping(value = "/updateObj", method = RequestMethod.POST)
    @ResponseBody
    private ResultJson updateObj(@RequestBody Map<String, Object> params){
        Map<String,Object> retMap = ((Map)params.get("form"));
        Integer updateEntity = null;
        try {
            updateEntity = sysUserService.updateObj(retMap);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(updateEntity);
    }

    @RequestMapping(value = "/delObj", method = RequestMethod.POST)
    @ResponseBody
    private ResultJson delObj(@RequestBody Map<String, Object> params){
        Integer updateEntity = null;
        try {
            // 0：禁用；1：正常
            params.put("status", "0");
            updateEntity = sysUserService.updateObj(params);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(updateEntity);
    }


}
