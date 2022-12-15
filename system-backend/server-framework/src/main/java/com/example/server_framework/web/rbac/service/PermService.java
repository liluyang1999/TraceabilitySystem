package com.example.server_framework.web.rbac.service;

import com.example.server_framework.web.rbac.entity.Permission;
import com.example.server_framework.web.rbac.entity.Role;
import com.example.server_framework.web.rbac.entity.User;
import com.example.server_framework.web.rbac.mapper.PermMapper;
import com.example.server_framework.web.rbac.mapper.RolePermMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermService {

    @Resource
    private RoleService roleService;

    @Resource
    private RolePermMapper rolePermMapper;

    @Resource
    private PermMapper permMapper;

    public List<Permission> getPermsByUser(User user) {
        Role role = roleService.getRoleByUser(user);
        List<Long> permIds = rolePermMapper.selectPermIdsByRoleId(role.getId());
        List<Permission> permissions = new ArrayList<>();
        for (Long id : permIds) {
            permissions.add(permMapper.selectPermById(id));
        }
        return permissions;
    }
}


