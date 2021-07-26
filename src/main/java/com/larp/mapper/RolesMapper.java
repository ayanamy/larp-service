package com.larp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.larp.entity.Roles;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
public interface RolesMapper extends BaseMapper<Roles> {
    List<Roles> selectByGame(int gameId);
}
