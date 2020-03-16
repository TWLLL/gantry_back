package com.qljt.gantry.platform.dept.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.platform.dept.bean.DeptEntity;
import com.qljt.gantry.platform.dept.mapper.DeptMapper;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.dept.service.intf.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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
}
