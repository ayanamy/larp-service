package com.larp.controller;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.larp.common.lang.Result;
import com.larp.common.lang.WSResult;
import com.larp.constants.MessageEnum;
import com.larp.entity.Clues;
import com.larp.entity.Game;
import com.larp.service.CluesService;
import com.larp.service.GameService;
import com.larp.service.WebSocket;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
@RestController
@RequestMapping("/clues")
public class CluesController {

    @Autowired
    CluesService cluesService;

    @Autowired
    WebSocket webSocket;

    @PostMapping("/getNewClues")
    public Result getNewClues(Integer roleId) {
        Clues clues = cluesService.getNewClues(roleId);
        return Result.success(clues);
    }

    @GetMapping("/getMyClues")
    public Result getMyClues(@RequestParam Integer roleId) {
        List<Clues> clues = cluesService.getMyClues(roleId);
        return Result.success(clues);
    }

    @PostMapping("/share")
    public Result share(@RequestParam Integer clueId, @RequestParam String user) throws IOException {
        Clues clues = cluesService.share(clueId);
        String res = WSResult.build(MessageEnum.SHARE_CLUE.getCode(), clues, user);
        webSocket.sendInfo(res, null);
        return Result.success(true);
    }

}
