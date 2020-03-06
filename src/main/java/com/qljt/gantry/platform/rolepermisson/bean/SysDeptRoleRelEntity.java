package com.qljt.gantry.platform.rolepermisson.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author liuliangliang
 * @create 2020-03-02 16:20
 */
@Data
@TableName("sys_role_dept_rel")
public class SysDeptRoleRelEntity {
    @TableId
    private Long id;
    private Long roleId;
    private Long deptId;
}
