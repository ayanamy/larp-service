package com.larp.service;

import com.larp.entity.Clues;
import com.larp.entity.Scripts;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
public interface ScriptsService extends IService<Scripts> {

    List<Scripts> getScripts(int roleId,int round);

    List<Scripts> getAllScripts(int roleId);
}
