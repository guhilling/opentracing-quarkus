package it.hilling.training.opentracing;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello")
public class HelloResource {
    private static final Logger LOG = LoggerFactory.getLogger(HelloResource.class);

    @RestClient
    WorldResource worldResource;

    @Path("/{name}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello(@PathParam("name") @NotNull String name) {
        LOG.info("called hello");
        if(name.equals("world")) {
            name = worldResource.world();
        }
        return "hello " + name;
    }

}