package com.qljt.gantry.datasycn.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "net_connect_process")
@ApiModel
public class NetConnectProcess extends BaseEntity{

  @TableId(value = "id")
  private Long id;
  private String ip;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
  private Integer type;
  private Integer errorType;
  private String errorInfo;
  private Integer status;
  @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;

}
