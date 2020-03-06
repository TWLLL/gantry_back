package com.qljt.gantry.platform.exception;

/**
 * @author liuliangliang
 * @create 2020-03-02 14:55
 */
public class BusiException extends RuntimeException{

    private static final long serialVersionUID = 1L;
    private String msg;
    private int code = 500;

    public BusiException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public BusiException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public BusiException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }

    public BusiException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }
}
