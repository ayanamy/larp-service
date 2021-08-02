package com.larp.controller;


import com.larp.common.lang.Result;
import com.larp.common.lang.WSResult;
import com.larp.constants.MessageEnum;
import com.larp.entity.Game;
import com.larp.service.CluesService;
import com.larp.service.GameService;
import com.larp.service.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/dm")
public class DMController {
    @Autowired
    GameService gameService;

    @Autowired
    WebSocket webSocket;

    @PostMapping("/setNextRound/{gameId}")
    public Result setNextRound(@PathVariable Integer gameId) throws IOException {
        gameService.setNextRound(gameId);
        webSocket.sendInfo(MessageEnum.SET_NEXT_ROUND.getCode(), null);
        return Result.success(true);
    }

    @PostMapping("/setClueEnableToggle/{gameId}")
    public Result setClueEnableToggle(@PathVariable Integer gameId) throws IOException {
        Game game = gameService.getById(gameId);
        if (game == null) {
            return Result.fail("游戏不存在");
        }
        game.setCluesEnable(game.getCluesEnable() == 1 ? 0 : 1);
        gameService.updateById(game);
        String res = WSResult.build(MessageEnum.OPEN_CLUE.getCode());
        webSocket.sendInfo(res, null);
        return Result.success(true);
    }


}
