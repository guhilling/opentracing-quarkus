package it.hilling.training.opentracing;

import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.tag.Tags;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/demo")
public class DemoResource {

    @Inject
    Tracer tracer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String demo() {
        Span span = tracer.buildSpan("demoSpan")
                .withTag(Tags.COMPONENT.getKey(), "demo")
                .start();
        for(int i=0; i<10; i++) {
            createChildSpan(i);
        }
        span.finish();
        return "spans created";
    }

    private void createChildSpan(int index) {
        Span span = tracer.buildSpan("childSpan " + index)
                .start();
        try {
            Thread.sleep(10 * index);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        span.finish();
    }

}
