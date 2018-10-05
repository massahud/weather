package com.massahud.weather.service;


import com.massahud.weather.gateway.openweathermap.WeatherGateway;
import com.massahud.weather.model.AverageForecast;
import com.massahud.weather.model.HourAverage;
import com.massahud.weather.model.HourlyForecast;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.assertj.core.api.Assertions.assertThat;

public class WeatherServiceTest {

    private static final String A_CITY = "Lisbon";
    private static final String A_COUNTRY = "PT";

    private static final LocalDate yesterday = LocalDate.of(2018, 10, 3);
    private static final LocalDate today = LocalDate.of(2018, 10, 4);
    private static final LocalDate tomorrow = LocalDate.of(2018, 10, 5);

    private final Logger logger = mock(Logger.class);

    private HourlyForecast forecast;
    private WeatherGateway weatherGateway;
    private WeatherService weatherService;

    @Before
    public void setUp() {
        forecast = new HourlyForecast();
        forecast.setCity(A_CITY);
        forecast.setCountry(A_COUNTRY);

        ArrayList<HourAverage> averages = new ArrayList<>();

        averages.add(new HourAverage(LocalDateTime.of(
                today,
                LocalTime.of(6, 0)),
                BigDecimal.valueOf(8),
                BigDecimal.valueOf(8))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                today,
                LocalTime.of(9, 0)),
                BigDecimal.valueOf(16),
                BigDecimal.valueOf(16))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                today,
                LocalTime.of(12, 0)),
                BigDecimal.valueOf(32),
                BigDecimal.valueOf(32))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                today,
                LocalTime.of(15, 0)),
                BigDecimal.valueOf(64),
                BigDecimal.valueOf(64))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                today,
                LocalTime.of(18, 0)),
                BigDecimal.valueOf(128),
                BigDecimal.valueOf(128))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                today,
                LocalTime.of(21, 0)),
                BigDecimal.valueOf(256),
                BigDecimal.valueOf(256))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                tomorrow,
                LocalTime.of(0, 0)),
                BigDecimal.valueOf(512),
                BigDecimal.valueOf(512))
        );
        averages.add(new HourAverage(LocalDateTime.of(
                tomorrow,
                LocalTime.of(3, 0)),
                BigDecimal.valueOf(1024),
                BigDecimal.valueOf(1024))
        );


        forecast.setAverages(averages);

        weatherGateway = mock(WeatherGateway.class);
        when(weatherGateway.getForecast(A_CITY)).thenReturn(forecast);

        weatherService = new WeatherService(logger, weatherGateway);
    }


    @Test
    public void shouldCalculateTheDayAveragesForToday() {
        AverageForecast averageForecast = weatherService.getCityWeather(A_CITY);

        assertThat(averageForecast.getCity()).isEqualTo(A_CITY);
        assertThat(averageForecast.getCountry()).isEqualTo(A_COUNTRY);
        assertThat(averageForecast.getAverages().get(0).getDate()).isEqualTo(today);
        assertThat(averageForecast.getAverages().get(0).getDayTemperature()).isEqualTo(BigDecimal.valueOf(8+16+32+64).divide(BigDecimal.valueOf(4)));
        assertThat(averageForecast.getAverages().get(0).getNightTemperature()).isEqualTo(BigDecimal.valueOf(128+256+512+1024).divide(BigDecimal.valueOf(4)));
    }

    @Test
    public void shouldCalculateOnlyTheNightAverageIfThereIsNoDayData() {
        forecast.getAverages().removeIf((avg -> avg.getDateTime().getHour() >= 6 && avg.getDateTime().getHour() < 18));

        AverageForecast averageForecast = weatherService.getCityWeather(A_CITY);

        assertThat(averageForecast.getAverages().get(0).getDate()).isEqualTo(today);
        assertThat(averageForecast.getAverages().get(0).getNightTemperature()).isEqualTo(BigDecimal.valueOf(128+256+512+1024).divide(BigDecimal.valueOf(4)));
        assertThat(averageForecast.getAverages().get(0).getDayTemperature()).isNull();
    }


    @Test
    public void shouldCalculateOnlyTheDayAverageIfThereIsNoNightData() {
        forecast.getAverages().removeIf((avg -> avg.getDateTime().getHour() < 6 || avg.getDateTime().getHour() >= 18));

        AverageForecast averageForecast = weatherService.getCityWeather(A_CITY);

        assertThat(averageForecast.getAverages().get(0).getDate()).isEqualTo(today);
        assertThat(averageForecast.getAverages().get(0).getNightTemperature()).isNull();
        assertThat(averageForecast.getAverages().get(0).getDayTemperature()).isEqualTo(BigDecimal.valueOf(8+16+32+64).divide(BigDecimal.valueOf(4)));
    }
}