package com.larp.constants;

public enum MessageEnum {
    SHARE_CLUE("share_clue", "分享线索"),
    OPEN_CLUE("open_clue", "开启线索"),
    START_VOTE("start_vote", "发起投票"),
    SET_NEXT_ROUND("set_next_round", "进入下一轮");

    private String code;
    private String name;

    MessageEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
