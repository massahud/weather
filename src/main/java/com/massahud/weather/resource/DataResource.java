package com.massahud.weather.resource;

import com.massahud.weather.model.AverageForecast;
import com.massahud.weather.model.DayAverage;
import com.massahud.weather.service.WeatherService;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.validation.constraints.NotBlank;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;

@Api
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("/data")

@Service
public class DataResource {

    private final Logger logger;
    private final WeatherService weatherService;

    @Inject
    public DataResource(Logger logger, WeatherService weatherService) {
        this.logger = logger;
        this.weatherService = weatherService;
    }

    @GET
    @Path("/{city: .+}")
    @ApiOperation(value = "Get weather information for a city", response = AverageForecast.class,
            notes = "<b>city</b> can be only a city name or a city name followed by " +
                    "the country code, separated by comma.")
    @ApiResponses({
            @ApiResponse(code = 404, message = "City not found"),
    })
    public Response getData(
            @ApiParam(name = "city", required = true, example = "Lisbon") @PathParam("city") @NotBlank String city) {
        logger.debug("Getting date for {}", city);
        AverageForecast weather = new AverageForecast();
        weather.setCity(city);
        weather.setCountry("PT");
        weather.setAverages(new ArrayList<>());
        DayAverage today = new DayAverage(
                LocalDate.now(),
                BigDecimal.valueOf(1.123).setScale(2, RoundingMode.HALF_UP),
                BigDecimal.TEN,
                BigDecimal.ZERO);
        weather.getAverages().add(today);
        AverageForecast averageForecast = weatherService.getCityWeather(city);
        logger.debug("Got data for {}", city);

        return Response.ok(averageForecast).build();
    }
}
