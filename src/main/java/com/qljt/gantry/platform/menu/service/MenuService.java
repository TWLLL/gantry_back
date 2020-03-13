package com.qljt.gantry.platform.menu.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qljt.gantry.platform.menu.bean.SysMenuEntity;
import com.qljt.gantry.platform.menu.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author liuliangliang
 * @create 2020-02-28 11:12
 *
 */
@Service
public class MenuService extends ServiceImpl<MenuMapper, SysMenuEntity> {
    @Autowired
    // yf：该注解提交时删除！！
    @SuppressWarnings("all")
    MenuMapper mapper;

    public List<SysMenuEntity> getAllTopRouter(){
        QueryWrapper<SysMenuEntity> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("PID",0);
        List<SysMenuEntity> sysDistRouters = mapper.selectList(queryWrapper);
        // yf：处理一遍之后放入最终Set返回
        return sysDistRouters;
    }

    public List<SysMenuEntity> findSonRouterByPID(Long pid){
        QueryWrapper<SysMenuEntity> queryWrapper  = new QueryWrapper<>();
        queryWrapper.eq("PID",pid);
        List<SysMenuEntity> sysDistRouters = mapper.selectList(queryWrapper);
        return sysDistRouters;
    }
}
