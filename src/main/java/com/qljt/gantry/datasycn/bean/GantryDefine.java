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
public class GantryDefine extends BaseEntity{

    @TableId(value = "id")
    private String id;

    //门架编号
    private String code;

    //门架名称
    private String name;

    //门架IP
    private String ip;

    //收费路段编号
    private String tollRoadId;

    //收费路段名称
    private String tollRoadName;

    //收费中心编码
    private String centerId;

    //分中心名称
    private String centerName;

    //门架类型（S表示实体门架）
    private Integer type;

    //省界表示（0：非省界；1：标识省界）
    private Integer isBoundaries;

    //收费单元个数
    private Integer amountCharges;

    //
    private String longitude;

    //
    private String dimension;

    //桩号
    private String pileId;

    //门架使用状态（0：停用；1：标识在用）
    private Integer status;

    // 门架HEX字符串
    private String hex;

    //服务器厂商
    private String serverManu;

    //服务器设备型号
    private String serverModelNum;

    //服务器设备编码
    private String serverCode;

    //操作系统版本
    private String osVersion;

    //数据库版本
    private String dbVersion;

    //数据库操作系统版本
    private String dbOsVersion;

    //所属部门ID
    private String orgId;

    //网络异常信息
    @TableField(exist = false)
    private String netErrorInfo;

}
