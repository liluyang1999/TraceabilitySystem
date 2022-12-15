package com.example.server_interface;

import com.example.server_framework.web.core.AjaxResult;
import com.example.server_framework.web.rbac.entity.Department;
import com.example.server_framework.web.rbac.entity.Role;
import com.example.server_framework.web.rbac.entity.User;
import com.example.server_framework.web.rbac.service.DeptService;
import com.example.server_framework.web.rbac.service.RoleService;
import com.example.server_framework.web.rbac.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/server/mgmt")
@RestController
public class MgmtController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private DeptService deptService;

    @RequestMapping("getAllMemberships")
    public AjaxResult getAllMemberships() {
        List<User> users = userService.getAllUsers();
        return AjaxResult.success("Get all users successfully", users);
    }

    @RequestMapping("/getAllRoles")
    public AjaxResult getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return AjaxResult.success("Get all roles successfully", roles);
    }

    @RequestMapping("/getAllDepts")
    public AjaxResult getAllDepts() {
        List<Department> depts = deptService.getAllDepts();
        return AjaxResult.success("Get all departments successfully", depts);
    }

    @RequestMapping("/registerMembership")
    public AjaxResult registerMembership(@RequestBody User user) {
        System.out.println("Register Membership: " + user);
        if (user.getId() == null && user.getDeptId() != null && user.getStatus() != null
                && !StringUtils.isEmpty(user.getCreator())
                && !StringUtils.isEmpty(user.getUsername())
                && !StringUtils.isEmpty(user.getPassword())
                && !StringUtils.isEmpty(user.getDisplayName())) {
            User userById = userService.getUserById(user.getId());
            User userByUsername = userService.getUserByUsername(user.getUsername());
            if (userById == null && userByUsername == null) {
                Department department = deptService.getDeptById(user.getDeptId());
                if (department != null || user.getDeptId().equals(-1L) || user.getDeptId().equals(-2L)) {
                    Role role;
                    if (department != null) {
                        role = roleService.getRoleByName(department.getType());
                    } else if (user.getDeptId().equals(-1L)) {
                        role = roleService.getRoleByName("consumer");
                    } else {
                        role = roleService.getRoleByName("doctor");
                    }
                    if (userService.addUser(user, role)) {
                        return AjaxResult.success("Register membership successfully");
                    }
                }
            }
        }
        return AjaxResult.error("Failed to register membership, please check the input");
    }

    @RequestMapping("/updateMembership")
    public AjaxResult updateMembership(@RequestBody User user) {
        System.out.println("Update membership: " + user);
        if (user.getId() != null && user.getDeptId() != null && user.getStatus() != null
                && !StringUtils.isEmpty(user.getUpdater())
                && !StringUtils.isEmpty(user.getUsername())
                && !StringUtils.isEmpty(user.getPassword())
                && !StringUtils.isEmpty(user.getDisplayName())) {
            User userById = userService.getUserById(user.getId());
            User userByUsername = userService.getUserByUsername(user.getUsername());
            if (userById != null && userByUsername != null
                    && userService.updateUser(user)) {
                return AjaxResult.success("Update membership successfully");
            }
        }
        return AjaxResult.error("Failed to update membership, please check the input");
    }

    @RequestMapping("/registerDept")
    public AjaxResult registerDept(@RequestBody Department dept) {
        System.out.println("Register Department: " + dept);
        if (dept.getId() == null
                && !StringUtils.isEmpty(dept.getCreator())
                && !StringUtils.isEmpty(dept.getName())
                && !StringUtils.isEmpty(dept.getType())
                && !StringUtils.isEmpty(dept.getLeader())
                && !StringUtils.isEmpty(dept.getEmail())
                && !StringUtils.isEmpty(dept.getAddress())) {
            Department department = deptService.getDeptById(dept.getId());
            if (department == null && deptService.addDept(dept)) {
                return AjaxResult.success("Register department successfully");
            }
        }
        return AjaxResult.error("Failed to register department, please check the input");
    }

    @RequestMapping("/updateDept")
    public AjaxResult updateDept(@RequestBody Department dept) {
        System.out.println("Update Department: " + dept);
        if (dept.getId() != null
                && !StringUtils.isEmpty(dept.getUpdater())
                && !StringUtils.isEmpty(dept.getName())
                && !StringUtils.isEmpty(dept.getLeader())
                && !StringUtils.isEmpty(dept.getEmail())
                && !StringUtils.isEmpty(dept.getAddress())) {
            Department department = deptService.getDeptById(dept.getId());
            if (department != null && deptService.updateDept(dept)) {
                return AjaxResult.success("Update department successfully");
            }
        }
        return AjaxResult.error("Failed to update department, please check the input");
    }

    @RequestMapping("/cancelMembership")
    public AjaxResult cancelMembership(@RequestParam("userId") Long userId) {
        System.out.println("Cancel membership: " + userId);
        if (userId != null) {
            User user = userService.getUserById(userId);
            if (user != null && userService.removeUser(user)) {
                return AjaxResult.success("Cancel membership successfully");
            }
        }
        return AjaxResult.error("Failed to cancel membership, please check the input");
    }

    @RequestMapping("/cancelDept")
    public AjaxResult cancelDept(@RequestParam("deptId") Long deptId, @RequestParam("deptName") String deptName) {
        System.out.println("Cancel Department: " + deptId);
        if (deptId != null && deptName != null) {
            Department dept = deptService.getDeptById(deptId);
            List<User> users = userService.getUsersByDept(deptId, deptName);
            if (dept != null && users == null
                    && deptService.removeDept(dept)) {
                return AjaxResult.success("Cancel department successfully");
            }
        }
        return AjaxResult.error("Failed to cancel department, please check the input");
    }
}
