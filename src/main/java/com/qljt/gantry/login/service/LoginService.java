package com.qljt.gantry.login.service;

import com.qljt.gantry.platform.base.bean.BaseModel;
import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.menu.bean.SysMenuEntity;
import com.qljt.gantry.platform.menu.mapper.MenuMapper;
import com.qljt.gantry.platform.rolepermisson.bean.SysMenuRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysRoleEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysUserRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.mapper.MenuRoleRelMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.RoleMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.UserRoleRelMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.ibatis.mapping.ResultMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.*;

/**
 * @author liuliangliang
 * @create 2020-03-02 19:34
 */
@Service
public class LoginService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleRelMapper userRoleRelMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private MenuRoleRelMapper menuRoleRelMapper;

    @Autowired
    private MenuMapper menuMapper;

    public Map<String,Object> getLoginInfo(String username) {
        //获取用户信息
        Map<String,Object> paramsMap = new HashMap<>();
        UserEntity user = null;
        paramsMap.put("USER_NAME",username);
        List<UserEntity> userEntityList = this.userMapper.selectByMap(paramsMap);
        if (userEntityList != null) {
            user = userEntityList.get(0);
        }

        //获取用户和角色关系信息
        Map<String,Object> userRoleMap = new HashMap<>();
        userRoleMap.put("user_id",user.getUserId());
        List<SysUserRoleRelEntity> userRoleRelEntityList = userRoleRelMapper.selectByMap(userRoleMap);
        //List<SysRole> roles = sysRoleService.selectRoleByUserId(user.getId());

        //获取角色信息
        Set<SysRoleEntity> roleList = new HashSet<>();
        for (SysUserRoleRelEntity userRoleRelEntity : userRoleRelEntityList) {
            roleList.add(roleMapper.selectById(userRoleRelEntity.getRoleId()));
        }


        //获取角色菜单关系的信息
        Set<SysMenuEntity> menuEntitySet = new HashSet<>();
        for (SysRoleEntity roleEntity : roleList) {
            Map<String,Object> menuRoleRelMap = new HashMap<>();
            menuRoleRelMap.put("role_id",roleEntity.getRoleId());
            List<SysMenuRoleRelEntity> menuRoleRelEntityList = menuRoleRelMapper.selectByMap(menuRoleRelMap);
            for (SysMenuRoleRelEntity sysMenuRoleRelEntity : menuRoleRelEntityList) {
                SysMenuEntity menuEntity = menuMapper.selectById(sysMenuRoleRelEntity.getMenuId());
                menuEntitySet.add(menuEntity);
            }
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("userInfo",user);
        resultMap.put("roleInfo",roleList);
        resultMap.put("menuInfo",menuEntitySet);
        String encryptionKey= DigestUtils.sha256Hex(user.getSalt()+user.getUserName());
        resultMap.put("token", encryptionKey);


//        Set<String> permissionList = new HashSet<>();
//        for (SysRole role : roles) {
//            roleList.add(role.getRole());//角色存储
//        }
//        //此处如果多个角色都拥有某项权限，bu会数据重复，内部用的是Set
//        List<SysPermission> sysPermissions = sysPermissionService.selectPermByRole(roles);
//        for (SysPermission perm : sysPermissions) {
//            permissionList.add(perm.getPermission());//权限存储
//        }

        return  resultMap;
    }


    public Map<String,Object> getRoleInfo(Long userId) {
        UserEntity userEntity = this.userMapper.selectById(userId);

        //获取用户和角色关系信息
        Map<String,Object> userRoleMap = new HashMap<>();
        userRoleMap.put("user_id",userId);
        List<SysUserRoleRelEntity> userRoleRelEntityList = userRoleRelMapper.selectByMap(userRoleMap);

        //获取角色信息
        Set<SysRoleEntity> roleList = new HashSet<>();
        for (SysUserRoleRelEntity userRoleRelEntity : userRoleRelEntityList) {
            roleList.add(roleMapper.selectById(userRoleRelEntity.getRoleId()));
        }
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("roles",roleList);
        resultMap.put("name",userEntity.getUserName());
        resultMap.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
        resultMap.put("introduction","智能运维平台 所有权归山东通维所有");

        return resultMap;
    }

}
