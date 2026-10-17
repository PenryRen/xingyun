package com.mindskip.wdd.configuration.utility;

import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @version 1.7.0
 * @description: 密钥配置
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Configuration
public class EncryptConfig {


    @Value("${system.encrypt.database.rsa.public-key}")
    private String databasePublicKey;
    @Value("${system.encrypt.database.rsa.private-key}")
    private String databasePrivateKey;


    @Value("${system.encrypt.pair-one.rsa.public-key}")
    private String pairOnePublicKey;
    @Value("${system.encrypt.pair-one.rsa.private-key}")
    private String pairOnePrivateKey;


    @Value("${system.encrypt.pair-two.rsa.public-key}")
    private String pairTwoPublicKey;
    @Value("${system.encrypt.pair-two.rsa.private-key}")
    private String pairTwoPrivateKey;


    /**
     * 密码公钥私钥
     *
     * @return the rsa
     */
    @Bean("DatabaseRSA")
    public RSA databaseRSA() {
        return new RSA(databasePrivateKey, databasePublicKey);
    }

    /**
     * pair-one 公钥私钥
     *
     * @return the rsa
     */
    @Bean("pairOneRSA")
    public RSA pairOneRSA() {
        return new RSA(pairOnePrivateKey, pairOnePublicKey);
    }


    /**
     * pair-two公钥私钥
     *
     * @return the rsa
     */
    @Bean("pairTwoRSA")
    public RSA pairTwoRSA() {
        return new RSA(pairTwoPrivateKey, pairTwoPublicKey);
    }


}
