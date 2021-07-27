package com.larp.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.larp.entity.Clues;
import com.larp.entity.Scripts;
import com.larp.mapper.ScriptsMapper;
import com.larp.service.ScriptsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ScriptsServiceImpl extends ServiceImpl<ScriptsMapper, Scripts> implements ScriptsService {

    @Resource
    private ScriptsMapper scriptsMapper;

    @Override
    public List<Scripts> getScripts(int roleId,int round) {
        QueryWrapper<Scripts> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).le("round",round);
        List<Scripts> scripts = scriptsMapper.selectList(queryWrapper);
        return scripts;
    }
}
