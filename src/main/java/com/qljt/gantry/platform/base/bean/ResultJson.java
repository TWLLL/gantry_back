package com.qljt.gantry.platform.base.bean;

/**
 * @author liuliangliang
 * @create 2020-02-24 18:07
 */
public class ResultJson<T> {
    private int code;// 业务自定义状态码

    private String msg;// 请求状态描述，调试用

    private T data;// 请求数据，对象或数组均可

    public ResultJson() {
    }

    /**
     * 成功时候的调用
     * @param data data
     * @param <T> t
     * @return Result
     */
    public static <T> ResultJson<T> success(T data){
        return new ResultJson<T>(data);
    }

    /**
     * 失败时候的调用
     * @param codeMsg codeMsg
     * @param <T> t
     * @return Result
     */
    public static <T> ResultJson<T> error(CodeMsg codeMsg){
        return new ResultJson<T>(codeMsg);
    }

    /**
     * 成功的构造函数
     * @param data data
     */
    public ResultJson(T data){
        this.code = 20000;//默认200是成功
        this.msg = "SUCCESS";
        this.data = data;
    }

    public ResultJson(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 失败的构造函数
     * @param codeMsg codeMsg
     */
    private ResultJson(CodeMsg codeMsg) {
        if(codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }




}
