package org.example.userservice.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 微信服务类
 * 用于调用微信 API
 * 
 * @author user-service
 */
@Service
public class WxService {

    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Value("${wx.appid:}")
    private String appId;

    @Value("${wx.secret:}")
    private String secret;

    @Value("${wx.test-mode:false}")
    private Boolean testMode;

    private final RestTemplate restTemplate = new RestTemplate();

    /**
     * 通过 code 获取 openid 和 session_key
     * 
     * @param code 微信小程序登录 code（测试模式下可以是 "test_openid_xxx" 格式）
     * @return 包含 openid 和 session_key 的 Map
     */
    public Map<String, String> code2Session(String code) {
        // 测试模式：如果 code 以 "test_" 开头，直接返回模拟数据
        if (Boolean.TRUE.equals(testMode) && code != null && code.startsWith("test_")) {
            Map<String, String> result = new HashMap<>();
            // 从 code 中提取 openid（格式：test_openid_xxx）
            String openId = code.replace("test_openid_", "");
            if (openId.equals(code)) {
                // 如果没有指定 openid，使用默认值
                openId = "test_openid_123456";
            }
            result.put("openid", openId);
            result.put("session_key", "test_session_key_" + System.currentTimeMillis());
            result.put("unionid", "test_unionid_123456");
            return result;
        }
        
        // 正式模式：调用微信接口
        String url = WX_LOGIN_URL + "?appid=" + appId + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        
        try {
            String response = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = JSON.parseObject(response);
            
            Map<String, String> result = new HashMap<>();
            
            // 检查是否有错误
            if (jsonObject.containsKey("errcode")) {
                Integer errcode = jsonObject.getInteger("errcode");
                String errmsg = jsonObject.getString("errmsg");
                throw new RuntimeException("微信登录失败: " + errcode + " - " + errmsg);
            }
            
            result.put("openid", jsonObject.getString("openid"));
            result.put("session_key", jsonObject.getString("session_key"));
            
            // unionid 可能不存在
            if (jsonObject.containsKey("unionid")) {
                result.put("unionid", jsonObject.getString("unionid"));
            }
            
            return result;
        } catch (Exception e) {
            throw new RuntimeException("调用微信接口失败: " + e.getMessage(), e);
        }
    }
}

