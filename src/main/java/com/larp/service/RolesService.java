package com.larp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.larp.entity.Roles;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
public interface RolesService extends IService<Roles> {
    List<Roles> getRolesByGame(int gameId,String user);
}
