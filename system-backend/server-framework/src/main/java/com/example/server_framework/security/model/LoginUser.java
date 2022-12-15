package com.example.server_framework.security.model;

import com.example.server_framework.web.rbac.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginUser implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long userId;

    private User user;

    //cache: (key: login_user_uuid, value: loginUser)
    private String uuid;

    private String loginIp;

    private String loginLocation;

    private String browser;

    private String os;

    public LoginUser(User user) {
        this.userId = user.getId();
        this.user = user;
    }
}
