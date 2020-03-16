package com.qljt.gantry.platform.dept.service.intf;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import org.apache.catalina.User;

import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-28 15:26
 */
public interface SysUserService extends IService<UserEntity> {

    List<UserEntity> pageQuery(Map<String, Object> params);

    Integer addObj(Map<String, Object> params);

    Integer updateObj(Map<String, Object> params);

    Integer delObj(Map<String, Object> params);
}
