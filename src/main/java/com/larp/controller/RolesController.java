package com.larp.controller;


import com.larp.common.lang.Result;
import com.larp.service.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
@RestController
@RequestMapping("/roles")
public class RolesController {
    @Autowired
    RolesService rolesService;

    /**
     * 获取当前剧本杀的角色
     * @param gameId
     * @return
     */
    @GetMapping("/list")
    public Result list(@RequestParam int gameId) {
        return Result.success(rolesService.getRolesByGame(gameId));
    }
}
