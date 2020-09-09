package it.hilling.training.opentracing;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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
        Span span = tracer.buildSpan("worldimpl")
                .withTag(Tags.COMPONENT.getKey(), "custom")
                .start();
        LOG.info("called world");
        span.finish();
        return databaseResource.translate("world");
    }

}