package com.qljt.gantry.platform.shiro;


import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.menu.bean.SysMenuEntity;
import com.qljt.gantry.platform.menu.mapper.MenuMapper;
import com.qljt.gantry.platform.rolepermisson.bean.SysUserRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.mapper.RoleMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.UserRoleRelMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

//import sun.net.www.protocol.http.AuthenticationInfo;

/**
 * @author liuliangliang
 * @create 2020-02-27 18:53
 */
@Component
public class UserRealm extends AuthorizingRealm {

    //超级管理员ID
    public static final Long ADMIN_ID = 1L;
    //用户账号状态0为锁定状态
    public static final int  USER_STATUS_LOCK = 0;

    @Autowired
    @SuppressWarnings("all")
    private UserMapper userMapper;

    @Autowired
    @SuppressWarnings("all")
    private MenuMapper menuMapper;

    @Autowired
    @SuppressWarnings("all")
    private RoleMapper roleMapper;

    @Autowired
    @SuppressWarnings("all")
    private UserRoleRelMapper userRoleRelMapper;


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        UserEntity user = (UserEntity) principalCollection.getPrimaryPrincipal();
        Long userId = user.getUserId();

        //权限列表
        List<String> permsList = null;
        //角色列表
        Set<String> rolesSet = null;
        //超级管理员的权限
        if (ADMIN_ID.equals(userId)) {
            List<SysMenuEntity> menuEntityList = this.menuMapper.selectList(null);
            permsList = new ArrayList<>(menuEntityList.size());
            //超级管理员拥有所有的menu的权限
            for (SysMenuEntity sysMenuEntity : menuEntityList){
//                permsList.add(sysMenuEntity.getPerms());
            }
        }
        //非管理员角色的情况
        else {
            permsList = this.userMapper.queryAllPerms(userId);
            Map<String,Object> paramsMap = new HashMap<>();
            paramsMap.put("user_id",userId);
            List<SysUserRoleRelEntity> userRoleRelEntityList =  this.userRoleRelMapper.selectByMap(paramsMap);
            for (SysUserRoleRelEntity entity : userRoleRelEntityList) {
                rolesSet.add(entity.getRoleId().toString());
            }
        }

        Set permsSet = new HashSet();
        for (String perms : permsList) {
            if (!StringUtils.isBlank(perms)) {
                permsSet.addAll(Arrays.asList(perms.trim().split(",")));
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        info.setRoles(rolesSet);

        return info;
    }

    /**
     * 登录鉴权
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected org.apache.shiro.authc.AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        UserEntity userEntity = new UserEntity();
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("USER_NAME",token.getUsername());
        List<UserEntity> userEntityList = this.userMapper.selectByMap(paramsMap);
        // yf：加上了&& userEntityList.size() > 0
        if (userEntityList != null && userEntityList.size() > 0) {
            userEntity = userEntityList.get(0);
        }
        // yf：由原来的userEntity == null改为userEntity.getUserName() == null
        if (userEntity.getUserName() == null) {
            throw new UnknownAccountException("账号或者密码不正确");
        }

        if (USER_STATUS_LOCK == userEntity.getStatus()) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userEntity, userEntity.getPassword(), ByteSource.Util.bytes(userEntity.getSalt()), getName());
        return info;
    }


    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName("SHA-256");
        shaCredentialsMatcher.setHashIterations(16);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }

}
