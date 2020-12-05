package it.hilling.training.opentracing;

import io.opentracing.*;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

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
        tracer.scopeManager().activate(span, true);
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
        try (Scope scope = tracer.scopeManager().activate(span, true)) {
            sleep(10 * index);
        } catch(Exception ex) {
            Tags.ERROR.set(span, true);
            span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, ex, Fields.MESSAGE, ex.getMessage()));
        } finally {
            span.finish();
        }
    }

    private void createReferencingSpan(int index) {
        Span span = tracer.buildSpan("referencing span " + index)
                .ignoreActiveSpan()
                .addReference(References.FOLLOWS_FROM, tracer.activeSpan().context())
                .withTag("mytag", "follows from")
                .start();
        try(Scope scope = tracer.scopeManager().activate(span, true)) {
            sleep(10 * index);
        } finally {
            span.finish();
        }
    }

    private void createIndependantSpan(int index) {
        Span span = tracer.buildSpan("independant span " + index)
                .ignoreActiveSpan()
                .withTag("mytag", "independant")
                .start();
        try(Scope scope = tracer.scopeManager().activate(span, true)) {
            sleep(10 * index);
        } finally {
            span.finish();
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
