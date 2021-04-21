package cn.ntshare.laboratory.serivice;

import cn.ntshare.laboratory.model.entity.User;

public interface UserService {
    public User findByAccount(String account);
}
