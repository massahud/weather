package com.massahud.weather.gateway.openweathermap;

import com.massahud.weather.model.HourAverage;
import com.massahud.weather.model.HourlyForecast;
import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

public class WeatherGateway {

    private final Logger logger;
    private final OWMWrapper owm;

    @Inject
    public WeatherGateway(Logger logger, OWMWrapper owm) {
        this.logger = logger;
        this.owm = owm;
    }

    public HourlyForecast getForecast(String city) {
        try {

            HourlyWeatherForecast owmForecast = owm.hourlyWeatherForecastByCityName(city);
            HourlyForecast hourlyForecast = new HourlyForecast();
            hourlyForecast.setCity(owmForecast.getCityData().getName());
            hourlyForecast.setCountry(owmForecast.getCityData().getCountryCode());
            List<HourAverage> averages = owmForecast.getDataList().stream().map(weatherData ->
                new HourAverage(
                        LocalDateTime.ofInstant(weatherData.getDateTime().toInstant(), ZoneOffset.UTC),
                        BigDecimal.valueOf(weatherData.getMainData().getTemp()),
                        BigDecimal.valueOf(weatherData.getMainData().getPressure()))
            ).collect(Collectors.toList());
            hourlyForecast.setAverages(averages);
            return hourlyForecast;
        } catch (APIException e) {
            logger.error("error connecting to the weather provider: " + e.getMessage(), e);
            if (e.getCode() == 404) {
                throw new NotFoundException();
            }
            throw new CouldNotConnectToOWMException("error connecting to the weather provider: " + e.getMessage(), e);
        }
    }

}
