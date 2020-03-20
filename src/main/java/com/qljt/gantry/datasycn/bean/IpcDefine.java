package com.qljt.gantry.datasycn.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by Lic on 2020/3/18.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "gantry_status_his")
@ApiModel
public class IpcDefine extends BaseEntity {

    @TableId(value = "id")
    private Long id;

    //工控机编码
    private String code;

    //工控机IP
    private String ip;

    //工控机厂商
    private String manufacturer;

    //工控机型号编码
    private String modelNum;

    //网络异常信息
    @TableField(exist = false)
    private String netErrorInfo;

}
