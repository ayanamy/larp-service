package com.larp.controller;


import com.larp.common.lang.Result;
import com.larp.entity.Game;
import com.larp.service.GameService;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    GameService gameService;

    /**
     * 【玩家】获取当前的剧本杀信息
     *
     * @return
     */
    @GetMapping("/getCurrentGame")
    public Result getCurrentGame() {
        Game game = gameService.getCurrentGame();
        return Result.success(game);
    }

    /**
     * 【管理】获取游戏列表
     *
     * @return
     */
    @GetMapping("/list")
    public Result list() {
        List<Game> game = gameService.getList();
        return Result.success(game);
    }

    /**
     * 【管理】删除游戏
     *
     * @param gameId
     * @return
     */
    @PostMapping("/delete/{gameId}")
    public Object delete(@PathVariable Integer gameId) {
        System.out.println(gameId);
        gameService.delete(gameId);
        return Result.success(true);
    }

    /**
     * 【管理】创建游戏
     *
     * @param scripts
     * @param clues
     * @param gameName
     * @param maxClues
     * @param description
     * @param roundTotal
     * @param maxUser
     * @param minUser
     * @return
     * @throws IOException
     */
    @PostMapping("/create")
    public Result create(@RequestParam(value = "scripts", required = false) MultipartFile[] scripts,
                         @RequestParam(value = "clues", required = false) MultipartFile[] clues,
                         String gameName,
                         Integer maxClues,
                         String description,
                         Integer roundTotal,
                         Integer maxUser,
                         Integer minUser
    ) throws IOException {
        Game game = new Game();
        game.setGameName(gameName);
        game.setDescription(description);
        game.setMaxClues(maxClues);
        game.setRoundTotal(roundTotal);
        game.setMaxUser(maxUser);
        game.setMinUser(minUser);
        System.out.println(game);
        gameService.create(game, scripts, clues);
        return Result.success(true);
    }

    /**
     * 【管理】获取游戏详情 包括角色剧情线索
     *
     * @param gameId
     * @return
     */
    @PostMapping("/detail/{gameId}")
    public Result detail(@PathVariable Integer gameId) {
        Object obj = gameService.getGameDetail(gameId);
        return Result.success(obj);
    }

    /**
     * 上传后初始化角色并附带剧本
     * @param gameId
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping("/initRoles/{gameId}")
    public Result initRoles(@PathVariable Integer gameId) throws IOException {
        gameService.initRoles(gameId);
        return Result.success(true);
    }
}
