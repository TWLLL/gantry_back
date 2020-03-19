package com.qljt.gantry.platform.rolepermisson.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qljt.gantry.platform.base.bean.CodeMsg;
import com.qljt.gantry.platform.base.bean.ResultJson;
import com.qljt.gantry.platform.dept.bean.DeptEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysDeptRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysRoleEntity;
import com.qljt.gantry.platform.rolepermisson.mapper.DeptRoleRelRelMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.RoleMapper;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @auther: yf
 * createTime: 2020.3.15 20:15
 */
@RestController
@Api(tags="角色权限相关接口")
public class RoleController
{
    @Autowired
    @SuppressWarnings("all")
    RoleMapper roleMapper;

    @Autowired
    @SuppressWarnings("all")
    DeptRoleRelRelMapper deptRoleRelRelMapper;

    @RequestMapping(value = {"/role/getRoles"}, method = {RequestMethod.POST})
    public ResultJson getRoles(@RequestBody Map<String, Object> map){
        String roleId = (String) map.get("roleId");
        String roleName = (String) map.get("roleName");
        // 根据角色ID通过 角色-部门关系表 得到部门列表
        HashMap<String, Object> map1 = new HashMap<>();
        map1.put("ROLE_ID",roleId);
        List<SysDeptRoleRelEntity> sysDeptRoleRelEntities = deptRoleRelRelMapper.selectByMap(map1);
        List<Long> deptIds = new ArrayList<>();
        for (SysDeptRoleRelEntity deptRoleRelEntity : sysDeptRoleRelEntities) {
           deptIds.add(deptRoleRelEntity.getDeptId());
        }
        // 得到部门列表后反查角色ID，得到若干角色-部门ID列表，合
        Set<Long> roleIds = new HashSet<>();
        for (int i = 0;i < deptIds.size();i ++) {
            Map<String,Object> map2 = new HashMap<>();
            map2.put("DEPT_ID",deptIds.get(i));
            List<SysDeptRoleRelEntity> sysDeptRoleRelEntities1 = deptRoleRelRelMapper.selectByMap(map2);
            for (SysDeptRoleRelEntity sysDeptRoleRelEntity : sysDeptRoleRelEntities1) {
                roleIds.add(sysDeptRoleRelEntity.getRoleId());
            }
        }
        // 得到应有的roleId集合后，得相应的role对象，遇见type == 4(运维人员)的,去
        // 非管理员
        List<SysRoleEntity> roleEntities = new ArrayList<>();
        for (Long id : roleIds) {
            SysRoleEntity sysRoleEntity = roleMapper.selectById(id);
            // 遇见运维人员角色
            if(sysRoleEntity.getRoleType() != 4) {
                roleEntities.add(sysRoleEntity);
            }
            if(sysRoleEntity.getStatus() == 1){
                roleEntities.add(sysRoleEntity);
            }
        }
        // 如果是管理员
        Map<String,Object> map2 = new HashMap<>();
        map2.put("ROLE_ID",roleId);
        List<SysRoleEntity> roleEntities1 = roleMapper.selectByMap(map2);
        if (roleEntities1.get(0).getRoleType() == 1) {
            roleEntities.clear();
            Map<String,Object> map3 = new HashMap<>();
            map3.put("STATUS",1);
            List<SysRoleEntity> roleEntityList = roleMapper.selectByMap(map3);
            for (SysRoleEntity sysRoleEntity : roleEntityList) {
                if(roleName != null && roleName != "") {
                    if(sysRoleEntity.getRoleName().equals(roleName)){
                        roleEntityList.clear();
                        roleEntityList.add(sysRoleEntity);
                        return ResultJson.success(roleEntityList);
                    }
                }
            }
            return ResultJson.success(roleEntityList);
        }
        // 根据角色名精确查询
        if(roleName != null && roleName != "") {
            for (SysRoleEntity sysRoleEntity : roleEntities) {
                if(sysRoleEntity.getRoleName().equals(roleName)){
                    roleEntities.clear();
                    roleEntities.add(sysRoleEntity);
                    return ResultJson.success(roleEntities);
                }
            }
        }
        return ResultJson.success(roleEntities);
    }

    @RequestMapping(value = {"/role/addRole"}, method = {RequestMethod.POST})
    public ResultJson addRole(@RequestBody Map<String, Object> map){
        String roleName = (String) map.get("roleName");
        String remark = (String) map.get("remark");
        String roleType = (String) map.get("roleType");
        if(roleName == null || roleName == "" || remark == null || roleName == "")
            return ResultJson.error(CodeMsg.REQUEST_ERROR);
        List<Integer> depts = (List<Integer>) map.get("dept");
        SysRoleEntity sysRoleEntity = new SysRoleEntity();
        sysRoleEntity.setRoleName(roleName);
        sysRoleEntity.setRemark(remark);
        //赵到最小的id，然后+1
        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("ROLE_ID").last("limit 1");
        SysRoleEntity sysRoleEntity1 = roleMapper.selectOne(queryWrapper);
        sysRoleEntity.setRoleId(sysRoleEntity1.getRoleId()+1);
        sysRoleEntity.setCreateTime(new Date());
        sysRoleEntity.setRoleType(Integer.valueOf(roleType));
        sysRoleEntity.setStatus(1);
        roleMapper.insert(sysRoleEntity);
        Map<String,Object> map1 = new HashMap<>();
        map1.put("ROLE_NAME",roleName);
        map1.put("REMARK",remark);
        List<SysRoleEntity> list = roleMapper.selectByMap(map1);
        SysRoleEntity roleEntity = list.get(0);
        for (Integer deptId : depts) {
            SysDeptRoleRelEntity sysDeptRoleRelEntity = new SysDeptRoleRelEntity();
            sysDeptRoleRelEntity.setRoleId(roleEntity.getRoleId());
            sysDeptRoleRelEntity.setDeptId(deptId.longValue());
            //赵到最小的id，然后+1
            QueryWrapper<SysDeptRoleRelEntity> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.orderByDesc("ID").last("limit 1");
            SysDeptRoleRelEntity sysDeptRoleRelEntity1 = deptRoleRelRelMapper.selectOne(queryWrapper1);
            sysDeptRoleRelEntity.setId(sysDeptRoleRelEntity1.getId() +1);
            deptRoleRelRelMapper.insert(sysDeptRoleRelEntity);
        }
        return ResultJson.success("SUCCESS");
    }

    @RequestMapping(value = {"/role/updateRole"}, method = {RequestMethod.PUT})
    public ResultJson updateRole(@RequestBody Map<String, Object> map){
        Integer roleId = (Integer) map.get("roleId");
        String roleName = (String) map.get("roleName");
        String remark = (String) map.get("remark");
        System.out.println(roleId);
        System.out.println(roleName);
        System.out.println(remark);
        SysRoleEntity sysRoleEntity = new SysRoleEntity();
        sysRoleEntity.setRoleId(roleId.longValue());
        sysRoleEntity.setRemark(remark);
        sysRoleEntity.setRoleName(roleName);
        roleMapper.updateById(sysRoleEntity);
        return ResultJson.success("测试中....");
    }

    @DeleteMapping("/role/deleteRole")
    public ResultJson deleteRole(@RequestBody Map<String, Object> map){
        Integer roleId = (Integer) map.get("roleId");
        SysRoleEntity sysRoleEntity = roleMapper.selectById(roleId);
        sysRoleEntity.setStatus(0);
        roleMapper.updateById(sysRoleEntity);
        return ResultJson.success("SUCCESS");
    }
}
