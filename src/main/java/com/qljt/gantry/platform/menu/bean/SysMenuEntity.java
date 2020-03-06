package com.qljt.gantry.platform.menu.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @author liuliangliang
 * @create 2020-02-28 11:07
 */
@Data
@TableName("SYS_MENU")
public class SysMenuEntity extends BaseEntity{

    @TableId
    private Long menuId;
    private Long parentId;

    @TableField(exist = false)
    private String parentName;
    private String name;
    private String url;
    private String perms;
    private Integer type;
    private String icon;
    private Integer orderNum;
    private String descript;

}
