package com.bookbla.americano.base.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.bookbla.americano")
public class FeignClientsConfig {

}
