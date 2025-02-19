package it.hilling.training.opentracing;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@RegisterRestClient(configKey = "world-api")
@Path("/world")
public class WorldResourceImpl {
    private static final Logger LOG = LoggerFactory.getLogger(WorldResourceImpl.class);

    @Inject
    Tracer tracer;

    @Inject
    DatabaseResource databaseResource;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String world() {
        Span span = tracer.spanBuilder("worldimpl")
                          .setAttribute("custom", "customValue")
                          .startSpan();
        LOG.info("called world");
        span.end();
        return databaseResource.translate("world");
    }

}
