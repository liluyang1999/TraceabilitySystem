package com.example.server_framework.web.rbac.mapper;

import com.example.server_framework.web.rbac.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Dao: User
 */
@Mapper
public interface UserMapper {

    User selectUserById(@Param("id") Long id);

    User selectUserByUsername(@Param("username") String username);

    List<User> selectUsersByDept(@Param("deptId") Long deptId, @Param("deptName") String deptName);

    List<User> selectUsersAll();

    List<User> selectEnabledUsers();

    List<User> selectDisabledUsers();

    List<User> selectDeletedUsers();

    int insertUser(User user);

    int updateUser(User user);

    int deleteUserById(@Param("userId") Long userId);

    int updatePasswordByUsername(@Param("username") String username, @Param("password") String password);

    int updateStatusByUsername(@Param("username") String username, @Param("status") Integer status);
}
