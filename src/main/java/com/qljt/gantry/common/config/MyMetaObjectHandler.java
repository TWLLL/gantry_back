package com.qljt.gantry.common.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author liuliangliang
 * @date  2020-03-12 09:46
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler{

    @Override
    public void insertFill(MetaObject metaObject) {
        setInsertFieldValByName("createTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
