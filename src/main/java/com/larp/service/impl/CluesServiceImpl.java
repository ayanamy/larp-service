package com.larp.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.common.exception.CommonException;
import com.larp.common.lang.Result;
import com.larp.entity.Clues;
import com.larp.entity.Game;
import com.larp.mapper.CluesMapper;
import com.larp.service.CluesService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.larp.service.GameService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hippo
 * @since 2021-07-23
 */
@Service
public class CluesServiceImpl extends ServiceImpl<CluesMapper, Clues> implements CluesService {

    @Resource
    CluesMapper cluesMapper;

    @Resource
    GameService gameService;

    @Override
    public Clues getNewClues(Integer roleId, Integer gameId, String location) {
        // 验证当前是否开启了线索
        Game game = gameService.getById(gameId);
        if (game.getRound() == null || game.getRound() < 1 || game.getCluesEnable() == null || game.getCluesEnable() < 1) {
            throw new CommonException("当前未开启线索");
        }
        // 获取这一轮已经获取的线索
        List<Clues> clues = cluesMapper.getPrevClues(roleId, game.getRound());

        // 判断最大次数
        if (game.getMaxClues() != null && game.getMaxClues() > 0 && clues.size() >= game.getMaxClues()) {
            throw new CommonException("本轮线索已获取了最大值");
        }
        // 和上次线索比较是否时间到了
        if (clues != null && !clues.isEmpty()) {
            Clues clue = clues.get(0);
            Date pickTime = clue.getPickTime();
            System.out.println(pickTime);
            long diff = DateUtil.between(pickTime, DateUtil.date(), DateUnit.MINUTE);
            System.out.println(diff);
            if (diff < 2) {
                throw new CommonException("请等待" + (2 - diff) + "分钟后进行获取!");
            }
        }
        // 判断当前是否存在线索 有的话就给出线索
        QueryWrapper<Clues> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_id", gameId).eq("round", game.getRound());

        int num = cluesMapper.selectCount(queryWrapper);
        if (num == 0) {
            throw new CommonException("线索已经用用完了，请等待下一轮");
        }
        Clues newClues = cluesMapper.getNewClues(game.getId(), game.getRound(), location);
        if (newClues == null) {
            throw new CommonException("当前区域已没有线索，请更换其他地点");
        }
        newClues.setStatus(1);
        newClues.setRoleId(roleId);
        newClues.setPickTime(DateUtil.date());
        cluesMapper.updateById(newClues);
        return newClues;
    }

    @Override
    public List<Clues> getMyClues(Integer roleId) {
        Game game = gameService.getCurrentGame();
        return cluesMapper.getMyClues(roleId, game.getId());
    }

    @Override
    public Clues share(Integer id) {
        Clues clues = cluesMapper.selectById(id);
        clues.setStatus(2);
        cluesMapper.updateById(clues);
        return clues;
    }

    @Override
    public List<Map<String, Object>> getLocation(Integer gameId, Integer round) {
        QueryWrapper<Clues> queryWrapper = new QueryWrapper<Clues>();
        queryWrapper.eq("game_id", gameId).eq("round", round).groupBy("location").select("location");
        return cluesMapper.selectMaps(queryWrapper);
    }
}
