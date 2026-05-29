package com.rocs.quanttradingassistant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 修改昵称请求参数
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Schema(description = "修改昵称请求参数")
public class UpdateNicknameRequest {

    @Schema(description = "昵称", example = "量化研究员")
    @NotBlank(message = "昵称不能为空")
    @Size(max = 32, message = "昵称长度不能超过32个字符")
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
