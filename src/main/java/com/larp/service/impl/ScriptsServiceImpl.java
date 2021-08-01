package com.larp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.common.exception.CommonException;
import com.larp.entity.Clues;
import com.larp.entity.Game;
import com.larp.entity.Roles;
import com.larp.entity.Scripts;
import com.larp.mapper.ScriptsMapper;
import com.larp.service.GameService;
import com.larp.service.RolesService;
import com.larp.service.ScriptsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
@Service
public class ScriptsServiceImpl extends ServiceImpl<ScriptsMapper, Scripts> implements ScriptsService {

    @Resource
    private ScriptsMapper scriptsMapper;

    @Resource
    private RolesService rolesService;

    @Resource
    private GameService gameService;

    @Override
    public List<Scripts> getScripts(int roleId,int gameId) {
        Roles roles = rolesService.getById(roleId);
        if(roles==null){
            throw new CommonException("该角色不存在");
        }
        Game game = gameService.getById(gameId);
        QueryWrapper<Scripts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).le("round",game.getRound());
        List<Scripts> scripts = scriptsMapper.selectList(queryWrapper);
        return scripts;
    }

    @Override
    public List<Scripts> getAllScripts(int roleId) {
        QueryWrapper<Scripts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId);
        List<Scripts> scripts = scriptsMapper.selectList(queryWrapper);
        return scripts;
    }
}
