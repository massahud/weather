package com.massahud.weather;

import com.massahud.test.categories.IntegrationTest;
import com.massahud.weather.config.JacksonConfig;
import com.massahud.weather.config.LogConfig;
import com.massahud.weather.config.OWMConfig;
import com.massahud.weather.config.RESTConfig;
import com.massahud.weather.gateway.openweathermap.WeatherGateway;
import io.restassured.RestAssured;
import io.restassured.matcher.RestAssuredMatchers;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static io.restassured.matcher.RestAssuredMatchers.containsPath;
import static org.hamcrest.Matchers.*;

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
public class WeatherApplicationTests {

    @LocalServerPort
    private Integer serverPort;

    @Value("${owm.key}")
    private String owmKey;

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://localhost:" + serverPort;
    }

    @Test
    public void contextLoads() {
    }


    @Test
    public void shouldListAveragesForLisbon() {
        when()
                .get("/api/data/{city}", "Lisbon")
        .then()
                .statusCode(200)
                .body("city", equalTo("Lisbon"))
                .body("country", equalTo("PT"))
                .body("averages", hasSize(3));
    }

    @Test
    public void shouldReturnNotFound() {
        when()
                .get("/api/data/{city}", "NON EXISTENT")
        .then()
                .statusCode(404);
    }

}
