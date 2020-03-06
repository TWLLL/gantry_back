package com.qljt.gantry.platform.dept.controller;

import com.qljt.gantry.platform.dept.bean.DeptEntity;
import com.qljt.gantry.platform.dept.mapper.DeptMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
