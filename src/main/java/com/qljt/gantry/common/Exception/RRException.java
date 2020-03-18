package com.qljt.gantry.common.Exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author : LY
 * @created with IntelliJ IDEA.
 * @date : 2019/4/17
 * @time : 10:08
 * @description :统一异常
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RRException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private String msg;
    private int code = 500;

    public RRException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public RRException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public RRException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public RRException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
