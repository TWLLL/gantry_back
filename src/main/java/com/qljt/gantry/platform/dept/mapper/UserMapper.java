package com.qljt.gantry.platform.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liuliangliang
 * @create 2020-02-28 11:10
 */
public interface UserMapper extends BaseMapper<UserEntity>{

    @Select("select m.perms from sys_user_role ur " +
            "LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
            "LEFT JOIN sys_menu m on rm.menu_id = m.menu_id " +
            "where ur.user_id = #{userId}")
    public List<String> queryAllPerms(Long userId);



}
