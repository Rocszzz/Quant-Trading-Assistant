package com.rocs.quanttradingassistant.service;

import com.rocs.quanttradingassistant.dto.LoginRequest;
import com.rocs.quanttradingassistant.dto.RegisterRequest;
import com.rocs.quanttradingassistant.dto.UpdateNicknameRequest;
import com.rocs.quanttradingassistant.vo.LoginResponseVO;
import com.rocs.quanttradingassistant.vo.LoginUserVO;

/**
 * 认证服务接口
 *
 * @author Rocs
 * @since 2026/05/28
 */
public interface AuthService {

    /**
     * 注册系统用户
     *
     * @param request 注册请求参数
     * @return 登录响应信息
     */
    LoginResponseVO register(RegisterRequest request);

    /**
     * 使用账号密码登录系统
     *
     * @param request 登录请求参数
     * @return 登录响应信息
     */
    LoginResponseVO login(LoginRequest request);

    /**
     * 退出当前登录会话
     *
     * @param authorization HTTP Authorization 请求头
     */
    void logout(String authorization);

    /**
     * 查询当前登录用户信息
     *
     * @param authorization HTTP Authorization 请求头
     * @return 当前登录用户信息
     */
    LoginUserVO me(String authorization);

    /**
     * 修改当前用户昵称
     *
     * @param authorization HTTP Authorization 请求头
     * @param request 修改昵称请求参数
     * @return 当前登录用户信息
     */
    LoginUserVO updateNickname(String authorization, UpdateNicknameRequest request);

    /**
     * 获取当前登录用户ID
     *
     * @param authorization HTTP Authorization 请求头
     * @return 当前登录用户ID
     */
    Long getCurrentUserId(String authorization);
}
