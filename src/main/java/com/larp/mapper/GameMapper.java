package com.larp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.larp.entity.Game;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
public interface GameMapper extends BaseMapper<Game> {
    @Select("select * from game where status=1 limit 1")
    Game getCurrentGame();
}
