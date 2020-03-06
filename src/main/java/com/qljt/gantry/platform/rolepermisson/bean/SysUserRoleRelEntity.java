package com.qljt.gantry.platform.rolepermisson.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import lombok.Data;

/**
 * @author liuliangliang
 * @create 2020-03-02 11:56
 */
@Data
@TableName("sys_role_user_rel")
public class SysUserRoleRelEntity extends BaseEntity{
    @TableId
    private Long id;
    private Long userId;
    private Long roleId;
}
