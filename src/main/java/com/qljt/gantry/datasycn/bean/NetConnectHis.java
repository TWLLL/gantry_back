package com.qljt.gantry.datasycn.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "net_connect_his")
@ApiModel
public class NetConnectHis extends BaseEntity{

  @TableId(value = "id")
  private Long id;
  private String ip;
  private java.sql.Timestamp createTime;
  private Integer type;
  private Integer errorType;
  private String errorInfo;
  private Integer status;
  private java.sql.Timestamp updateTime;

}
