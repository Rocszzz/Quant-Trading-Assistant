package com.rocs.quanttradingassistant.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rocs.quanttradingassistant.common.PasswordUtils;
import com.rocs.quanttradingassistant.common.ResultCode;
import com.rocs.quanttradingassistant.dto.LoginRequest;
import com.rocs.quanttradingassistant.dto.RegisterRequest;
import com.rocs.quanttradingassistant.dto.UpdateNicknameRequest;
import com.rocs.quanttradingassistant.entity.User;
import com.rocs.quanttradingassistant.exception.BusinessException;
import com.rocs.quanttradingassistant.mapper.UserMapper;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.vo.LoginResponseVO;
import com.rocs.quanttradingassistant.vo.LoginUserVO;
import java.time.Duration;
import java.time.LocalDateTime;
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

    private final StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper;

    private final UserMapper userMapper;

    @Value("${quant.auth.token-expire-hours:2}")
    private long tokenExpireHours;

    public AuthServiceImpl(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper, UserMapper userMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
        this.userMapper = userMapper;
    }

    @Override
    public LoginResponseVO register(RegisterRequest request) {
        User existsUser = selectByUsername(request.getUsername());
        if (existsUser != null) {
            throw new BusinessException(ResultCode.CONFLICT, "用户名已存在");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(PasswordUtils.encode(request.getPassword()));
        user.setNickname(request.getNickname());
        userMapper.insert(user);
        return buildLoginResponse(toLoginUserVO(user));
    }

    @Override
    public LoginResponseVO login(LoginRequest request) {
        User user = selectByUsername(request.getUsername());
        if (user == null || !PasswordUtils.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "用户名或密码错误");
        }
        return buildLoginResponse(toLoginUserVO(user));
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
        Long userId = getCurrentUserId(authorization);
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "登录用户不存在");
        }
        return toLoginUserVO(user);
    }

    @Override
    public LoginUserVO updateNickname(String authorization, UpdateNicknameRequest request) {
        Long userId = getCurrentUserId(authorization);
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED, "登录用户不存在");
        }

        user.setNickname(request.getNickname());
        user.setUpdateTime(LocalDateTime.now());
        userMapper.updateById(user);

        LoginUserVO userVO = toLoginUserVO(user);
        refreshSession(authorization, userVO);
        return userVO;
    }

    @Override
    public Long getCurrentUserId(String authorization) {
        LoginUserVO user = getSessionUser(authorization);
        return user.getId();
    }

    private LoginResponseVO buildLoginResponse(LoginUserVO user) {
        String token = UUID.randomUUID().toString().replace("-", "");
        saveSession(token, user);
        return new LoginResponseVO(token, user);
    }

    private User selectByUsername(String username) {
        return userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, username));
    }

    private LoginUserVO getSessionUser(String authorization) {
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

    private void saveSession(String token, LoginUserVO user) {
        try {
            // token 仅代表本系统登录会话，不包含任何证券交易授权信息。
            stringRedisTemplate.opsForValue().set(
                    buildRedisKey(token),
                    objectMapper.writeValueAsString(user),
                    Duration.ofHours(tokenExpireHours)
            );
        } catch (JsonProcessingException exception) {
            throw new BusinessException(ResultCode.BUSINESS_ERROR, "登录会话创建失败");
        }
    }

    private void refreshSession(String authorization, LoginUserVO user) {
        String token = parseToken(authorization);
        if (StringUtils.hasText(token)) {
            saveSession(token, user);
        }
    }

    private LoginUserVO toLoginUserVO(User user) {
        return new LoginUserVO(user.getId(), user.getUsername(), user.getNickname());
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
