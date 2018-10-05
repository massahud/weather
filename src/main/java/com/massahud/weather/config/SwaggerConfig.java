package com.massahud.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.converter.ModelConverters;
import io.swagger.jackson.ModelResolver;
import io.swagger.jaxrs.config.BeanConfig;
import org.springframework.context.annotation.Configuration;

import javax.inject.Inject;

@Configuration
public class SwaggerConfig {

    @Inject
    public SwaggerConfig(ObjectMapper mapper) {
        ModelConverters.getInstance().addConverter(new ModelResolver(mapper));

        final BeanConfig beanConfig = new BeanConfig();
        beanConfig.setTitle("Weather");

        beanConfig.setDescription("Service that returns weather information");

        beanConfig.setVersion("1.0");

        beanConfig.setBasePath("/api");

        beanConfig.setResourcePackage("com.massahud.weather.resource");
        beanConfig.setScan(true);
        beanConfig.setPrettyPrint(true);
    }
}
