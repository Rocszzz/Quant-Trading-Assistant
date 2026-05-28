package com.rocs.quanttradingassistant.vo;

/**
 * 登录响应展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
public class LoginResponseVO {

    private String token;

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
