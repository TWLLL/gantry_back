package com.qljt.gantry.platform.dept.service.intf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qljt.gantry.platform.dept.bean.DeptEntity;

import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-28 16:10
 */
public interface DeptService extends IService<DeptEntity> {

    List<Map<String, Object>> getDeptTreeList(Map<String, Object> params);

    DeptEntity getDept(Map<String, Object> params);

    Integer addObj(Map<String, Object> params);

    Integer updateObj(Map<String, Object> params);

    Integer delObj(Map<String, Object> params);

    List<Map<String, Object>> getDeptTreeListByRole(Map<String, Object> params);

}