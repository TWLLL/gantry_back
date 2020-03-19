package com.qljt.gantry.platform.dict.controller;

import com.qljt.gantry.platform.base.bean.ResultJson;
import com.qljt.gantry.platform.dict.bean.DictGroupEntity;
import com.qljt.gantry.platform.dict.bean.DictItemEntity;
import com.qljt.gantry.platform.dict.service.DictService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @auther: yf
 * createTime: 2020.3.18 10:43
 */
@RestController
@Api(tags="字典相关接口")
public class DictController
{
    @Autowired
    DictService dictService;

    @RequestMapping(value = {"/dict/addGroup"}, method = {RequestMethod.POST})
    public ResultJson add(@RequestBody Map<String, Object> map) {
        // 字典组参数
        String groupName = (String)map.get("groupName");
        String groupCode = (String)map.get("groupCode");
        // 装字典组
        DictGroupEntity dictGroupEntity = new DictGroupEntity();
        dictGroupEntity.setGroupId(UUID.randomUUID().toString());
        dictGroupEntity.setGroupName(groupName);
        dictGroupEntity.setGroupCode(groupCode);
        dictGroupEntity.setStatus(1);
        dictGroupEntity.setCreateTime(new Date());
        dictGroupEntity.setOperator("admin");
        int x = dictService.addGroup(dictGroupEntity);
        return ResultJson.success("SUCCESS");
    }

    @RequestMapping(value = {"/dict/addItem"}, method = {RequestMethod.POST})
    public ResultJson addItem(@RequestBody Map<String, Object> map) {
        // 字典项参数
        String itemId = (String)map.get("itemId");
        String groupId = (String)map.get("groupId");
        String itemValue = (String)map.get("itemValue");
        String itemParam = (String)map.get("itemParam");
        String remark = (String)map.get("remark");
        // 装字典组
        DictItemEntity dictItemEntity = new DictItemEntity();
        dictItemEntity.setId(UUID.randomUUID().toString());
        dictItemEntity.setItemId(itemId);
        dictItemEntity.setItemValue(itemValue);
        dictItemEntity.setItemParam(itemParam);
        dictItemEntity.setGroupId(groupId);
        dictItemEntity.setRemark(remark);
        dictItemEntity.setCreateTime(new Date());
        dictItemEntity.setOperator("admin");
        dictItemEntity.setStatus("1");
        int x = dictService.addItem(dictItemEntity);
        return ResultJson.success("SUCCESS");
    }

    @RequestMapping(value = {"/dict/delGroup"}, method = {RequestMethod.DELETE})
    public ResultJson delGroup(@RequestBody Map<String, Object> map) { return ResultJson.success(dictService.delGroup((String)map.get("groupId"))); }

    @RequestMapping(value = {"/dict/delItem"}, method = {RequestMethod.DELETE})
    public ResultJson delItem(@RequestBody Map<String, Object> map) { return ResultJson.success(dictService.delItem((String)map.get("id"))); }

    @RequestMapping(value = {"/dict/updateGroup"}, method = {RequestMethod.PUT})
    public ResultJson updateGroup(@RequestBody Map<String, Object> map) { return ResultJson.success(dictService.updateGroup(map)); }

    @RequestMapping(value = {"/dict/updateItem"}, method = {RequestMethod.PUT})
    public ResultJson updateItem(@RequestBody Map<String, Object> map) { return ResultJson.success(dictService.updateItem(map)); }

    /**
     * @param DictGroupEntity的groupId
     * @return Map,根据串"DictGroupEntity"取DictGroupEntity对象，根据串"DictItemEntityList"取List<DictItemEntity>
     */
    @RequestMapping(value = {"/dict/selectGroupAndItemByGroupId"}, method = {RequestMethod.POST})
    public ResultJson selectGroupAndItemByGroupId(@RequestBody Map<String, Object> map) {
        return ResultJson.success(dictService.selectGroupAndItemByGroupId((String) map.get("groupId")));
    }
}
