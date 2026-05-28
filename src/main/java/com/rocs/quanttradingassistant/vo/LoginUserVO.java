package com.rocs.quanttradingassistant.vo;

/**
 * 当前登录用户展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
public class LoginUserVO {

    private Long id;

    private String username;

    private String nickname;

    public LoginUserVO() {
    }

    public LoginUserVO(Long id, String username, String nickname) {
        this.id = id;
        this.username = username;
        this.nickname = nickname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "LoginUserVO{"
                + "id=" + id
                + ", username='" + username + '\''
                + ", nickname='" + nickname + '\''
                + '}';
    }
}
