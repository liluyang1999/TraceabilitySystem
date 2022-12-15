package com.example.blockchain_gateway.service;

import com.example.blockchain_gateway.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class RestService {

    private static final String USER_URL = "http://127.0.0.1:9000/server/user";

    @Autowired
    private RestTemplate restTemplate;

    public boolean isUserExist(String username) {
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("username", username);
        AjaxResult ajaxResult = restTemplate.postForObject(USER_URL + "/isUserExisted",
                multiValueMap, AjaxResult.class);
        return ajaxResult != null && ajaxResult.get("code").equals(1);
    }
}
