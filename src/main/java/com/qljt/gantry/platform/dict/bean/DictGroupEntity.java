package com.qljt.gantry.platform.dict.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @auther: yf
 * createTime: 2020.3.18 10:43
 */
@Data
@TableName("t_dict_group")
public class DictGroupEntity
{
    @TableId
    private String groupId;
    private String groupName;
    private String groupCode;
    private Integer status;
    private Date createTime;
    private String operator;
}
