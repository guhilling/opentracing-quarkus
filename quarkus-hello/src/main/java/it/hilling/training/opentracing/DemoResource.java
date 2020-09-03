package it.hilling.training.opentracing;

import io.opentracing.References;
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
        Span span = tracer.buildSpan("demo span")
                .withTag(Tags.COMPONENT.getKey(), "demo")
                .start();
        for(int i=0; i<5; i++) {
            createChildSpan(i);
        }
        for(int i=0; i<5; i++) {
            createReferencingSpan(i);
        }
        for(int i=0; i<5; i++) {
            createIndependantSpan(i);
        }
        span.finish();
        return "spans created";
    }

    private void createChildSpan(int index) {
        Span span = tracer.buildSpan("child span " + index)
                .withTag("mytag", "a child span")
                .start();
        sleep(10 * index);
        span.finish();
    }

    private void createReferencingSpan(int index) {
        Span span = tracer.buildSpan("referencing span " + index)
                .ignoreActiveSpan()
                .addReference(References.FOLLOWS_FROM, tracer.activeSpan().context())
                .withTag("mytag", "follows from")
                .start();
        sleep(10 * index);
        span.finish();
    }

    private void createIndependantSpan(int index) {
        Span span = tracer.buildSpan("independant span " + index)
                .ignoreActiveSpan()
                .withTag("mytag", "independant")
                .start();
        sleep(10 * index);
        span.finish();
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
