package com.example.server_framework.security.service;

import com.example.common_resource.enums.UserStatus;
import com.example.server_framework.security.model.LoginUser;
import com.example.server_framework.web.rbac.entity.User;
import com.example.server_framework.web.rbac.service.RoleService;
import com.example.server_framework.web.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Login Authentication Service
 */
@Service
public class LoginService {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    public String validateAccount(String username, String password, String deptType) {
        String token = null;
        User user = userService.getUserByUsername(username);
        if (user != null && user.getUsername().equals(username)
                && user.getStatus().equals(UserStatus.ENABLED.getCode())
                && user.getPassword().equals(password)
                && roleService.getRoleByUser(user).getName().equals(deptType)) {
            LoginUser loginUser = new LoginUser(user);
            token = tokenService.createToken(loginUser);
        }
        return token;
    }
}
