package com.example.server_framework.web.rbac.mapper;

import com.example.server_framework.web.rbac.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermMapper {

    Permission selectPermById(@Param("id") Long id);

    Permission selectPermByName(@Param("name") String name);

    List<Permission> selectPermsAll();

    int insertPerm(Permission perm);

    int updatePerm(Permission perm);

    int deletePermById(@Param("id") Long id);
}
