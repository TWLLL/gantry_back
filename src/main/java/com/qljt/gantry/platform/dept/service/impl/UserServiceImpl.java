package com.qljt.gantry.platform.dept.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.dept.service.intf.SysUserService;
import com.qljt.gantry.platform.rolepermisson.bean.SysUserRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.mapper.UserRoleRelMapper;
import com.qljt.gantry.platform.shiro.ShiroUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-28 15:25
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper,UserEntity>
        implements SysUserService{

    @Autowired
    private UserRoleRelMapper userRoleRelMapper;

    @Override
    public List<UserEntity> pageQuery(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("username") != null && !"".equals(String.valueOf(params.get("username")))) {
//            map.put("user_name", params.get("username"));
            map.put("username", params.get("username"));
        }
        if (params.get("deptId") != null && !"".equals(String.valueOf(params.get("deptId")))) {
//            map.put("dept_id", params.get("deptId"));
            map.put("deptId", params.get("deptId"));
        }
        if (params.get("email") != null && !"".equals(String.valueOf(params.get("email")))) {
            map.put("email", params.get("email"));
        }
        if (params.get("mobile") != null && !"".equals(String.valueOf(params.get("mobile")))) {
            map.put("mobile", params.get("mobile"));
        }
        if (params.get("status") != null && !"".equals(String.valueOf(params.get("status")))) {
            map.put("status", params.get("status"));
        }
//        return this.baseMapper.selectByMap(map);
        return this.baseMapper.pageQuery(map);
    }

    @Override
    public Integer addObj(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", params.get("userName"));
        List<UserEntity> entityList = this.pageQuery(map);
        if (entityList != null && !entityList.isEmpty() && entityList.size() > 0){
            return -1;
        }
        UserEntity entity = new UserEntity();
        entity.setUserName(String.valueOf(params.get("userName")));
        entity.setEmail(String.valueOf(params.get("email")));
        entity.setMobile(String.valueOf(params.get("mobile")));
        entity.setDeptId(Long.valueOf(String.valueOf(params.get("deptId"))));
        entity.setStatus(Integer.parseInt(String.valueOf(params.get("status"))));
        String salt = RandomStringUtils.randomAlphanumeric(20);
        entity.setSalt(salt);
        String passWord = ShiroUtils.sha256(entity.getMobile(),salt);
        entity.setPassword(passWord);
        this.baseMapper.insert(entity);
        SysUserRoleRelEntity userRoleRelEntity = new SysUserRoleRelEntity();
        userRoleRelEntity.setUserId(entity.getUserId());
        userRoleRelEntity.setRoleId(Long.valueOf(String.valueOf(params.get("roleId"))));
        return userRoleRelMapper.insert(userRoleRelEntity);
    }

    @Override
    public Integer updateObj(Map<String, Object> params) {
        UserEntity entity = new UserEntity();
        if (params.get("userId") != null && !"".equals(String.valueOf(params.get("userId") != null))) {
            entity.setUserId(Long.valueOf(String.valueOf(params.get("userId"))));
        }
        if (params.get("userName") != null && !"".equals(String.valueOf(params.get("userName") != null))) {
            entity.setUserName(String.valueOf(params.get("userName")));
        }
        if (params.get("email") != null && !"".equals(String.valueOf(params.get("email") != null))) {
            entity.setEmail(String.valueOf(params.get("email")));
        }
        if (params.get("mobile") != null && !"".equals(String.valueOf(params.get("mobile") != null))) {
            entity.setMobile(String.valueOf(params.get("mobile")));
        }
        if (params.get("deptId") != null && !"".equals(String.valueOf(params.get("deptId") != null))) {
            entity.setDeptId(Long.valueOf(String.valueOf(params.get("deptId"))));
        }
        if (params.get("status") != null && !"".equals(String.valueOf(params.get("status") != null))) {
            entity.setStatus(Integer.parseInt(String.valueOf(params.get("status"))));
        }
        return this.baseMapper.updateById(entity);
    }

    @Override
    public Integer delObj(Map<String, Object> params) {
        Map<String, Object> map = new HashMap<>();
        if (params.get("userId") != null && !"".equals(String.valueOf(params.get("userId")))) {
            map.put("user_id", params.get("userId"));
        }
        return this.baseMapper.deleteByMap(map);
    }
}


