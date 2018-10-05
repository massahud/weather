package com.massahud.weather.model;

import lombok.Data;

import java.util.List;

@Data
public class HourlyForecast {
    private String city;
    private String country;
    private List<HourAverage> averages;
}
