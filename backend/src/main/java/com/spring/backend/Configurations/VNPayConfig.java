package com.spring.backend.Configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class VNPayConfig {
    @Getter
    @Value("${payment.vnpay.url}")
    private String payUrl;
    @Value("${payment.vnpay.return-url}")
    private String returnUrl;
    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode ;
    @Getter
    @Value("${payment.vnpay.secret-key}")
    private String secretKey;
    @Value("${payment.vnpay.version}")
    private String version;
    @Value("${payment.vnpay.command}")
    private String command;
    private String orderType = "other";
}