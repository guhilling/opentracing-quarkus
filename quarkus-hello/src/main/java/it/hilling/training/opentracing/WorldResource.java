package it.hilling.training.opentracing;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "world-api")
@Path("/world")
public interface WorldResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String world();

}
