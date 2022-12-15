package com.example.server_framework.web.rbac.mapper;

import com.example.server_framework.web.rbac.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoleMapper {

    Role selectRoleById(@Param("id") Long id);

    Role selectRoleByName(@Param("name") String name);

    List<Role> selectRolesAll();

    List<Role> selectEnabledRoles();

    List<Role> selectDisabledRoles();

    List<Role> selectDeletedRoles();

    int insertRole(Role role);

    int updateRole(Role role);

    int deleteRoleById(@Param("id") Long id);
}
