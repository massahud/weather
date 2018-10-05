package com.massahud.weather.service;

import com.massahud.test.categories.IntegrationTest;
import com.massahud.weather.config.JacksonConfig;
import com.massahud.weather.config.LogConfig;
import com.massahud.weather.config.OWMConfig;
import com.massahud.weather.config.RESTConfig;
import com.massahud.weather.gateway.openweathermap.WeatherGateway;
import com.massahud.weather.model.AverageForecast;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@Category(IntegrationTest.class)
@ActiveProfiles({"test", "development", "default"})
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {
                RESTConfig.class,
                JacksonConfig.class,
                ValidationAutoConfiguration.class,
                LogConfig.class,
                OWMConfig.class,
                WeatherGateway.class,
                WeatherService.class
        }
)
public class WeatherServiceITest {

    @Inject
    private WeatherService weatherService;

    @Test
    public void shouldReturn3DayForecasts() {

        final AverageForecast forecast = weatherService.getCityWeather("Lisbon");
        assertThat(forecast.getAverages()).hasSize(3);

    }
}