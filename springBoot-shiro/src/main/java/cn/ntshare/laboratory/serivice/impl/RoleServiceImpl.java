package cn.ntshare.laboratory.serivice.impl;

import cn.ntshare.laboratory.model.entity.Role;
import cn.ntshare.laboratory.mapper.RoleMapper;
import cn.ntshare.laboratory.serivice.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleMapper roleMapper;

    public List<Role> findRoleByUserId(Integer id) {
        return roleMapper.findRoleByUserId(id);
    }
}
