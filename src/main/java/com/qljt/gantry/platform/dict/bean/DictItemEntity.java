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
@TableName("t_dict_item")
public class DictItemEntity
{
    @TableId
    private String id;
    private String itemId;
    private String itemValue;
    private String itemParam;
    private String groupId;
    private String remark;
    private Date createTime;
    private String operator;
    private String exfeild;
    private String status;
}
