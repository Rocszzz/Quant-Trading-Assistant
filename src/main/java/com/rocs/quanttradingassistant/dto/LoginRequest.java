package com.rocs.quanttradingassistant.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * 登录请求参数
 *
 * @author Rocs
 * @since 2026/05/28
 */
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "LoginRequest{"
                + "username='" + username + '\''
                + '}';
    }
}
