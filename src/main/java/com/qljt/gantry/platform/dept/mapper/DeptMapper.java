package com.qljt.gantry.platform.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qljt.gantry.platform.dept.bean.DeptEntity;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-26 14:58
 */
public interface DeptMapper extends BaseMapper<DeptEntity>{


    @Select("select dept.* from sys_dept dept where dept.del_flag = 0")
    List<DeptEntity> getDeptTreeList(Map<String, Object> params);

    @Select("select dept.* from sys_dept dept where dept_id = #{id}")
    DeptEntity getDept(Map<String, Object> params);

}
