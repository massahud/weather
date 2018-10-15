package com.massahud.weather.service;

import com.massahud.weather.gateway.openweathermap.WeatherGateway;
import com.massahud.weather.model.AverageForecast;
import com.massahud.weather.model.DayAverage;
import com.massahud.weather.model.HourAverage;
import com.massahud.weather.model.HourlyForecast;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private final Logger logger;
    private final WeatherGateway weatherGateway;


    @Inject
    public WeatherService(Logger logger, WeatherGateway weatherGateway) {
        this.logger = logger;
        this.weatherGateway = weatherGateway;
    }

    public AverageForecast getCityWeather(String city) {
        logger.debug("Getting weather for city {}", city);
        HourlyForecast hourlyForecast = weatherGateway.getForecast(city);
        AverageForecast averageForecast = new AverageForecast();
        averageForecast.setCity(hourlyForecast.getCity());
        averageForecast.setCountry(hourlyForecast.getCountry());
        averageForecast.setAverages(calculateDayAverages(hourlyForecast.getAverages()));

        logger.debug("Got weather for city {}", city);
        return averageForecast;
    }

    private List<DayAverage> calculateDayAverages(List<HourAverage> averages) {

        ArrayList<DayAverage> calculated = new ArrayList<>();
        if (!averages.isEmpty()) {

            int countDay = 0;
            int countNight = 0;
            int countPressure = 0;


            LocalDate startDate = getDateOfDayAverage(averages.get(0).getDateTime());
            LocalDate currentDate = startDate;

            BigDecimal dayTemp = BigDecimal.ZERO;
            BigDecimal nightTemp = BigDecimal.ZERO;
            BigDecimal pressure = BigDecimal.ZERO;
            for (HourAverage avg : averages) {
                LocalDate date = getDateOfDayAverage(avg.getDateTime());

                if (ChronoUnit.DAYS.between(startDate, date) > 2) {
                    currentDate = date;
                    break;
                }
                if (!date.equals(currentDate)) {

                    calculated.add(new DayAverage(
                            currentDate,
                            countDay == 0 ? null :dayTemp.divide(BigDecimal.valueOf(countDay), RoundingMode.HALF_UP),
                            countNight == 0 ? null :nightTemp.divide(BigDecimal.valueOf(countNight), RoundingMode.HALF_UP),
                            countPressure == 0 ? null :pressure.divide(BigDecimal.valueOf(countPressure), RoundingMode.HALF_UP))
                    );

                    countDay = 0;
                    countNight = 0;
                    countPressure = 0;

                    dayTemp = BigDecimal.ZERO;
                    nightTemp = BigDecimal.ZERO;
                    pressure = BigDecimal.ZERO;
                    currentDate = date;
                }

                if (isDay(avg)) {
                    dayTemp = dayTemp.add(avg.getTemperature());
                    countDay++;
                } else {
                    nightTemp = nightTemp.add(avg.getTemperature());
                    countNight++;
                }
                pressure = pressure.add(avg.getPressure());
                countPressure++;
            }
            calculated.add(new DayAverage(
                    currentDate,
                    countDay == 0 ? null : dayTemp.divide(BigDecimal.valueOf(countDay), RoundingMode.HALF_UP),
                    countNight == 0 ? null : nightTemp.divide(BigDecimal.valueOf(countNight), RoundingMode.HALF_UP),
                    countPressure == 0 ? null : pressure.divide(BigDecimal.valueOf(countPressure), RoundingMode.HALF_UP))
            );
        }
        return calculated;

    }

    /**
     * For day averages, hours before 6 are for the previous date
     *
     * @param dateTime date time to get day average date
     * @return day average date
     */
    private LocalDate getDateOfDayAverage(LocalDateTime dateTime) {
        int hour = dateTime.getHour();
        LocalDate date = dateTime.toLocalDate();
        if (hour >= 0 && hour < 6) {
            date = date.minusDays(1);
        }
        return date;
    }

    private boolean isDay(HourAverage hourAverage) {
        int hour = hourAverage.getDateTime().getHour();
        return hour >= 6 && hour < 18;
    }
//    private final WeatherGateway weatherClient;

//    @Inject
//    public WeatherService(WeatherGateway weatherClient) {
//        this.weatherClient = weatherClient;
//    }

}
