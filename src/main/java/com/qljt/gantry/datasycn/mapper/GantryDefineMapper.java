package com.qljt.gantry.datasycn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qljt.gantry.datasycn.bean.GantryDefine;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Lic on 2020/3/18.
 */
public interface GantryDefineMapper extends BaseMapper<GantryDefine> {

    @Select("select g.*,n.ERROR_INFO as netErrorInfo from gantry_define g LEFT JOIN net_connect_process n on n.ip = g.ip where n.type = 1")
    public List<GantryDefine> selectByJoin();
}
