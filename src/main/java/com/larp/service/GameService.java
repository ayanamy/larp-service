package com.larp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.larp.entity.Game;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hippo
 * @since 2021-07-20
 */
public interface GameService extends IService<Game> {
    Game  getCurrentGame();

    void create(Game game, MultipartFile[] scripts, MultipartFile[] clues) throws IOException;

    List<Game> getList();

    void delete(int gameId);

    Object getGameDetail(int gameId);

    void initGame(Integer gameId) throws IOException;
}
