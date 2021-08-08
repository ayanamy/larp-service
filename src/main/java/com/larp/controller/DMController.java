package com.larp.controller;


import com.larp.common.lang.Result;
import com.larp.common.lang.WSResult;
import com.larp.constants.MessageEnum;
import com.larp.entity.Game;
import com.larp.service.CluesService;
import com.larp.service.GameService;
import com.larp.service.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/dm")
public class DMController {
    @Autowired
    GameService gameService;

    @Autowired
    WebSocket webSocket;

    /**
     * 进入下一章
     *
     * @param gameId
     * @return
     * @throws IOException
     */
    @PostMapping("/setNextRound/{gameId}")
    public Result setNextRound(@PathVariable Integer gameId) throws IOException {
        gameService.setNextRound(gameId);
        String res = WSResult.build(MessageEnum.SET_NEXT_ROUND.getCode());
        webSocket.sendInfo(res, null);
        return Result.success(true);
    }

    /**
     * 开关线索
     *
     * @param gameId
     * @return
     * @throws IOException
     */
    @PostMapping("/setClueEnableToggle/{gameId}")
    public Result setClueEnableToggle(@PathVariable Integer gameId) throws IOException {
        Game game = gameService.getById(gameId);
        if (game == null) {
            return Result.fail("游戏不存在");
        }
        game.setCluesEnable(game.getCluesEnable() == 1 ? 0 : 1);
        gameService.updateById(game);
        if (game.getCluesEnable() == 1) {
            String res = WSResult.build(MessageEnum.OPEN_CLUE.getCode());
            webSocket.sendInfo(res, null);
        }
        return Result.success(true);
    }

    /**
     * 【管理】开启投票
     * @param gameId
     * @param voteItem
     * @return
     * @throws IOException
     */
    @PostMapping("/startVote/{gameId}")
    public Result startVote(@PathVariable Integer gameId, @RequestParam String[] voteItem) throws IOException {

        Game game = gameService.getById(gameId);
        if (game == null) {
            return Result.fail("游戏不存在");
        }
        game.setRound(999);
        String res = WSResult.build(MessageEnum.START_VOTE.getCode(), voteItem, null);
        webSocket.sendInfo(res, null);
        gameService.updateById(game);
        return Result.success(true);
    }
}
