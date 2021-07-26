package com.larp.service.impl;

import com.larp.entity.Users;
import com.larp.mapper.UsersMapper;
import com.larp.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hippo
 * @since 2021-07-22
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
