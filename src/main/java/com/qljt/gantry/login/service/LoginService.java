package com.qljt.gantry.login.service;

import com.qljt.gantry.platform.dept.bean.UserEntity;
import com.qljt.gantry.platform.dept.mapper.UserMapper;
import com.qljt.gantry.platform.menu.bean.SysMenuEntity;
import com.qljt.gantry.platform.menu.mapper.MenuMapper;
import com.qljt.gantry.platform.menu.service.MenuService;
import com.qljt.gantry.platform.rolepermisson.bean.SysMenuRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysRoleEntity;
import com.qljt.gantry.platform.rolepermisson.bean.SysUserRoleRelEntity;
import com.qljt.gantry.platform.rolepermisson.mapper.MenuRoleRelMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.RoleMapper;
import com.qljt.gantry.platform.rolepermisson.mapper.UserRoleRelMapper;
import com.qljt.gantry.platform.shiro.ShiroUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author liuliangliang
 * @create 2020-03-02 19:34
 */
@Service
@SuppressWarnings("all")
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

    @Autowired
    MenuService routerService;

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

        return  resultMap;
    }

    public Map<String,Object> getRoleInfo(Long userId) {
        // 根据userId得user对象
        UserEntity userEntity = this.userMapper.selectById(userId);

        //获取用户和角色关系信息
        Map<String,Object> userRoleMap = new HashMap<>();
        userRoleMap.put("user_id",userId);
        List<SysUserRoleRelEntity> userRoleRelEntityList = userRoleRelMapper.selectByMap(userRoleMap);

        //获取角色信息
        Set<SysRoleEntity> roleList0 = new HashSet<>();
        for (SysUserRoleRelEntity userRoleRelEntity : userRoleRelEntityList) {
            roleList0.add(roleMapper.selectById(userRoleRelEntity.getRoleId()));
        }
        Set<String> roleNameList = new HashSet<>();
        String roleType = "noRoleType";
        String roleId = "noRoleId";
        for (SysRoleEntity entity : roleList0) {
            roleNameList.add(entity.getRoleName());
            roleType = entity.getRoleType() + "";
            roleId = entity.getRoleId() + "";
        }

        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("roles",roleNameList);
        resultMap.put("roleType",roleType);
        resultMap.put("roleId",roleId);
        resultMap.put("name",userEntity.getUserName());

        // 得roles对应的菜单
        //获取角色菜单关系的信息
        Set<SysMenuEntity> menuEntitySet = new HashSet<>();
        for (SysRoleEntity roleEntity : roleList0) {
            Map<String,Object> menuRoleRelMap = new HashMap<>();
            menuRoleRelMap.put("role_id",roleEntity.getRoleId());
            List<SysMenuRoleRelEntity> menuRoleRelEntityList = menuRoleRelMapper.selectByMap(menuRoleRelMap);
            for (SysMenuRoleRelEntity sysMenuRoleRelEntity : menuRoleRelEntityList) {
                SysMenuEntity menuEntity = menuMapper.selectById(sysMenuRoleRelEntity.getMenuId());
                menuEntitySet.add(menuEntity);
            }
        }
        List<SysMenuEntity> list = routerService.getAllTopRouter();

        // 渲染路由模板
        StringBuilder routers = new StringBuilder();
        routers.append("[");
        for (int k = 0;k < list.size();k ++)
        {
            routers.append("{ ");
            routers.append("\"path\": \"").append(list.get(k).getPath()).append("\", ");
            routers.append("\"name\": \"").append(list.get(k).getName()).append("\", ");
            if(list.get(k).getComponent() != null) routers.append("\"component\": \"").append(list.get(k).getComponent()).append("\", ");
            routers.append("\"alwaysShow\" : \"").append(list.get(k).getHidden() == 1 ? "true" : "false").append("\", ");
            routers.append("\"hidden\": \"").append(list.get(k).getHidden() == 1 ? "true" : "false").append("\" ");
            if(list.get(k).getRedirect() != null)
            {
                routers.append(", ");
                routers.append("\"redirect\": \"").append(list.get(k).getRedirect()).append("\" ");
            }
            if(list.get(k).getMeta() != null && list.get(k).getMeta() == 1) routers.append(", ");
            if(list.get(k).getMeta() != null && list.get(k).getMeta() == 1)
            {
                routers.append("\"meta\": { ");
                routers.append("\"title\": \"").append(list.get(k).getTitle()).append("\", ");
                routers.append("\"icon\": \"").append(list.get(k).getIcon()).append("\" ");
                routers.append("}");
            }
            List<SysMenuEntity> sonRouters = routerService.findSonRouterByPID(list.get(k).getId());
            if(sonRouters.size() > 0)
            {
                routers.append(", \"children\": ");
                routers.append("[ ");
                for (int i = 0;i < sonRouters.size();i ++)
                {
                    routers.append("{ ");
                    routers.append("\"path\": \"").append(sonRouters.get(i).getPath()).append("\", ");
                    routers.append("\"name\": \"").append(sonRouters.get(i).getName()).append("\", ");
                    routers.append("\"component\": \"").append(sonRouters.get(i).getComponent()).append("\", ");
                    routers.append("\"alwaysShow\" : \"").append(sonRouters.get(i).getHidden() == 1 ? "true" : "false").append("\", ");
                    routers.append("\"hidden\": \"").append(sonRouters.get(i).getHidden() == 1 ? "true" : "false").append("\" ");
                    if(sonRouters.get(i).getRedirect() != null)
                    {
                        routers.append(", ");
                        routers.append("\"redirect\": \"").append(sonRouters.get(i).getRedirect()).append("\" ");
                    }
                    if(sonRouters.get(i).getMeta() != null && sonRouters.get(i).getMeta() == 1) routers.append(", ");
                    if(sonRouters.get(i).getMeta() != null && sonRouters.get(i).getMeta() == 1)
                    {
                        routers.append("\"meta\": { ");
                        routers.append("\"title\": \"").append(sonRouters.get(i).getTitle()).append("\", ");
                        routers.append("\"icon\": \"").append(sonRouters.get(i).getIcon()).append("\" ");
                        routers.append("}");
                    }
                    routers.append("}");
                    if(i != sonRouters.size() - 1) routers.append(",");
                }
                routers.append("]");
            }
            routers.append("}");
            if(k != list.size() - 1) routers.append(",");
        }
        routers.append("]");
        resultMap.put("routers",routers);
        return resultMap;
    }
}
