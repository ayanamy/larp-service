package com.larp.controller;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.larp.common.lang.Result;
import com.larp.entity.Clues;
import com.larp.entity.Game;
import com.larp.service.CluesService;
import com.larp.service.GameService;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/getNewClues")
    public Result getNewClues(Integer roleId) {
        Clues clues = cluesService.getNewClues(roleId);
        return Result.success(clues);
    }

    @GetMapping("/getMyClues")
    public Result getMyClues(Integer roleId) {
        List<Clues> clues = cluesService.getMyClues(roleId);
        return Result.success(clues);
    }
}
