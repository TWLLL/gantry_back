package com.qljt.gantry.datasycn.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * Created by Lic on 2020/3/17.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "ipc_status_business")
@ApiModel
public class IpcStatusBusiness extends BaseEntity{

    @TableId(value = "id")
    private Long id;
    private String code;
    private String ip;
    private String belongGantry;
    private String gantryIp;
    private String mac;
    private Integer count;
    private Integer countAlive;
    private String appInfo;
    private Integer net;
    private Integer status;
    private String eStatus;
//    @TableField(fill = FieldFill.INSERT)
    private Date createdate;
    @TableField(value = "exfield_1")
    private String exfield1;
    @TableField(value = "exfield_2")
    private String exfield2;

}
