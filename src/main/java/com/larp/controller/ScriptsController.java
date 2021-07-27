package com.larp.controller;


import com.larp.common.lang.Result;
import com.larp.entity.Clues;
import com.larp.entity.Scripts;
import com.larp.service.ScriptsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/scripts")
public class ScriptsController {

    @Autowired
    ScriptsService scriptsService;

    /**
     * 【管理】获取角色当前轮次前的剧本
     * @param roleId
     * @return
     */
    @GetMapping("/getScripts")
    public Result getScripts(@RequestParam int roleId,
                             @RequestParam int round) {
        List<Scripts> scripts = scriptsService.getScripts(roleId, round);
        return Result.success(scripts);
    }
}
