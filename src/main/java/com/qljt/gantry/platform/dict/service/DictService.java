package com.qljt.gantry.platform.dict.service;

import com.qljt.gantry.platform.dict.bean.DictGroupEntity;
import com.qljt.gantry.platform.dict.bean.DictItemEntity;
import com.qljt.gantry.platform.dict.mapper.DictGroupMapper;
import com.qljt.gantry.platform.dict.mapper.DictItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @auther: yf
 * createTime: 2020.3.18 10:43
 */
@Service
public class DictService
{
    @Autowired
    DictGroupMapper dictGroupMapper;

    @Autowired
    DictItemMapper dictItemMapper;


    public int addGroup(DictGroupEntity dictGroupEntity) { return dictGroupMapper.insert(dictGroupEntity); }

    public int addItem(DictItemEntity dictItemEntity) { return dictItemMapper.insert(dictItemEntity); }

    public int delGroup(String groupId)
    {
        DictGroupEntity dictGroupEntity = dictGroupMapper.selectById(groupId);
        if(dictGroupEntity != null){
            dictGroupEntity.setStatus(0);
            return dictGroupMapper.updateById(dictGroupEntity);
        }
        return 0;
    }

    public int delItem(String id)
    {
        DictItemEntity dictItemEntity = dictItemMapper.selectById(id);
        dictItemEntity.setStatus("0");
        return dictItemMapper.updateById(dictItemEntity);
    }

    public int updateGroup(Map<String, Object> map)
    {
        DictGroupEntity dictGroupEntity = dictGroupMapper.selectById((String) map.get("groupId"));
        String groupName = (String) map.get("groupName");
        String groupCode = (String) map.get("groupCode");
        if(groupName != null && groupName != ""){
            dictGroupEntity.setGroupName(groupName);
        }
        if(groupCode != null && groupCode != ""){
            dictGroupEntity.setGroupCode(groupCode);
        }
        return dictGroupMapper.updateById(dictGroupEntity);
    }

    public int updateItem(Map<String, Object> map)
    {
        DictItemEntity dictItemEntity = dictItemMapper.selectById((String) map.get("id"));
        String itemId = (String) map.get("itemId");
        String itemValue = (String) map.get("itemValue");
        String itemParam = (String) map.get("itemParam");
        String remark = (String) map.get("remark");
        if(itemId != null && itemId != ""){
            dictItemEntity.setItemId(itemId);
        }
        if(itemValue != null && itemValue != ""){
            dictItemEntity.setItemValue(itemValue);
        }
        if(itemParam != null && itemParam != ""){
            dictItemEntity.setItemParam(itemParam);
        }
        if(remark != null && remark != ""){
            dictItemEntity.setRemark(remark);
        }
        return dictItemMapper.updateById(dictItemEntity);
    }

    public Map<String, Object> selectGroupAndItemByGroupId(String groupId)
    {
        Map<String,Object> map = new HashMap<>();
        DictGroupEntity dictGroupEntity = dictGroupMapper.selectById(groupId);
        // 返回status为1的即状态可用的
        if(dictGroupEntity.getStatus() == 1){
            map.put("DictGroupEntity",dictGroupEntity);
            Map<String,Object> map1 = new HashMap<>();
            map1.put("GROUP_ID",groupId);
            map1.put("STATUS","1");
            List<DictItemEntity> dictItemEntities = dictItemMapper.selectByMap(map1);
            map.put("DictItemEntityList",dictItemEntities);
        }
        return map;
    }
}
