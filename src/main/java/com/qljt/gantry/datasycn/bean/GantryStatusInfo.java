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
@TableName(value = "gantry_status_info")
@ApiModel
public class GantryStatusInfo extends BaseEntity{

  @TableId(value = "id")
  private Long id;
  private String name;
  private String code;
  private String ip;
  private java.sql.Timestamp recordNowData;
  private String rsuDate;
  private String lprDate;
  private Integer status;
  private java.sql.Timestamp createDate;
  private java.sql.Timestamp updateTime;
  private String mac;
  private Integer count;
  private Integer countAlive;
  private String appInfo;
  private Integer isPing;
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
