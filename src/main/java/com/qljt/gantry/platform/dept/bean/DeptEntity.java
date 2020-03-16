package com.qljt.gantry.platform.dept.bean;

import com.baomidou.mybatisplus.annotation.*;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author liuliangliang
 * @create 2020-02-26 14:17
 */
@Data
@TableName(value = "sys_dept")
@ApiModel
public class DeptEntity extends BaseEntity{

    @TableId(value = "dept_id")
    @ApiModelProperty(value = "部门id")
    private Long deptId;
    private Long parentId;
    private String name;
    private Integer orderNum;
    private Timestamp createDate;
    private String ancestors;
    private Integer delFlag;

    @TableField(exist = false)
    private String pName;

    @TableField(exist = false)
    private List<DeptEntity> childrenList;

}
