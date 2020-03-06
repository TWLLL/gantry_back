package com.qljt.gantry.platform.base.bean;

import com.baomidou.mybatisplus.annotation.TableField;

import java.util.HashMap;
import java.util.Map;

/**
 * @author liuliangliang
 * @create 2020-02-27 19:32
 */
public class BaseModel {

    public static final String RESULT = "data";
    /**
     * 承载与web交互的数据，不可入数据库
     */
    @TableField(exist = false)
    private Map<String, Object> params;


    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<String, Object>();
            params.put(RESULT,null);
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
