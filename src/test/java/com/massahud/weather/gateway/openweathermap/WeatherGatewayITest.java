package com.massahud.weather.gateway.openweathermap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.massahud.test.categories.IntegrationTest;
import com.massahud.weather.config.JacksonConfig;
import com.massahud.weather.config.LogConfig;
import com.massahud.weather.config.OWMConfig;
import com.massahud.weather.config.RESTConfig;
import com.massahud.weather.model.HourlyForecast;
import net.aksingh.owmjapis.api.APIException;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
                WeatherGateway.class
        }
)

public class WeatherGatewayITest {

    @Inject
    private WeatherGateway gateway;

    @Test
    public void shouldGetAForecastForLisbon() {
        HourlyForecast forecast = gateway.getForecast("Lisbon");
        assertThat(forecast.getCity()).isEqualTo("Lisbon");
        assertThat(forecast.getCountry()).isEqualTo("PT");
        assertThat(forecast.getAverages()).isNotEmpty();
    }

    @Test
    public void shouldThrowNotFoundExceptionForNonExistentCity() {
        assertThatThrownBy(() -> gateway.getForecast("NON_EXISTENT"))
                .isInstanceOf(NotFoundException.class);

    }

}