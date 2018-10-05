package com.massahud.weather.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor(onConstructor_ = {@JsonCreator})
public class DayAverage {
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private BigDecimal dayTemperature;
    private BigDecimal nightTemperature;
    private BigDecimal pressure;
}
