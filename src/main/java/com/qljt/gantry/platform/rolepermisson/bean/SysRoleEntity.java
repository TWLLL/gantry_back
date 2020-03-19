package com.qljt.gantry.platform.rolepermisson.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @author liuliangliang
 * @create 2020-02-28 10:50
 */
@Data
@TableName("SYS_ROLE")
public class SysRoleEntity extends BaseEntity{
    @TableId
    private Long roleId;

    @NotBlank(message = "角色名称不能为空")
    private String roleName;
    private String remark;

    @TableField(exist = false)
    private List<Long> menuIdList;

    @TableField(exist = false)
    private List<Long> deptIdList;

    @TableField(exist = false)
    private List<String> deptList;

    private Integer roleType;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    private String dataScope;
}
