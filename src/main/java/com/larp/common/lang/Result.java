package com.larp.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public static Result success(Object data){
        Result r  = new Result();
        r.setCode(200);
        r.setData(data);
        r.setMsg("");
        return r;
    };

    public static Result fail(String msg){
      return   fail(500,msg,null);
    };
    public static Result fail(String msg,Object data){
       return fail(500,msg,data);
    };

    public static Result fail(int code, String msg,Object data){
        Result r  = new Result();
        r.setCode(code);
        r.setData(data);
        r.setMsg(msg);
        return r;
    };
}
