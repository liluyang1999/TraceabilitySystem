package com.example.server_framework.web.rbac.service;

import com.example.server_framework.web.rbac.entity.Role;
import com.example.server_framework.web.rbac.entity.User;
import com.example.server_framework.web.rbac.mapper.UserMapper;
import com.example.server_framework.web.rbac.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    public List<User> getAllUsers() {
        return userMapper.selectUsersAll();
    }

    public User getUserById(Long id) {
        return userMapper.selectUserById(id);
    }

    public User getUserByUsername(String username) {
        return userMapper.selectUserByUsername(username);
    }

    public List<User> getUsersByDept(Long deptId, String deptName) {
        return userMapper.selectUsersByDept(deptId, deptName);
    }

    public List<User> getUsersByRole(Role role) {
        List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(role.getId());
        List<User> users = new ArrayList<>();
        for (Long id: userIds) {
            users.add(userMapper.selectUserById(id));
        }
        return users;
    }

    public boolean addUser(User user, Role role) {
        if (userMapper.insertUser(user) == 1) {
            Long userId = userMapper.selectUserByUsername(user.getUsername()).getId();
            Long roleId = role.getId();
            return userRoleMapper.insertUserRole(userId, roleId) == 1;
        }
        return false;
    }

    public boolean updateUser(User user) {
        return userMapper.updateUser(user) == 1;
    }

    public boolean removeUser(User user) {
        return userMapper.deleteUserById(user.getId()) == 1;
    }

    public boolean updateUserPassword(String username, String password) {
        return userMapper.updatePasswordByUsername(username, password) == 1;
    }
}

