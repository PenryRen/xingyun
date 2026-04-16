package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.SystemStatus;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.repository.SystemStatusMapper;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.utility.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 系统配置
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@RequiredArgsConstructor
public class SystemServiceImpl implements SystemService {

    @Resource(name = "DatabaseRSA")
    private RSA databaseRsa;
    @Resource(name = "pairOneRSA")
    private RSA pairOneRsa;
    @Resource(name = "pairTwoRSA")
    private RSA pairTwoRsa;
    @Value("#{'${system.security-ignore-urls}'.split('-')}")
    private List<String> securityIgnoreUrls;
    @Value("${system.name}")
    private String name;
    @Value("${system.token-time-to-live}")
    private Duration tokenTimeToLive;
    private final SystemStatusMapper systemStatusMapper;


    @Override
    public boolean authUser(User user, String username, String password) {
        if (user == null) {
            return false;
        }
        String encodePwd = user.getPassword();
        if (null == encodePwd || encodePwd.length() == 0) {
            return false;
        }
        String pwd = pwdDecode(encodePwd);
        return pwd.equals(password);
    }


    @Override
    public String pwdDecode(String encodePwd) {
        return databaseRsa.decryptStr(encodePwd, KeyType.PrivateKey);
    }

    @Override
    public String pwdEncode(String password) {
        return databaseRsa.encryptBase64(password, KeyType.PublicKey);
    }


    @Override
    public String pairOneEncode(String encodeStr) {
        return pairOneRsa.encryptBase64(encodeStr, KeyType.PublicKey);
    }

    @Override
    public String pairOneDecode(String decodeStr) {
        return pairOneRsa.decryptStr(decodeStr, KeyType.PrivateKey);
    }


    @Override
    public RestResponse paperEncrypt(Object object) {
        String jsonStr = JsonUtil.toJsonStr(object);
        String randomKey = RandomUtil.randomString(16);
        AES aes = new AES(randomKey.getBytes());
        String encodeStr = aes.encryptBase64(jsonStr);
        RestResponse restResponse = RestResponse.ok(encodeStr);
        String rasKey = pairTwoRsa.encryptBase64(randomKey, KeyType.PublicKey);
        restResponse.setMessage(rasKey);
        return restResponse;
    }

    @Override
    public List<String> getSecurityIgnoreUrls() {
        return securityIgnoreUrls;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getMysqlVersion() {
        return systemStatusMapper.getVersion();
    }

    @Override
    public List<SystemStatus> getStatus(String key) {
        return systemStatusMapper.getStatus(key);
    }

    @Override
    public Duration getTokenTimeToLive() {
        return tokenTimeToLive;
    }
}
