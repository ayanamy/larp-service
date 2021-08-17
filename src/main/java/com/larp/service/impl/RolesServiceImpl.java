package com.larp.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.entity.Game;
import com.larp.entity.Roles;
import com.larp.mapper.RolesMapper;
import com.larp.service.GameService;
import com.larp.service.RolesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements RolesService {
    @Resource
    private RolesMapper rolesMapper;

    @Resource
    GameService gameService;

    @Override
    public List<Roles> getRolesByGame(int gameId, String user) {
        if (!StrUtil.hasBlank(user)) {
            Game game = gameService.getById(gameId);
            if (game.getRound() != null && game.getRound() < 1) {
                QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("game_id", gameId).eq("user", user);
                Roles roles = rolesMapper.selectOne(queryWrapper);
                if (roles == null) {
                    return new ArrayList<Roles>();
                } else {
                    return rolesMapper.selectList(new QueryWrapper<Roles>().eq("game_id", gameId).isNotNull("user"));
                }
            }
        }

        return rolesMapper.selectByGame(gameId);
    }
}
