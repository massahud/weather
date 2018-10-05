package com.massahud.weather.config;

import com.massahud.weather.gateway.openweathermap.OWMWrapper;
import net.aksingh.owmjapis.core.OWM;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OWMConfig {

    @Value("${owm.key}")
    private String owmApiKey;

    @Bean
    public OWMWrapper getOWMWrapper() {

        OWMWrapper owmWrapper = new OWMWrapper(owmApiKey);
        owmWrapper.getDelegate().setUnit(OWM.Unit.METRIC);

        return owmWrapper;
    }
}
