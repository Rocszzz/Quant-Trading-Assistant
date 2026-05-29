package com.rocs.quanttradingassistant.controller;

import com.rocs.quanttradingassistant.common.Result;
import com.rocs.quanttradingassistant.dto.LoginRequest;
import com.rocs.quanttradingassistant.dto.RegisterRequest;
import com.rocs.quanttradingassistant.dto.UpdateNicknameRequest;
import com.rocs.quanttradingassistant.service.AuthService;
import com.rocs.quanttradingassistant.vo.LoginResponseVO;
import com.rocs.quanttradingassistant.vo.LoginUserVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户认证控制器
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Tag(name = "用户模块")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * 用户注册
     *
     * @param request 注册请求参数
     * @return 登录响应信息
     */
    @Operation(summary = "用户注册")
    @PostMapping("/register")
    public Result<LoginResponseVO> register(@Valid @RequestBody RegisterRequest request) {
        return Result.success(authService.register(request));
    }

    /**
     * 用户登录
     *
     * @param request 登录请求参数
     * @return 登录响应信息
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginRequest request) {
        return Result.success(authService.login(request));
    }

    /**
     * 用户退出登录
     *
     * @param authorization 认证请求头
     * @return 统一响应
     */
    @Operation(summary = "用户退出登录")
    @PostMapping("/logout")
    public Result<Void> logout(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        authService.logout(authorization);
        return Result.success();
    }

    /**
     * 查询当前用户
     *
     * @param authorization 认证请求头
     * @return 当前用户信息
     */
    @Operation(summary = "查询当前用户信息")
    @GetMapping("/me")
    public Result<LoginUserVO> me(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization
    ) {
        return Result.success(authService.me(authorization));
    }

    /**
     * 修改当前用户昵称
     *
     * @param authorization 认证请求头
     * @param request 修改昵称请求参数
     * @return 当前用户信息
     */
    @Operation(summary = "修改昵称")
    @PutMapping("/nickname")
    public Result<LoginUserVO> updateNickname(
            @Parameter(description = "Bearer token")
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @Valid @RequestBody UpdateNicknameRequest request
    ) {
        return Result.success(authService.updateNickname(authorization, request));
    }
}
