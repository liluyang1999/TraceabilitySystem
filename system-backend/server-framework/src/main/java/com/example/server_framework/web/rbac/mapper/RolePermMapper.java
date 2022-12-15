package com.example.server_framework.web.rbac.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RolePermMapper {

    List<Long> selectPermIdsByRoleId(@Param("roleId") Long roleId);

    int insertRolePerm(@Param("roleId") Long roleId, @Param("permId") Long permId);

    int deleteRolePerm(@Param("roleId") Long roleId, @Param("permId") Long permId);
}
