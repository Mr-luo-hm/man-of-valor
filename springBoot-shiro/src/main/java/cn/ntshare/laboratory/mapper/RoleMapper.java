package cn.ntshare.laboratory.mapper;

import cn.ntshare.laboratory.model.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface RoleMapper {
    List<Role> findRoleByUserId(@Param("userId") Integer userId);
}
