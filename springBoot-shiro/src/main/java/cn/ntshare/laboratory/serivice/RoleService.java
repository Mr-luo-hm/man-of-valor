package cn.ntshare.laboratory.serivice;

import cn.ntshare.laboratory.model.entity.Role;

import java.util.List;

public interface RoleService {
    public List<Role> findRoleByUserId(Integer id);
}
