package com.larp.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.common.exception.CommonException;
import com.larp.entity.Clues;
import com.larp.entity.Game;
import com.larp.entity.Roles;
import com.larp.mapper.CluesMapper;
import com.larp.mapper.GameMapper;
import com.larp.mapper.RolesMapper;
import com.larp.service.GameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
@Service
public class GameServiceImpl extends ServiceImpl<GameMapper, Game> implements GameService {
    @Autowired
    private Environment env;

    @Resource
    GameMapper gameMapper;

    @Resource
    RolesMapper rolesMapper;

    @Resource
    CluesMapper cluesMapper;

    @Override
    public Game getCurrentGame() {
        Game game = gameMapper.getCurrentGame();
        return game;
    }

    @Override
    public void create(Game game, MultipartFile[] scripts, MultipartFile[] clues) throws IOException {
        // 检查名称是否重复
        QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_name", game.getGameName());
        int count = gameMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new CommonException("名称重复了");
        }
        game.setStatus(0);
        game.setCreateTime(DateUtil.date());
        game.setRound(0);
        game.setCluesEnable(0);
        gameMapper.insert(game);
        // 上传线索和剧本
        String path = ResourceUtils.getURL("classpath:").getPath() + "static";
        String dirPath = path + "/" + game.getGameName();
        FileUtil.mkdir(dirPath + "/剧本");
        FileUtil.mkdir(dirPath + "/线索");
        if (scripts != null) {
            for (MultipartFile file : scripts) {
                String fileName = file.getOriginalFilename();
                fileName = fileName.substring(fileName.indexOf("/"));
                File f = new File(dirPath + "/剧本/" + fileName);
                FileUtil.touch(f);
                file.transferTo(f);
            }
        }
        if (clues != null) {
            for (MultipartFile file : clues) {
                String fileName = file.getOriginalFilename();
                File f = new File(dirPath + "/线索/" + fileName);
                FileUtil.touch(f);
                file.transferTo(f);
            }
        }
    }

    @Override
    public List<Game> getList() {
        return gameMapper.selectList(null);
    }

    @Override
    public void delete(int gameId) {
        gameMapper.deleteById(gameId);
    }

    @Override
    public Object getGameDetail(int gameId) {
        Game game = gameMapper.selectById(gameId);
        List<Roles> roles = rolesMapper.selectByGame(gameId);
        QueryWrapper<Clues> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_id", gameId);
        List<Clues> clues = cluesMapper.selectList(queryWrapper);
        Map<String, Object> result = new HashMap<>();
        result.put("game", game);
        result.put("roles", roles);
        result.put("clues", clues);
        return result;
    }

    @Override
    public void initRoles(Integer gameId) throws IOException {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new CommonException("该游戏不存在");
        }
        List<Roles> roleList = rolesMapper.selectByGame(gameId);
        if (!roleList.isEmpty()) {
            throw new CommonException("角色已存在");
        }
        String path = ResourceUtils.getURL("classpath:").getPath() + "static";
        String dirPath = path + "/" + game.getGameName() + "/剧本";
        File[] files = FileUtil.ls(dirPath);
        if (files.length == 0) {
            throw new CommonException("未上传剧本");
        }

        for (File file : files) {
            if (file.isDirectory()) {
                String roleName = file.getName();
                Roles role = new Roles();
                role.setGameId(gameId);
                role.setRoleName(roleName);
                // todo 以后加上批量
                rolesMapper.insert(role);
            }
        }
    }
}
