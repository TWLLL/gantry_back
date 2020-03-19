package com.qljt.gantry.platform.quartz.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "schedule_job_log")
@ApiModel
public class ScheduleJobLogEntity implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableField(value = "id")
  private long id;
  private long jobId;
  private String beanName;
  private String methodName;
  private String params;
  private Integer status;
  private String error;
  private long times;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;

}
