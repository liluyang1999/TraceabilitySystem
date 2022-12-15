package com.example.server_framework.web.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao: Relationship between User and Role
 */
@Mapper
public interface UserRoleMapper {

    Long selectRoleIdByUserId(@Param("userId") Long userId);

    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    int insertUserRole(@Param("userId") Long userId, @Param("roleId") Long roleId);

    int deleteUserRole(@Param("userId") Long userId);
}
