package cn.ntshare.laboratory.serivice;

import java.util.List;

public interface PermissionService {
    public List<String> findByRoleId(List<Integer> roleIds);
}
