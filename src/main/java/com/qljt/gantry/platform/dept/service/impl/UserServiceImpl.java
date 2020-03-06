package com.qljt.gantry.platform.dept.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.dept.service.intf.SysUserService;
import org.springframework.stereotype.Service;

/**
 * @author liuliangliang
 * @create 2020-02-28 15:25
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity>
        implements SysUserService{

}


