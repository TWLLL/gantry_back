package com.qljt.gantry.datasycn.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qljt.gantry.datasycn.bean.IpcDefine;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Created by Lic on 2020/3/18.
 */
public interface IpcDefineMapper extends BaseMapper<IpcDefine> {

    @Select("select i.*,n.ERROR_INFO as netErrorInfo from ipc_define i LEFT JOIN net_connect_process n on n.ip = i.ip where n.type = 2")
    public List<IpcDefine> selectByJoin();

}
