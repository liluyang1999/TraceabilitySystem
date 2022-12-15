package com.example.server_framework.web.rbac.entity;

import com.example.server_framework.web.core.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serial;

/**
 * RBAC Model: User
 * Table: ss_user
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long deptId;

    private String username;

    private String password;

    private String displayName;

    private String email;

    private String phone;

    // 0: account enabled
    // 1: account disabled
    private Integer status;

    public User(Long id, String username, String password, Integer status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.status = status;
    }

    @Size(max = 30, message = "The length of username should be within 20 characters")
    @NotBlank(message = "The user's account shouldn't be blank")
    public String getUsername() {
        return this.username;
    }

    @Size(max = 30, message = "The length of nickname should be within 30 characters")
    @NotBlank(message = "The display name shouldn't be blank")
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayName='" + displayName + '\'' +
                ", deptId=" + deptId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                '}';
    }
}
