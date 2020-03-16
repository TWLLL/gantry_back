package com.qljt.gantry.datasycn.bean;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@TableName(value = "ip_device_rel")
@ApiModel
public class IpDeviceRel extends BaseEntity {

  @TableId(value = "ip")
  private String ip;
  private String type;

}
