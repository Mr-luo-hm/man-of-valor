package cn.ntshare.laboratory.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface PermissionMapper {
    List<String> findByRoleId(@Param("roleIds") List<Integer> roleIds);
}
