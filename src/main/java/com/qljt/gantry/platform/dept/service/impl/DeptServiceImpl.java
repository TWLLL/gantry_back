package com.qljt.gantry.platform.dept.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.platform.dept.bean.DeptEntity;
import com.qljt.gantry.platform.dept.mapper.DeptMapper;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.dept.service.intf.DeptService;
import com.qljt.gantry.platform.rolepermisson.bean.SysDeptRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysRoleEntity;
import com.qljt.gantry.platform.rolepermisson.mapper.DeptRoleRelRelMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-26 15:14
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper,DeptEntity> implements DeptService{

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @SuppressWarnings("all")
    RoleMapper roleMapper;

    @Autowired
    @SuppressWarnings("all")
    DeptMapper deptMapper;

    @Autowired
    @SuppressWarnings("all")
    DeptRoleRelRelMapper deptRoleRelRelMapper;


    @Override
    public List<Map<String, Object>> getDeptTreeList(Map<String, Object> params) {
        List<DeptEntity> resultList = new ArrayList<>();
        try {
            List<DeptEntity> deptEntityList = this.baseMapper.getDeptTreeList(params);
            resultList = this.createDeptTree(deptEntityList, 0);
        }catch (Exception e){
            return new ArrayList<>();
        }
        return this.formatTreeJson(resultList);
    }

    private List<DeptEntity> createDeptTree(List<DeptEntity> deptEntityList, Integer parentId){
        if (deptEntityList == null || deptEntityList.isEmpty()){
            return new ArrayList<>();
        }
        List<DeptEntity> treeList = new ArrayList<>();
        for (DeptEntity deptEntity : deptEntityList) {
            if (deptEntity.getParentId().longValue() == (Long) parentId.longValue()){
                treeList.add(deptEntity);
                deptEntity.setChildrenList(this.createDeptTree(deptEntityList, deptEntity.getDeptId().intValue()));
            }
        }
        return treeList.isEmpty() ? null : treeList;
    }

    private List<Map<String, Object>> formatTreeJson(List<DeptEntity> entityList){
        List<Map<String, Object>> comboTreeList = new ArrayList<Map<String, Object>>();
        for(DeptEntity entity : entityList){
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("id", entity.getDeptId());
            map.put("label", entity.getName());
            if (entity.getChildrenList() != null && entity.getChildrenList().size() > 0) {
                map.put("children", this.formatTreeJson(entity.getChildrenList()));
            }else {
                map.put("children", null);
            }
            if(map != null){
                comboTreeList.add(map);
            }
        }
        return comboTreeList;
    }

    @Override
    public DeptEntity getDept(Map<String, Object> params) {
        DeptEntity deptEntity = this.baseMapper.getDept(params);
        Map<String, Object> map = new HashMap<>();
        map.put("id", deptEntity.getParentId());
        DeptEntity entity = this.baseMapper.getDept(map);
        if (entity != null) {
            deptEntity.setPName(entity.getName());
        }
        return deptEntity;
    }

    @Override
    public Integer addObj(Map<String, Object> params) {
        DeptEntity entity = new DeptEntity();
        entity.setName(String.valueOf(params.get("name")));
        entity.setParentId(Long.valueOf(String.valueOf(params.get("parentId"))));
        entity.setOrderNum(Integer.parseInt(String.valueOf(params.get("orderNum"))));
        entity.setDelFlag(Integer.parseInt(String.valueOf(params.get("delFlag"))));
        return this.baseMapper.insert(entity);

    }

    @Override
    public Integer updateObj(Map<String, Object> params) {
        DeptEntity entity = new DeptEntity();
        if (params.get("deptId") != null && !"".equals(String.valueOf(params.get("deptId") != null))) {
            entity.setDeptId(Long.valueOf(String.valueOf(params.get("deptId"))));
        }
        if (params.get("name") != null && !"".equals(String.valueOf(params.get("name") != null))) {
            entity.setName(String.valueOf(params.get("name")));
        }
        if (params.get("parentId") != null && !"".equals(String.valueOf(params.get("parentId") != null))) {
            entity.setParentId(Long.valueOf(String.valueOf(params.get("parentId"))));
        }
        if (params.get("orderNum") != null && !"".equals(String.valueOf(params.get("orderNum") != null))) {
            entity.setOrderNum(Integer.parseInt(String.valueOf(params.get("orderNum"))));
        }
        if (params.get("delFlag") != null && !"".equals(String.valueOf(params.get("delFlag") != null))) {
            entity.setDelFlag(Integer.parseInt(String.valueOf(params.get("delFlag"))));
        }
        this.baseMapper.updateById(entity);
        return userMapper.updateDeptById(Long.valueOf(entity.getDeptId()));

    }

    @Override
    public Integer delObj(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("deptId") != null && !"".equals(String.valueOf(params.get("deptId")))) {
            map.put("dept_id", params.get("deptId"));
        }
        return this.baseMapper.deleteByMap(map);
    }

    @Override
    public List<Map<String, Object>> getDeptTreeListByRole(Map<String, Object> params) {
        List<DeptEntity> resultList = new ArrayList<>();
        String currentRole = (String)params.get("currentRole");
        // 根据当前角色名得到角色
        Map<String,Object> map = new HashMap<>();
        map.put("ROLE_NAME",currentRole);
        List<SysRoleEntity> roleEntities = roleMapper.selectByMap(map);
        // 根据角色ID得到角色部门对应实体类列表
        Map<String,Object> map1 = new HashMap<>();
        map1.put("ROLE_ID",roleEntities.get(0).getRoleId());
        List<SysDeptRoleRelEntity> sysDeptRoleRelEntities = deptRoleRelRelMapper.selectByMap(map1);
        // 遍历角色部门实体类得到部门ID列表,然后得部门
        List<DeptEntity> deptEntities = new ArrayList<>();
        for (SysDeptRoleRelEntity sysDeptRoleRelEntity : sysDeptRoleRelEntities){
            deptEntities.add(this.baseMapper.getDeptById(sysDeptRoleRelEntity.getDeptId()));
        }
        // 遍历并添加其子部门
        // 如果是超管
        if(roleEntities.get(0).getRoleType() == 1)
        {
            List<DeptEntity> deptEntityList = this.baseMapper.getDeptTreeList(params);
            resultList = this.createDeptTree(deptEntityList, 0);
            return this.formatTreeJson(resultList);
        }
        // 如果是分公司管理员
        if(roleEntities.get(0).getRoleType() == 2)
        {
            resultList = this.createDeptTree(deptEntities, 1);
            return this.formatTreeJson(resultList);
        }
        // 如果是普通用户
        if(roleEntities.get(0).getRoleType() == 3)
        {
            // 找PID对应的部门
            deptEntities.add(this.baseMapper.getDeptById(deptEntities.get(0).getParentId()));
            deptEntities.add(this.baseMapper.getRoot());
            // 找上级部门下所有的子部门
            resultList = this.createDeptTree(deptEntities, 0);
            return this.formatTreeJson(resultList);
        }
        // 如果是运维人员
        if(roleEntities.get(0).getRoleType() == 4)
        {
            deptEntities.add(this.baseMapper.getRoot());
            resultList = this.createDeptTree(deptEntities, 0);
            return this.formatTreeJson(resultList);
        }
        return null;
    }

}
