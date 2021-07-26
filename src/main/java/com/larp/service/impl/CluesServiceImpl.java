package com.larp.service.impl;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
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
    public Clues getNewClues(Integer roleId) {
        // 验证当前是否开启了线索
        Game game = gameService.getCurrentGame();
        if (game.getRound() == null || game.getRound() < 1 || game.getCluesEnable() == null || game.getCluesEnable() < 1) {
            throw new CommonException("当前为开启线索");
        }
        // 获取这一轮已经获取的线索
        List<Clues> clues = cluesMapper.getPrevClues(roleId, game.getRound());

        // 判断最大次数
        if(game.getMaxClues()!=null && game.getMaxClues()>0 &&  clues.size()>=game.getMaxClues()){
            throw new CommonException("本轮线索已获取了最大值");
        }
        // 和上次线索比较是否时间到了
        if (clues != null && !clues.isEmpty()) {
            Clues clue = clues.get(0);
            Date pickTime = clue.getPickTime();
            System.out.println(pickTime);
            long diff = DateUtil.between(pickTime, DateUtil.date(), DateUnit.MINUTE);
            System.out.println(diff);
            if (diff < 5) {
                throw new CommonException("请等待" + (5 - diff) + "分钟后进行获取!");
            }
        }
        // 判断当前是否存在线索 有的话就给出线索
        Clues newClues = cluesMapper.getNewClues(game.getId(), game.getRound());
        if (newClues == null) {
            throw new CommonException("线索已经用用完了，请等待下一轮");
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
    };
}
