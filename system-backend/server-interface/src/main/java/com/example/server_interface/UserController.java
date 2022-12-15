package com.example.server_interface;

import com.example.common_resource.utils.ServletUtils;
import com.example.server_framework.security.model.LoginUser;
import com.example.server_framework.security.service.TokenService;
import com.example.server_framework.web.core.AjaxResult;
import com.example.server_framework.web.rbac.entity.Department;
import com.example.server_framework.web.rbac.entity.Permission;
import com.example.server_framework.web.rbac.entity.Role;
import com.example.server_framework.web.rbac.entity.User;
import com.example.server_framework.web.rbac.service.DeptService;
import com.example.server_framework.web.rbac.service.PermService;
import com.example.server_framework.web.rbac.service.RoleService;
import com.example.server_framework.web.rbac.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/server/user")
@RestController
public class UserController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PermService permService;

    @Autowired
    private DeptService deptService;

    @RequestMapping("/getUserInfo")
    public AjaxResult getUserInfo() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            User user = userService.getUserById(loginUser.getUserId());
            Role role = roleService.getRoleByUser(user);
            List<Permission> perms = permService.getPermsByUser(user);

            Department dept = null;
            if (!user.getDeptId().equals(-1L) && !user.getDeptId().equals(-2L)) {
                dept = deptService.getDeptById(user.getDeptId());
            }

            AjaxResult ajax = AjaxResult.success("Get user info successfully");
            ajax.put("user", user);
            ajax.put("role", role);
            ajax.put("perms", perms);
            ajax.put("dept", dept);
            return ajax;
        } else {
            return AjaxResult.error(3,"The token is incorrect");
        }
    }

    @RequestMapping("/updateUserInfo")
    public AjaxResult updateInfo(@RequestParam String username,
                                 @RequestParam String displayName,
                                 @RequestParam String email,
                                 @RequestParam String phone) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            User user = userService.getUserByUsername(username);
            if (user != null) {
                user.setDisplayName(displayName);
                user.setEmail(email);
                user.setPhone(phone);
                user.setUpdater(username);
                if (userService.updateUser(user)) {
                    return AjaxResult.success("Update information successfully");
                }
            }
            return AjaxResult.error("Failed to update information");
        } else {
            return AjaxResult.error(3, "The token is incorrect");
        }
    }

    @RequestMapping("/updateUserPassword")
    public AjaxResult updatePassword(@RequestParam String username,
                                     @RequestParam String oldPassword,
                                     @RequestParam String newPassword) {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            User user = userService.getUserByUsername(username);
            if (user != null && user.getPassword().equals(oldPassword)
                    && userService.updateUserPassword(username, newPassword)) {
                return AjaxResult.success("Update password successfully");
            }
            return AjaxResult.error("Failed to update password");
        } else {
            return AjaxResult.error(3, "The token is incorrect");
        }
    }

    @RequestMapping("/getUsersByRoleName")
    public AjaxResult getUsersByRoleName(@RequestParam String roleName) {
        Role role = roleService.getRoleByName(roleName);
        if (role != null) {
            List<User> users = userService.getUsersByRole(role);
            if (!users.isEmpty()) {
                return AjaxResult.success("Get users by role name successfully", users);
            }
        }
        return AjaxResult.error("No memberships can be found with this role name");
    }

    @RequestMapping("/isUserExisted")
    public AjaxResult checkUser(@RequestParam String username) {
        System.out.println("Check isUserExisted: " + username);
        User user = userService.getUserByUsername(username);
        if (user != null) {
            return AjaxResult.success();
        } else {
            return AjaxResult.error();
        }
    }
}
