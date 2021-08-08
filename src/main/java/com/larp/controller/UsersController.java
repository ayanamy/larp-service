package com.larp.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.common.lang.Result;
import com.larp.entity.Users;
import com.larp.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hippo
 * @since 2021-07-22
 */
@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    UsersService usersService;

    @PostMapping("/login")
    public Result login(@RequestBody Users users) {
        QueryWrapper<Users> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", users.getName()).eq("password", users.getPassword());
        Users users1 = usersService.getOne(queryWrapper);
        if (users1 == null) {
            return Result.fail("账号或者密码不对");
        }
        return Result.success(users1);
    }

    /**
     * 【管理】获取所有用户用来选择dm
     * @return
     */
    @GetMapping("/getList")
    public Result getList() {
        List<Users> usersList = usersService.list(null);
        return Result.success(usersList);
    }

}
