package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.viewmodel.common.EncryptKV;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 系统配置
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
public class SystemServiceImpl implements SystemService, InitializingBean {

    @Resource(name = "DatabaseRSA")
    private RSA databaseRsa;
    @Resource(name = "pairOneRSA")
    private RSA pairOneRsa;
    @Resource(name = "pairTwoRSA")
    private RSA pairTwoRsa;
    @Value("${system.encrypt.pair-three.aes.key}")
    private String pairThreeKey;
    @Value("${system.name}")
    private String name;
    @Value("${system.paper-status-time-to-live}")
    private Duration paperStatusTimeToLive;
    @Value("#{'${system.security-ignore-urls}'.split('-')}")
    private List<String> securityIgnoreUrls;
    @Value("${system.token-time-to-live}")
    private Duration tokenTimeToLive;
    @Value("${system.resource.stop-word-path}")
    private String stopWordPath;
    @Value("${system.resource.stop-word-enabled}")
    private Boolean stopWordEnabled;
    private static final List<String> stopWordList = new ArrayList<>();
    private static final Logger logger = LoggerFactory.getLogger(SystemServiceImpl.class);

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
    public RestResponse pairTwoObjectEncrypt(Object object) {
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
    public <T> T pairOneObjectDecrypt(EncryptKV encryptKV, Class<T> valueType) {
        String key = pairOneRsa.decryptStr(encryptKV.getKey(), KeyType.PrivateKey);
        AES aes = new AES(key.getBytes());
        String jsonStr = aes.decryptStr(encryptKV.getValue());
        return JsonUtil.toJsonObject(jsonStr, valueType);
    }


    public String getName() {
        return name;
    }

    public Duration getPaperStatusTimeToLive() {
        return paperStatusTimeToLive;
    }

    public List<String> getSecurityIgnoreUrls() {
        return securityIgnoreUrls;
    }

    @Override
    public Duration getTokenTimeToLive() {
        return tokenTimeToLive;
    }

    @Override
    public <T> T aesDecrypt(String encryptV, Class<T> valueType) {
        AES aes = new AES(pairThreeKey.getBytes());
        String jsonStr = aes.decryptStr(encryptV);
        return JsonUtil.toJsonObject(jsonStr, valueType);
    }

    @Override
    public String aesEncrypt(Object object) {
        String jsonStr = JsonUtil.toJsonStr(object);
        AES aes = new AES(pairThreeKey.getBytes());
        return aes.encryptBase64(jsonStr);
    }

    @Override
    public String clearStopWord(String str) {
        StringBuilder stringBuilder = new StringBuilder(str);
        if (str.length() > 0) {
            stopWordList.parallelStream().forEach(item -> {
                if (item.length() > 0 && str.contains(item)) {
                    replaceAll(stringBuilder, item, StringUtils.repeat("*", item.length()));
                }
            });
        }
        return stringBuilder.toString();
    }


    /**
     * 敏感词初始化
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        if(stopWordEnabled != null && stopWordEnabled){
            Files.list(Paths.get(stopWordPath)).map(wordFile -> {
                try {
                    return Files.readAllLines(Paths.get(wordFile.toUri()));
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
                return null;
            }).forEach(stopWordList::addAll);
        }
    }


    /**
     * 文本替换
     *
     * @param builder
     * @param from
     * @param to
     */
    private void replaceAll(StringBuilder builder, String from, String to) {
        int index = builder.indexOf(from);
        while (index != -1) {
            builder.replace(index, index + from.length(), to);
            index += to.length(); // Move to the end of the replacement
            index = builder.indexOf(from, index);
        }
    }
}
