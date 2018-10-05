package com.massahud.weather.gateway.openweathermap;

import net.aksingh.owmjapis.api.APIException;

import javax.ws.rs.InternalServerErrorException;

public class CouldNotConnectToOWMException extends InternalServerErrorException {
    public CouldNotConnectToOWMException(String message, Exception nested) {
        super(message, nested);
    }
}
