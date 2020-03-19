package com.qljt.gantry.platform.quartz.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName(value = "schedule_job")
@ApiModel
public class ScheduleJobEntity implements Serializable {
  private static final long serialVersionUID = 1L;
  public static final String JOB_PARAM_KEY = "JOB_PARAM_KEY";

  @TableId(value = "JOB_ID")
  private long jobId;
  private String beanName;
  private String methodName;
  private String params;
  private String cronExpression;
  private Integer status;
  private String remark;
  @TableField(fill = FieldFill.INSERT)
  private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
  private Date updateTime;

}
