package com.example.server_interface;

import com.example.common_resource.utils.ServletUtils;
import com.example.server_framework.security.model.LoginUser;
import com.example.server_framework.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/server/logout")
@RestController
public class LogoutController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping("/handleLogout")
    public void handleLogout() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            tokenService.delLoginUser(loginUser.getUuid());
            System.out.println("Logout Success, Username: " + loginUser.getUser().getUsername());
        }
    }
}
