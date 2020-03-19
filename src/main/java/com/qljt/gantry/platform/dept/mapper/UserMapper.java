package com.qljt.gantry.platform.dept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

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

    @Select({"<script>",
            "select u.*,d.name as deptName from sys_user u " +
                    "LEFT JOIN sys_dept d on u.dept_id = d.dept_id where u.status = 1" +
                    "<if test='username != null'>and u.user_name = #{username}</if>" +
                    "<if test='email != null'>and u.email = #{email}</if>" +
                    "<if test='mobile != null'>and u.mobile = #{mobile}</if>" +
//                "<if test = 'status != null'>and u.status = #{status}</if>" +
                    "<if test = 'deptId != null'>and u.dept_id = #{deptId}</if>",
            "</script>"}
    )
    public List<UserEntity> pageQuery(Map<String, Object> params);

    @Update("update sys_user set status = '0' where dept_id = #{deptId}")
    public Integer updateDeptById(Long deptId);


}
