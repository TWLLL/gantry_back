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
@TableName(value = "ipc_status_his")
@ApiModel
public class IpcStatusHis extends BaseEntity{

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
  private java.sql.Timestamp createdate;
  @TableField(value = "exfield_1")
  private String exfield1;
  @TableField(value = "exfield_2")
  private String exfield2;

}
