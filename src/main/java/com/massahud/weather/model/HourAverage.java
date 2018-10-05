package com.massahud.weather.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class HourAverage {
    private LocalDateTime dateTime;
    private BigDecimal temperature;
    private BigDecimal pressure;


}
