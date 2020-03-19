package com.qljt.gantry.platform.dept.controller;

import com.qljt.gantry.platform.base.bean.CodeMsg;
import com.qljt.gantry.platform.base.bean.ResultJson;
import com.qljt.gantry.platform.dept.bean.DeptEntity;
import com.qljt.gantry.platform.dept.mapper.DeptMapper;
import com.qljt.gantry.platform.dept.service.intf.DeptService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-26 15:13
 */
@RestController
@RequestMapping("/dept")
@Api(tags="部门相关接口")
public class DeptController {

    @Autowired
    DeptMapper deptMapper;

    @Autowired
    DeptService deptService;

    @GetMapping("/")
    @ApiOperation("添加部门")
    public DeptEntity addDept(DeptEntity dept){
        //deptMapper.insertDept(dept);
        deptMapper.insert(dept);
        return  dept;
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询部门")
    @ApiImplicitParam(name = "id",value = "部门id",required = true)
    public DeptEntity getOneDept(@PathVariable("id") Long id){
        DeptEntity deptEntity = deptMapper.selectById(id);

        return  deptEntity;
    }

    @GetMapping(value = "/getDeptTreeList")
    @ResponseBody
    @ApiOperation("组织架构树形列表查询")
    private ResultJson getDeptTreeList(@RequestParam Map<String, Object> params){
        List<Map<String, Object>> deptEntityList = new ArrayList<>();
        try {
            deptEntityList = deptService.getDeptTreeList(params);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(deptEntityList);
    }

    @GetMapping(value = "/getDept")
    @ResponseBody
    @ApiOperation("获取组织信息")
    private ResultJson getDept(@RequestParam Map<String, Object> params){
        DeptEntity deptEntity = new DeptEntity();
        try {
            deptEntity = deptService.getDept(params);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        List<DeptEntity> list = new ArrayList<>();
        list.add(deptEntity);
        return ResultJson.success(list);
    }

    @RequestMapping(value = "/addObj", method = RequestMethod.POST)
    @ResponseBody
    private ResultJson addObj(@RequestBody Map<String, Object> params){
        Map<String,Object> retMap = ((Map)params.get("form"));
        Integer addEntity = null;
        try {
            addEntity = deptService.addObj(retMap);
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
            updateEntity = deptService.updateObj(retMap);
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
            // 0：正常；-1：删除
            params.put("delFlag", "-1");
            updateEntity = deptService.updateObj(params);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(updateEntity);
    }

    @GetMapping(value = "/getDeptTreeListByRole")
    @ResponseBody
    @ApiOperation("组织架构树形列表查询")
    private ResultJson getDeptTreeListByRole(@RequestParam Map<String, Object> params){
        List<Map<String, Object>> deptEntityList = new ArrayList<>();
        try {
            deptEntityList = deptService.getDeptTreeListByRole(params);
        }catch (Exception e){
            return ResultJson.error(CodeMsg.SERVER_ERROR.fillArgs(e.getMessage()));
        }
        return ResultJson.success(deptEntityList);
    }
}
