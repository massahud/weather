package com.massahud.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.massahud.weather.resource.DataResource;
import io.swagger.jaxrs.listing.ApiListingResource;
import io.swagger.jaxrs.listing.SwaggerSerializers;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

/**
 * Configuration of JAX-RS with Jersey
 */
@Configuration
@ComponentScan("com.massahud.weather")
@ApplicationPath("/api")
public class RESTConfig extends ResourceConfig {

    public RESTConfig(ObjectMapper mapper) {
        register(JacksonJaxbJsonProvider.class);
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);

        register(DataResource.class);
        packages("com.massahud.weather");
        packages("com.olx.mercury.config");

        // swagger
        register(ApiListingResource.class);
        register(SwaggerSerializers.class);

    }
}
