package com.massahud.weather.resource;

import com.massahud.weather.model.AverageForecast;
import com.massahud.weather.service.WeatherService;
import org.junit.Test;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DataResourceTest {

    @Test
    public void shouldReturnTheServiceInformation() {

        WeatherService weatherService = mock(WeatherService.class);
        AverageForecast averageForecast = mock(AverageForecast.class);
        when(weatherService.getCityWeather("Lisbon")).thenReturn(averageForecast);

        DataResource dataResource = new DataResource(mock(Logger.class), weatherService);

        Response response = dataResource.getData("Lisbon");

        assertThat(response.getEntity()).isSameAs(averageForecast);

    }

}