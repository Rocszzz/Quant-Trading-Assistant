package com.rocs.quanttradingassistant.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocs.quanttradingassistant.common.ResultCode;
import com.rocs.quanttradingassistant.dto.LoginRequest;
import com.rocs.quanttradingassistant.exception.BusinessException;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.vo.LoginResponseVO;
import com.rocs.quanttradingassistant.vo.LoginUserVO;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 认证服务实现类
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final String TOKEN_PREFIX = "Bearer ";

    private static final String LOGIN_TOKEN_KEY_PREFIX = "quant:auth:token:";

    private static final long DEMO_USER_ID = 1L;

    private static final Duration LOGIN_EXPIRE_TIME = Duration.ofHours(2);

    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    @Value("${quant.auth.username:admin}")
    private String demoUsername;

    @Value("${quant.auth.password:admin123}")
    private String demoPassword;

    @Value("${quant.auth.nickname:量化助手演示用户}")
    private String demoNickname;

    public AuthServiceImpl(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public LoginResponseVO login(LoginRequest request) {
        if (!demoUsername.equals(request.getUsername()) || !demoPassword.equals(request.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }

        LoginUserVO user = new LoginUserVO(DEMO_USER_ID, demoUsername, demoNickname);
        String token = UUID.randomUUID().toString().replace("-", "");
        try {
            // 当前阶段只保存演示用户会话，不连接真实证券账户，也不包含任何交易权限。
            stringRedisTemplate.opsForValue().set(buildRedisKey(token), objectMapper.writeValueAsString(user), LOGIN_EXPIRE_TIME);
        } catch (JsonProcessingException exception) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "登录会话创建失败");
        }
        return new LoginResponseVO(token, user);
    }

    @Override
    public void logout(String authorization) {
        String token = parseToken(authorization);
        if (!StringUtils.hasText(token)) {
            return;
        }
        stringRedisTemplate.delete(buildRedisKey(token));
    }

    @Override
    public LoginUserVO me(String authorization) {
        String token = parseToken(authorization);
        if (!StringUtils.hasText(token)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "请先登录");
        }

        String userJson = stringRedisTemplate.opsForValue().get(buildRedisKey(token));
        if (!StringUtils.hasText(userJson)) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "登录状态已失效");
        }

        try {
            return objectMapper.readValue(userJson, LoginUserVO.class);
        } catch (JsonProcessingException exception) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "登录用户信息解析失败");
        }
    }

    private String parseToken(String authorization) {
        if (!StringUtils.hasText(authorization)) {
            return null;
        }
        if (authorization.startsWith(TOKEN_PREFIX)) {
            return authorization.substring(TOKEN_PREFIX.length()).trim();
        }
        return authorization.trim();
    }

    private String buildRedisKey(String token) {
        return LOGIN_TOKEN_KEY_PREFIX + token;
    }
}
