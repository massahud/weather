package com.massahud.weather.gateway.openweathermap;

import javax.ws.rs.InternalServerErrorException;

public class CouldNotConnectToOWMException extends InternalServerErrorException {
    public CouldNotConnectToOWMException(String message, Exception nested) {
        super(message, nested);
    }
}
