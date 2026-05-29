package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 登录响应展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "登录响应展示对象")
public class LoginResponseVO {

    @Schema(description = "登录令牌", example = "9f1a2b3c4d5e6f")
    private String token;

    @Schema(description = "登录用户信息")
    private LoginUserVO user;

    public LoginResponseVO() {
    }

    public LoginResponseVO(String token, LoginUserVO user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LoginUserVO getUser() {
        return user;
    }

    public void setUser(LoginUserVO user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "LoginResponseVO{"
                + "token='***'"
                + ", user=" + user
                + '}';
    }
}
