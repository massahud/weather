package com.massahud.weather.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Configure logger injection
 */
@Configuration
public class LogConfig {

    @Bean
    @Scope("prototype")
    public Logger logger(InjectionPoint injectionPoint) {
        return LoggerFactory.getLogger(
                injectionPoint.getMethodParameter() != null
                        ? injectionPoint.getMethodParameter().getContainingClass()
                        : injectionPoint.getField().getDeclaringClass());
    }
}
