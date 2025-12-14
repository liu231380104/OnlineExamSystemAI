package com.onlineexam.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.onlineexam.config.AIConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class AIUtil {
    @Autowired
    private AIConfig aiConfig;

    @Autowired
    private RestTemplate restTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private String accessToken;
    private long tokenExpireTime = 0; // 令牌过期时间（毫秒）

    /**
     * 调用AI生成题目（支持智谱AI和百度文心一言）
     */
    public String generateQuestions(String prompt) throws Exception {
        String provider = aiConfig.getProvider();
        
        if ("zhipu".equals(provider)) {
            return generateQuestionsWithZhipu(prompt);
        } else {
            return generateQuestionsWithWenxin(prompt);
        }
    }
    
    /**
     * 调用智谱AI生成题目
     */
    private String generateQuestionsWithZhipu(String prompt) throws Exception {
        String apiKey = aiConfig.getZhipuApiKey();
        if (apiKey == null || apiKey.isEmpty()) {
            throw new RuntimeException("智谱AI API密钥未配置，请在application.properties中设置ai.zhipu.api-key");
        }

        // 构建请求参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey); // 智谱AI使用Bearer Token认证

        // 构建消息列表
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", aiConfig.getZhipuModel()); // 指定模型，如glm-4
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7); // 生成随机性

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    aiConfig.getZhipuApiUrl(),
                    request,
                    String.class
            );

            // 解析返回结果
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());

                // 智谱AI返回格式：{"choices": [{"message": {"content": "..."}}]}
                if (root.has("choices") && root.get("choices").size() > 0) {
                    JsonNode choices = root.get("choices");
                    if (choices.get(0).has("message") && choices.get(0).get("message").has("content")) {
                        return choices.get(0).get("message").get("content").asText();
                    }
                } else if (root.has("error")) {
                    JsonNode error = root.get("error");
                    String errorMsg = error.has("message") ? error.get("message").asText() : "未知错误";
                    throw new RuntimeException("智谱AI返回错误: " + errorMsg);
                }

                throw new RuntimeException("无法解析智谱AI返回: " + response.getBody());
            } else {
                throw new RuntimeException("智谱AI接口调用失败: " + response.getStatusCode() + ", 响应: " + response.getBody());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            try {
                JsonNode errorNode = objectMapper.readTree(errorBody);
                if (errorNode.has("error") && errorNode.get("error").has("message")) {
                    throw new RuntimeException("智谱AI API错误: " + errorNode.get("error").get("message").asText());
                }
            } catch (Exception parseEx) {
                // 如果无法解析错误响应，返回原始错误信息
            }
            throw new RuntimeException("智谱AI API调用失败: " + e.getStatusCode() + " - " + errorBody);
        } catch (org.springframework.web.client.RestClientException e) {
            throw new RuntimeException("网络请求失败: " + e.getMessage(), e);
        }
    }
    
    /**
     * 调用百度文心一言生成题目（备用）
     */
    private String generateQuestionsWithWenxin(String prompt) throws Exception {
        // 1. 获取有效的access_token
        String token = getAccessToken();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("获取access_token失败，请检查API密钥配置");
        }

        // 2. 构建请求参数
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 构建消息列表
        List<Map<String, String>> messages = new ArrayList<>();
        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", prompt);
        messages.add(message);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("temperature", 0.7); // 生成随机性

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // 3. 调用文心一言接口（URL需拼接access_token）
        String apiUrl = aiConfig.getWenxinApiUrl() + "?access_token=" + token;
        
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl,
                    request,
                    String.class
            );

            // 4. 解析返回结果
            if (response.getStatusCode() == HttpStatus.OK) {
                JsonNode root = objectMapper.readTree(response.getBody());

                // 文心一言返回结构可能有多种，尝试不同字段
                if (root.has("result")) {
                    return root.get("result").asText();
                } else if (root.has("choices") && root.get("choices").size() > 0) {
                    JsonNode choices = root.get("choices");
                    if (choices.get(0).has("message")) {
                        return choices.get(0).get("message").get("content").asText();
                    } else if (choices.get(0).has("text")) {
                        return choices.get(0).get("text").asText();
                    }
                } else if (root.has("error_msg")) {
                    String errorMsg = root.get("error_msg").asText();
                    if (errorMsg.contains("No permission") || errorMsg.contains("permission")) {
                        throw new RuntimeException("文心一言权限错误: " + errorMsg + 
                            "。请检查：1. API密钥是否已开通文心一言服务；2. 是否在百度智能云控制台中开通了对应模型；3. 账户余额是否充足");
                    }
                    throw new RuntimeException("文心一言返回异常: " + errorMsg);
                } else if (root.has("error_code")) {
                    String errorCode = root.get("error_code").asText();
                    String errorMsg = root.has("error_msg") ? root.get("error_msg").asText() : "未知错误";
                    throw new RuntimeException("文心一言API错误 [代码:" + errorCode + "]: " + errorMsg);
                }

                throw new RuntimeException("无法解析文心一言返回: " + response.getBody());
            } else {
                throw new RuntimeException("接口调用失败: " + response.getStatusCode() + ", 响应: " + response.getBody());
            }
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            String errorBody = e.getResponseBodyAsString();
            try {
                JsonNode errorNode = objectMapper.readTree(errorBody);
                if (errorNode.has("error_msg")) {
                    throw new RuntimeException("文心一言API错误: " + errorNode.get("error_msg").asText());
                } else if (errorNode.has("error_description")) {
                    throw new RuntimeException("文心一言API错误: " + errorNode.get("error_description").asText());
                }
            } catch (Exception parseEx) {
                // 如果无法解析错误响应，返回原始错误信息
            }
            throw new RuntimeException("文心一言API调用失败: " + e.getStatusCode() + " - " + errorBody);
        } catch (org.springframework.web.client.RestClientException e) {
            throw new RuntimeException("网络请求失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取文心一言的access_token（有效期30天，这里简单处理为过期则重新获取）
     */
    private String getAccessToken() throws Exception {
        // 检查令牌是否有效
        if (System.currentTimeMillis() < tokenExpireTime && accessToken != null) {
            return accessToken;
        }

        // 构建获取token的请求
        String url = UriComponentsBuilder.fromHttpUrl(aiConfig.getWenxinTokenUrl())
                .queryParam("grant_type", "client_credentials")
                .queryParam("client_id", aiConfig.getWenxinApiKey())
                .queryParam("client_secret", aiConfig.getWenxinSecretKey())
                .toUriString();

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            JsonNode root = objectMapper.readTree(response.getBody());
            if (root.has("access_token")) {
                accessToken = root.get("access_token").asText();
                int expiresIn = root.get("expires_in").asInt(); // 有效期（秒）
                tokenExpireTime = System.currentTimeMillis() + (expiresIn - 3600) * 1000; // 提前1小时过期
                return accessToken;
            } else if (root.has("error")) {
                throw new RuntimeException("获取access_token失败: " + root.get("error_description").asText());
            } else {
                throw new RuntimeException("获取access_token失败: 未知响应格式");
            }
        } else {
            throw new RuntimeException("获取access_token失败: " + response.getStatusCode() + ", 响应: " + response.getBody());
        }
    }

    /**
     * 强制刷新access_token（例如令牌失效时）
     */
    public void refreshAccessToken() {
        this.accessToken = null;
        this.tokenExpireTime = 0;
    }
}