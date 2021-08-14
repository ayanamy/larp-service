package com.larp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.larp.entity.Clues;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
public interface CluesService extends IService<Clues> {
    Clues getNewClues(Integer roleId, Integer gameId, String location);

    List<Clues> getMyClues(Integer roleId);

    Clues share(Integer id);

    List<Map<String, Object>> getLocation(Integer gameId, Integer round);
}
