package com.larp.service.impl;

import com.larp.entity.Roles;
import com.larp.mapper.RolesMapper;
import com.larp.service.RolesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
@Service
public class RolesServiceImpl extends ServiceImpl<RolesMapper, Roles> implements RolesService {
    @Resource
    private RolesMapper rolesMapper;

    @Override
    public List<Roles> getRolesByGame(int gameId) {
        return rolesMapper.selectByGame(gameId);
    }
}
