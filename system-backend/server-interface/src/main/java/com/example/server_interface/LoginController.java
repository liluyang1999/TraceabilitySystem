package com.example.server_interface;

import com.example.common_resource.constant.CacheConstant;
import com.example.common_resource.constant.TermConstant;
import com.example.common_resource.utils.ServletUtils;
import com.example.server_framework.cache.RedisCache;
import com.example.server_framework.security.model.LoginBody;
import com.example.server_framework.security.model.LoginUser;
import com.example.server_framework.security.service.LoginService;
import com.example.server_framework.security.service.TokenService;
import com.example.server_framework.server.ServerInfo;
import com.example.server_framework.web.core.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/server/login")
@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisCache redisCache;

    @RequestMapping("/authenticate")
    public AjaxResult authenticate(@RequestBody LoginBody loginBody) {
        String verCode = loginBody.getVerCode();
        String cacheKey = CacheConstant.CAPTCHA_CODE_KEY;
        String captcha = redisCache.getCacheObject(cacheKey);
        if (captcha != null && captcha.equals(verCode)) {
            String username = loginBody.getUsername();
            String password = loginBody.getPassword();
            String deptType = loginBody.getDeptType();

            String token = loginService.validateAccount(username, password, deptType);
            if (token != null) {
                System.out.println("Login Success, Username: " + username);
                AjaxResult ajax = AjaxResult.success("Login Success");
                ajax.put(TermConstant.TOKEN, token);
                return ajax;
            } else {
                return AjaxResult.error("The account is incorrect or disabled");
            }
        } else {
            return AjaxResult.error("The captcha is incorrect or expired");
        }
    }

    @RequestMapping("/getLoginInfo")
    public AjaxResult getLoginUser() {
        LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        if (loginUser != null) {
            return AjaxResult.success("Get login user successfully", loginUser);
        } else {
            return AjaxResult.error("The token is incorrect or expired");
        }
    }

    @RequestMapping("/getServerInfo")
    public AjaxResult getServerInfo() {
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.load();
        AjaxResult ajaxResult = AjaxResult.success("Get server info successfully");
        ajaxResult.put("cpu", serverInfo.getCpu());
        ajaxResult.put("mem", serverInfo.getMem());
        ajaxResult.put("jvm", serverInfo.getJvm());
        ajaxResult.put("sys", serverInfo.getSys());
        ajaxResult.put("sysFiles", serverInfo.getSysFiles());
        ajaxResult.put("pathDomain", serverInfo.getPathDomain());
        return ajaxResult;
    }
}
