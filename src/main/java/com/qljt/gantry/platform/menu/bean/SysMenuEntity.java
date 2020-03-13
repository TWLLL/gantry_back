package com.qljt.gantry.platform.menu.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author liuliangliang
 * @create 2020-02-28 11:07
 */
@Data
@TableName("SYS_MENU")
public class SysMenuEntity extends BaseEntity{

    @TableId
    private Long id;
    private Long pid;
    private Integer sort;
    private String remark;
    private Date createTime;
    private String path;
    private String name;
    private Integer hidden;
    private String redirect;
    private String component;
    private Integer alwaysShow;
    private Integer meta;
    private String title;
    private String icon;

}
