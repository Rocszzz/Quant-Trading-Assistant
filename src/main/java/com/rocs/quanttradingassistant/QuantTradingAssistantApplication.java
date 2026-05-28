package com.rocs.quanttradingassistant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 量化交易助手后端启动类
 *
 * @author Rocs
 * @since 2026/05/28
 */
@MapperScan("com.rocs.quanttradingassistant.mapper")
@SpringBootApplication
public class QuantTradingAssistantApplication {

    /**
     * 启动后端服务
     *
     * @param args 启动参数
     */
    public static void main(String[] args) {
        SpringApplication.run(QuantTradingAssistantApplication.class, args);
    }
}
