package com.mindskip.wdd.utility;

import com.alibaba.fastjson.JSONObject;
import com.mindskip.wdd.domain.DeleteWare;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

import static com.mindskip.wdd.domain.enums.ueit.HttpEnum.Authorization;

public class HttpUtils {

    private static RestTemplate restTemplate = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

    public static ResponseEntity<String> sendPut(String url, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization",Authorization);
        HttpEntity httpEntity = new HttpEntity(param, headers);
        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.PUT, httpEntity, String.class);

        return res;
    }

    /**
     * Post
     */
    public static ResponseEntity<String> sendPost(String url, String param) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization",Authorization);
        HttpEntity httpEntity = new HttpEntity(param, headers);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);

        return res;
    }

    public static ResponseEntity<String> doDelete(String url, String param) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("Authorization",Authorization);
        HttpEntity httpEntity = new HttpEntity(param, headers);

        ResponseEntity<String> res = restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);

        return res;
    }

}