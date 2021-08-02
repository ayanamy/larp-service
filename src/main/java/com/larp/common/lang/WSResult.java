package com.larp.common.lang;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

@Data
public class WSResult {
    private String from;
    private String type;
    private Object data;

    public static String build(String type, Object data, String from){
        WSResult r  = new WSResult();
        r.setData(data);
        r.setFrom(from);
        r.setType(type);
        JSONObject json = JSONUtil.parseObj(r, false);
        return json.toString();
    };

    public static String build(String type){
        WSResult r  = new WSResult();
        r.setType(type);
        JSONObject json = JSONUtil.parseObj(r, false);
        return json.toString();
    };
}
