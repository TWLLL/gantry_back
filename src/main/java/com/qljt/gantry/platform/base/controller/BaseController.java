package com.qljt.gantry.platform.base.controller;

import com.qljt.gantry.platform.dept.bean.UserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author liuliangliang
 * @create 2020-02-26 17:10
 */
public class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected UserEntity getUser() {
        return (UserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    protected Long getUserId() {
        return getUser().getUserId();
    }

    protected Long getDeptId() {
        return getUser().getDeptId();
    }

    protected String getGuid(){
        return UUID.randomUUID().toString();
    }


}
