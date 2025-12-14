package com.onlineexam.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AIConfig {
    // AI服务提供商：zhipu（智谱AI）或 wenxin（百度文心一言）
    @Value("${ai.provider:zhipu}")
    private String provider;

    // 智谱AI配置
    @Value("${ai.zhipu.api-key:}")
    private String zhipuApiKey;
    
    @Value("${ai.zhipu.api-url:https://open.bigmodel.cn/api/paas/v4/chat/completions}")
    private String zhipuApiUrl;
    
    @Value("${ai.zhipu.model:glm-4}")
    private String zhipuModel;

    // 百度文心一言配置（备用）
    @Value("${ai.wenxin.api-key:}")
    private String wenxinApiKey;

    @Value("${ai.wenxin.secret-key:}")
    private String wenxinSecretKey;

    @Value("${ai.wenxin.api-url:https://aip.baidubce.com/rpc/2.0/ai_custom/v1/wenxinworkshop/chat/completions}")
    private String wenxinApiUrl;

    @Value("${ai.wenxin.token-url:https://aip.baidubce.com/oauth/2.0/token}")
    private String wenxinTokenUrl;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // getter方法
    public String getProvider() { return provider; }
    
    // 智谱AI getter
    public String getZhipuApiKey() { return zhipuApiKey; }
    public String getZhipuApiUrl() { return zhipuApiUrl; }
    public String getZhipuModel() { return zhipuModel; }
    
    // 文心一言 getter
    public String getWenxinApiKey() { return wenxinApiKey; }
    public String getWenxinSecretKey() { return wenxinSecretKey; }
    public String getWenxinApiUrl() { return wenxinApiUrl; }
    public String getWenxinTokenUrl() { return wenxinTokenUrl; }
    
    // 兼容旧代码
    @Deprecated
    public String getApiKey() { 
        return "zhipu".equals(provider) ? zhipuApiKey : wenxinApiKey; 
    }
    
    @Deprecated
    public String getSecretKey() { return wenxinSecretKey; }
    
    @Deprecated
    public String getApiUrl() { 
        return "zhipu".equals(provider) ? zhipuApiUrl : wenxinApiUrl; 
    }
    
    @Deprecated
    public String getTokenUrl() { return wenxinTokenUrl; }
}