package com.rocs.quanttradingassistant.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI 文档配置类
 *
 * @author Rocs
 * @since 2026/05/28
 */
@Configuration
public class OpenApiConfig {

    /**
     * 配置接口文档基础信息
     *
     * @return OpenAPI 配置
     */
    @Bean
    public OpenAPI quantTradingAssistantOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Quant Trading Assistant API")
                        .description("量化交易助手后端接口文档，当前阶段仅提供登录等基础能力")
                        .version("0.0.1"));
    }
}
