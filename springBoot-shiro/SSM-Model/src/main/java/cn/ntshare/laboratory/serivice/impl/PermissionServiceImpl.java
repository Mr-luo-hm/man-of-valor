package cn.ntshare.laboratory.serivice.impl;

import cn.ntshare.laboratory.mapper.PermissionMapper;
import cn.ntshare.laboratory.serivice.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {
    private final PermissionMapper permissionMapper;


    public List<String> findByRoleId(List<Integer> roleIds) {
        return permissionMapper.findByRoleId(roleIds);
    }
}
