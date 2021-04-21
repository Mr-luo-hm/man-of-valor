package cn.ntshare.laboratory.serivice.impl;

import cn.ntshare.laboratory.model.entity.User;
import cn.ntshare.laboratory.mapper.UserMapper;
import cn.ntshare.laboratory.serivice.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;

    public User findByAccount(String account) {
        return userMapper.findByAccount(account);
    }
}
