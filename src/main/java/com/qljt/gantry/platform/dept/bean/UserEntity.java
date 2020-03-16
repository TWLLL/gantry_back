package com.qljt.gantry.platform.dept.bean;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.qljt.gantry.platform.annotation.Mobile;
import com.qljt.gantry.platform.base.bean.BaseEntity;
import com.qljt.gantry.platform.annotation.groupvalidate.Insert;
import com.qljt.gantry.platform.annotation.groupvalidate.Update;
import lombok.Data;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Date;


/**
 * @author liuliangliang
 * @create 2020-02-26 17:24
 */
@Data
@TableName("SYS_USER")
public class UserEntity extends BaseEntity{

    @TableId
    private Long userId;

    @NotBlank(message = "用户名不能为空",groups = {Insert.class, Update.class})
    private String userName;

    //需要设置password属性就只能写入，不会显示出来
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotBlank(message = "密码不能为空",groups = {Insert.class})
    private String password;

    private String salt;

    @NotBlank(message = "邮箱不能为空", groups = {Insert.class, Update.class})
    @Email(message = "邮箱格式不正确", groups = {Insert.class, Update.class})
    private String email;

    @NotBlank(message = "邮箱不能为空", groups = {Insert.class, Update.class})
    //自定义标签
    @Mobile(message = "邮箱格式不正确", groups = {Insert.class, Update.class})
    private String mobile;

    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    private Long deptId;

    @TableField(exist = false)
    private String deptName;
}
