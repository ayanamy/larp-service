package com.larp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.larp.entity.Clues;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
public interface CluesMapper extends BaseMapper<Clues> {

    List<Clues> getPrevClues(int roleId, int round);

    List<Clues> getMyClues(int roleId, int gameId);

    Clues getNewClues(int gameId, int round,String location);
}
