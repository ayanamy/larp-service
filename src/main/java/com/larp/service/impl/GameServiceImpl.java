package com.larp.service.impl;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.common.exception.CommonException;
import com.larp.entity.Clues;
import com.larp.entity.Game;
import com.larp.entity.Roles;
import com.larp.entity.Scripts;
import com.larp.mapper.CluesMapper;
import com.larp.mapper.GameMapper;
import com.larp.mapper.RolesMapper;
import com.larp.mapper.ScriptsMapper;
import com.larp.service.GameService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.Value;
import org.apache.tomcat.util.digester.Rules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;
import java.util.stream.Collectors;

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

    @Resource
    ScriptsMapper scriptsMapper;

    @Override
    public Game getCurrentGame() {
        Game game = gameMapper.getCurrentGame();
        return game;
    }

    @Transactional
    @Override
    public void create(Game game, MultipartFile[] scripts, MultipartFile[] clues, MultipartFile[] handbook) throws IOException {
        // 检查名称是否重复
        try {
            QueryWrapper<Game> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("game_name", game.getGameName());
            int count = gameMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new CommonException("名称重复了");
            }
            game.setStatus(0);
            game.setCreateTime(DateUtil.date());
            game.setRound(-1); // -1 还未分配角色 0是分配好角色 发放初始信息
            game.setCluesEnable(0);
            // 上传线索和剧本
            String path = ResourceUtils.getURL("classpath:").getPath() + "static";
            String dirPath = path + "/" + game.getGameName();
            FileUtil.mkdir(dirPath + "/剧本");
            FileUtil.mkdir(dirPath + "/线索");
            FileUtil.mkdir(dirPath + "/组织者手册");
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
                    fileName = fileName.substring(fileName.indexOf("/"));
                    File f = new File(dirPath + "/线索/" + fileName);
                    FileUtil.touch(f);
                    file.transferTo(f);
                }
            }
            if (handbook != null) {
                for (MultipartFile file : handbook) {
                    String fileName = file.getOriginalFilename();
                    fileName = fileName.substring(fileName.indexOf("/"));
                    File f = new File(dirPath + "/组织者手册/" + fileName);
                    FileUtil.touch(f);
                    file.transferTo(f);
                }
            }
            gameMapper.insert(game);
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
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
    public void initGame(Integer gameId) throws IOException {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new CommonException("该游戏不存在");
        }
        List<Roles> roleList = rolesMapper.selectByGame(gameId);
        if (!roleList.isEmpty()) {
            throw new CommonException("角色已存在");
        }
        String path = ResourceUtils.getURL("classpath:").getPath() + "static";
        // 初始化角色和剧情
        String roleDirPath = path + "/" + game.getGameName() + "/剧本";
        //todo 这里可以直接用FileUtil.loopFiles 遍历获取所有文件和文件夹

        File[] roleFolds = FileUtil.ls(roleDirPath);
        if (roleFolds.length == 0) {
            throw new CommonException("未上传剧本");
        }
        // 第一层是角色的名称
        // 第二层是轮次
        // 第三层是角色的剧本
        int roundMax = 0;
        for (File fold : roleFolds) {
            if (fold.isDirectory()) {
                String roleName = fold.getName();
                Roles role = new Roles();
                role.setGameId(gameId);
                role.setRoleName(roleName);
                // todo 以后加上批量
                rolesMapper.insert(role);
                File[] roundFolds = FileUtil.ls(fold.getPath());
                roundMax = Math.max(roundMax, roundFolds.length);
                for (File roundFold : roundFolds) {
                    if (roundFold.isDirectory()) {
                        String foldName = roundFold.getName();
                        int round = Convert.toInt(foldName, 1);
                        File[] files = FileUtil.ls(roundFold.getPath());
                        for (File file : files) {
                            if (file.isFile()) {
                                String fileName = file.getName();
                                fileName = fileName.substring(0, fileName.indexOf("."));
                                int orderNo = Convert.toInt(fileName, 0);
                                Scripts scripts = new Scripts();
                                scripts.setGameId(gameId);
                                scripts.setRoleId(role.getId());
                                String content = StrUtil.format("{}/剧本/{}/{}/{}", game.getGameName(), roleName, round, file.getName());
                                scripts.setContent(content);
                                scripts.setOrderNo(orderNo);
                                scripts.setRound(round);
                                scriptsMapper.insert(scripts);
                            }
                        }
                    }
                }
            }
        }
        game.setRoundTotal(roundMax);

        // 初始化线索
        // 线索有三层
        // 第一层 文件夹 1 2 代表着第几轮的线索
        // 第二层 文件夹 地点一 地点二 代表着线索来自哪里
        // 第三层 图片   具体的线索
        String cluesDirPath = path + "/" + game.getGameName() + "/线索";
        File[] cluesFolds = FileUtil.ls(cluesDirPath);
        if (cluesFolds.length == 0) {
            throw new CommonException("未上传线索");
        }
        for (File fold : cluesFolds) {
            if (fold.isDirectory()) {
                String foldName = fold.getName();
                int round = Convert.toInt(foldName, -1);
                if (round > 0) {
                    File[] folds2 = FileUtil.ls(fold.getPath());
                    for (File fold2 : folds2) {
                        if (fold.isDirectory()) {
                            String location = fold2.getName();
                            File[] clues = FileUtil.ls(fold2.getPath());
                            for (File clueFile : clues) {
                                if (clueFile.isFile()) {
                                    String fileName = clueFile.getName();
                                    fileName = fileName.substring(0, fileName.indexOf("."));
                                    Clues clue = new Clues();
                                    clue.setGameId(gameId);
                                    clue.setCode(fileName);
                                    clue.setLocation(location);
                                    clue.setRound(round);
                                    clue.setStatus(0);
                                    clue.setClueType("normal");
                                    String images = StrUtil.format("{}/线索/{}/{}/{}", game.getGameName(), round, location, clueFile.getName());
                                    clue.setImages(images);
                                    cluesMapper.insert(clue);
                                }
                            }
                        } else if (fold.isFile()) {
                            String fileName = fold.getName();
                            fileName = fileName.substring(0, fileName.indexOf("."));
                            Clues clue = new Clues();
                            clue.setGameId(gameId);
                            clue.setCode(fileName);
                            clue.setLocation("");
                            clue.setRound(round);
                            clue.setStatus(0);
                            clue.setClueType("special");
                            String images = StrUtil.format("{}/线索/{}/{}", game.getGameName(), round, fileName);
                            clue.setImages(images);
                            cluesMapper.insert(clue);
                        }
                    }

                }
            }
        }
        gameMapper.updateById(game);
    }

    @Override
    public Roles initMyRole(Integer gameId, String user) {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new CommonException("游戏不存在");
        }
        if (game.getStatus() != 1 || game.getRound() != -1) {
            throw new CommonException("数据异常，请刷新");
        }
        QueryWrapper<Roles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("game_id", gameId).eq("user", user);
        Roles role = rolesMapper.selectOne(queryWrapper);
        if (role != null) {
            throw new CommonException("您已获取了角色");
        }

        QueryWrapper<Roles> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("game_id", gameId).and(wrapper -> wrapper.isNull("user").or().eq("user", ""));
        List<Roles> rolesList = rolesMapper.selectList(queryWrapper2);
        if (rolesList.size() == 0) {
            throw new CommonException("已经没有剩余的角色了");
        }
        Random rand = new Random();
        int temp = rand.nextInt(rolesList.size());
        role = rolesList.get(temp);
        role.setUser(user);
        rolesMapper.updateById(role);
        return role;

    }

    @Override
    public void setNextRound(Integer gameId) {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new CommonException("游戏不存在");
        }
        if (game.getRound() > game.getRoundTotal()) {
            throw new CommonException("已经没有下一回合了");
        }
        int round = Convert.toInt(game.getRound(), -1);
        game.setRound(round + 1);
        gameMapper.updateById(game);
    }

    @Override
    public List<String> getHandbook(Integer gameId) throws FileNotFoundException {
        Game game = gameMapper.selectById(gameId);
        if (game == null) {
            throw new CommonException("游戏不存在");
        }
        String path = ResourceUtils.getURL("classpath:").getPath() + "static";
        String dirPath = path + "/" + game.getGameName() + "/组织者手册";
        File[] files = FileUtil.ls((dirPath));
        List<String> handbooks = new ArrayList<String>();
        for (File file : files) {
            if (file.isFile()) {
                handbooks.add(game.getGameName() + "/组织者手册/" + file.getName());
            }
        }
        return handbooks.stream().sorted(Comparator.comparing(String::toString)).collect(Collectors.toList());
    }
}
