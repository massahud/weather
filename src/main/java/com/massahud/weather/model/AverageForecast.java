package com.massahud.weather.model;

import lombok.Data;

import java.util.List;

@Data
public class AverageForecast {
    private String city;
    private String country;
    private List<DayAverage> averages;
}
