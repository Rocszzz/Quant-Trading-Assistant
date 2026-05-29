package com.rocs.quanttradingassistant.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 当前登录用户展示对象
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "当前登录用户展示对象")
public class LoginUserVO {

    @Schema(description = "用户ID", example = "1")
    private Long id;

    @Schema(description = "用户名", example = "admin")
    private String username;

    @Schema(description = "昵称", example = "量化助手演示用户")
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
