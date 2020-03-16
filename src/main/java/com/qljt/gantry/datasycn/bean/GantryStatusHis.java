package com.qljt.gantry.datasycn.bean;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "gantry_status_his")
@ApiModel
public class GantryStatusHis extends BaseEntity{

  @TableId(value = "id")
  private Long id;
  private String name;
  private String code;
  private String ip;
  private java.sql.Timestamp recordNowData;
  private java.sql.Timestamp recordDate;
  private Integer status;
  private java.sql.Timestamp createDate;
  private String mac;
  private Integer countAlive;
  private String appInfo;
  @TableField(value = "dns_1")
  private Integer dns1;
  @TableField(value = "dns_2")
  private Integer dns2;
  private Integer net;
  @TableField(value = "exfield_1")
  private String exfield1;
  @TableField(value = "exfield_2")
  private String exfield2;

}
