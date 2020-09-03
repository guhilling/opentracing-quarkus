package it.hilling.training.opentracing;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "world-api")
@Path("/world")
public interface WorldResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String world();

}
