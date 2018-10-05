package com.massahud.weather.gateway.openweathermap;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import org.jetbrains.annotations.NotNull;

/**
 * Delegation wrapper needed for unit tests, because OWM has final methods.
 */
public class OWMWrapper {

    private final OWM owm;

    public OWMWrapper(String apiKey) {
        owm = new OWM(apiKey);
    }

    public OWM getDelegate() {
        return owm;
    }

    @NotNull
    public HourlyWeatherForecast hourlyWeatherForecastByCityName(@NotNull String cityName) throws APIException {
        return owm.hourlyWeatherForecastByCityName(cityName);
    }
}
