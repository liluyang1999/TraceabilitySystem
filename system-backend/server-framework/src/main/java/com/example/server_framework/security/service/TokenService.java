package com.example.server_framework.security.service;

import com.example.common_resource.constant.CacheConstant;
import com.example.common_resource.constant.TermConstant;
import com.example.common_resource.utils.ServletUtils;
import com.example.common_resource.utils.UUIDUtils;
import com.example.common_resource.utils.network.IPUtils;
import com.example.server_framework.cache.RedisCache;
import com.example.server_framework.security.model.LoginUser;
import eu.bitwalker.useragentutils.UserAgent;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Handling Token Verification
 */
@Service
public class TokenService {

    @Resource
    private RedisCache redisCache;

    @Value("${token.header}")
    private String header;

    @Value("${token.secret}")
    private String secret;

    // parse jwt into claims -> get uuid from claims -> get uuid by login_user_uuid
    // -> get loginUser from cache by login_user_{uuid}
    public LoginUser getLoginUser(HttpServletRequest request) {
        String token = getToken(request);
        if (StringUtils.isNotEmpty(token)) {
            Claims claims = parseJwt(token);
            String uuid = (String) claims.get(TermConstant.LOGIN_USER_UUID);
            String userKey = getUserKey(uuid);
            return redisCache.getCacheObject(userKey);
        }
        return null;
    }

    public void delLoginUser(String uuid) {
        if (StringUtils.isNotEmpty(uuid)) {
            String userKey = getUserKey(uuid);
            redisCache.deleteObject(userKey);
        }
    }

    // called in the login
    public String createToken(LoginUser loginUser) {
        String uuid = UUIDUtils.fastUUID();
        loginUser.setUuid(uuid);
        setUserAgent(loginUser);
        // store the loginUser into the cache
        storeToken(loginUser);
        // generate jwt token and return to the client
        Map<String, Object> claims = new HashMap<>();
        claims.put(TermConstant.LOGIN_USER_UUID, uuid);
        return createJwt(claims);
    }

    public void storeToken(LoginUser loginUser) {
        // cache key = token + uuid
        // cache value = loginUser
        String userKey = getUserKey(loginUser.getUuid());
        redisCache.setCacheObject(userKey, loginUser, 72, TimeUnit.HOURS);
    }

    // achieve jwt token from request
    // jwt token contains the claims
    private String getToken(HttpServletRequest request) {
        // Authorization: token
        String token = request.getHeader(header);
        if (StringUtils.isNotEmpty(token) && token.startsWith(TermConstant.TOKEN_PREFIX)) {
            token = token.replace(TermConstant.TOKEN_PREFIX, "");
        }
        return token;
    }

    // redis cache key -> {login_token_uuid}
    private String getUserKey(String uuid) {
        return CacheConstant.LOGIN_USER_KEY + uuid;
    }

    // jwt token contains the login's uuid
    private String createJwt(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    private Claims parseJwt(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    private void setUserAgent(LoginUser loginUser) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IPUtils.getIpAddress(ServletUtils.getRequest());
        loginUser.setLoginIp(ip);
        loginUser.setLoginLocation(IPUtils.getLocationByIP(ip));
        loginUser.setBrowser(userAgent.getBrowser().getName());
        loginUser.setOs(userAgent.getOperatingSystem().getName());
    }
}
