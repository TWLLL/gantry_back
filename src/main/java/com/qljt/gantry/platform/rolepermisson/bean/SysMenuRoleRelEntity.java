package com.qljt.gantry.platform.rolepermisson.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import lombok.Data;

/**
 * @author liuliangliang
 * @create 2020-03-02 16:19
 */
@Data
@TableName("sys_role_menu_rel")
public class SysMenuRoleRelEntity extends BaseEntity{

    @TableId
    private Long id;
    private Long roleId;
    private Long menuId;
}
