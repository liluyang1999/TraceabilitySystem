package com.example.server_framework.web.rbac.service;

import com.example.common_resource.utils.ServletUtils;
import com.example.server_framework.security.model.LoginUser;
import com.example.server_framework.security.service.TokenService;
import com.example.server_framework.web.rbac.entity.Role;
import com.example.server_framework.web.rbac.entity.User;
import com.example.server_framework.web.rbac.mapper.RoleMapper;
import com.example.server_framework.web.rbac.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

@Service
public class RoleService {

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Autowired
    private TokenService tokenService;

    public List<Role> getAllRoles() {
        return roleMapper.selectRolesAll();
    }

    public Role getRoleByUser(User user) {
        Long roleId = userRoleMapper.selectRoleIdByUserId(user.getId());
        return roleMapper.selectRoleById(roleId);
    }

    public Role getRoleByName(String name) {
        return roleMapper.selectRoleByName(name);
    }

    public boolean addRole(Role role) {
        return roleMapper.insertRole(role) == 1;
    }

    public boolean updateRole(Role role) {
        return roleMapper.updateRole(role) == 1;
    }

    public boolean removeRole(Role role) {
        return roleMapper.deleteRoleById(role.getId()) == 1;
    }

    public boolean hasRole(Role role) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (role == null || loginUser == null || loginUser.getUser() == null) {
            return false;
        }
        Role userRole = getRoleByUser(loginUser.getUser());
        return userRole != null && Objects.equals(userRole.getId(), role.getId());
    }
}
