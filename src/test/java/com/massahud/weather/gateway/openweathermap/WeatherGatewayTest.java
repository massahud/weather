package com.massahud.weather.gateway.openweathermap;


import com.massahud.weather.model.HourlyForecast;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.DailyWeatherForecast;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.City;
import net.aksingh.owmjapis.model.param.Main;
import net.aksingh.owmjapis.model.param.WeatherData;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;

import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WeatherGatewayTest {

    private static final String A_CITY = "LisbonPT";
    private Logger logger;
    private OWMWrapper owm;
    private WeatherGateway weatherGateway;

    @Before
    public void setUp() {
        logger = mock(Logger.class);
        owm = mock(OWMWrapper.class);
        weatherGateway = new WeatherGateway(logger, owm);
    }

    @Test
    public void shouldSetTheCityAndCountryFromOWMForecast() throws APIException {
        HourlyWeatherForecast hourlyWeatherForecast = new HourlyWeatherForecast();
        hourlyWeatherForecast.setCityData(new City(null, "Lisbon", null, "PT", null));
        hourlyWeatherForecast.setDataList(new ArrayList<>());
        when(owm.hourlyWeatherForecastByCityName(A_CITY)).thenReturn(hourlyWeatherForecast);

        HourlyForecast forecast = weatherGateway.getForecast(A_CITY);
        assertThat(forecast.getCity()).isEqualTo("Lisbon");
        assertThat(forecast.getCountry()).isEqualTo("PT");
    }

    @Test
    public void shouldConvertWeatherDataIntoHourAverages() throws APIException {
        HourlyWeatherForecast hourlyWeatherForecast = new HourlyWeatherForecast();
        hourlyWeatherForecast.setCityData(new City(null, "Lisbon", null, "PT", null));

        Date today = Date.from(LocalDateTime.of(2018,10,4,15,0).toInstant(ZoneOffset.UTC));
        List<WeatherData> dataList = new ArrayList<>();
        LocalDateTime firstDate = LocalDateTime.of(2018,10,4,15,0);
        LocalDateTime secondDate = LocalDateTime.of(2018,10,4,15,0);
        dataList.add(createWeatherData(firstDate, 35.123, 14.321));
        dataList.add(createWeatherData(secondDate, 10.0, 12.0));
        hourlyWeatherForecast.setDataList(dataList);
        when(owm.hourlyWeatherForecastByCityName(A_CITY)).thenReturn(hourlyWeatherForecast);

        HourlyForecast forecast = weatherGateway.getForecast(A_CITY);
        assertThat(forecast.getAverages().get(0).getDateTime()).isEqualTo(firstDate);
        assertThat(forecast.getAverages().get(1).getDateTime()).isEqualTo(secondDate);
        assertThat(forecast.getAverages().get(0).getTemperature()).isEqualTo(BigDecimal.valueOf(35.123));
        assertThat(forecast.getAverages().get(1).getTemperature()).isEqualTo(BigDecimal.valueOf(10.0));
        assertThat(forecast.getAverages().get(0).getPressure()).isEqualTo(BigDecimal.valueOf(14.321));
        assertThat(forecast.getAverages().get(1).getPressure()).isEqualTo(BigDecimal.valueOf(12.0));
    }

    @Test
    public void shouldThrowNotFoundExceptionIfAPIExceptionCodeIs404() throws APIException {
        when(owm.hourlyWeatherForecastByCityName(any())).thenThrow(new APIException(404, "foo"));
        assertThatThrownBy(() -> weatherGateway.getForecast(A_CITY)).isInstanceOf(NotFoundException.class);
    }

    @Test
    public void shouldThrowCouldNotConnectToOWMExceptionWhenAPIException() throws APIException {
        when(owm.hourlyWeatherForecastByCityName(any())).thenThrow(new APIException(500, "foo"));
        assertThatThrownBy(() -> weatherGateway.getForecast(A_CITY)).isInstanceOf(CouldNotConnectToOWMException.class);
    }

    private WeatherData createWeatherData(LocalDateTime date, Double temp, Double pressure) {
        Main main = new Main(temp, null, null, pressure, null, null, null, null);
        WeatherData weatherData = new WeatherData((int) date.toEpochSecond(ZoneOffset.UTC), main, null, null, null, null, null, null, null, null);
        return weatherData;
    }

}