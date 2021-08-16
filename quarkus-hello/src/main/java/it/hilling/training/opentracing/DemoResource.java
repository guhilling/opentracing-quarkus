package it.hilling.training.opentracing;

import io.opentracing.References;
import io.opentracing.Scope;
import io.opentracing.Span;
import io.opentracing.Tracer;
import io.opentracing.log.Fields;
import io.opentracing.tag.Tags;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
        tracer.scopeManager().activate(span);
        for (int i = 0; i < 5; i++) {
            createChildSpan(i);
        }
        for (int i = 0; i < 5; i++) {
            createReferencingSpan(i);
        }
        for (int i = 0; i < 5; i++) {
            createIndependantSpan(i);
        }
        span.finish();
        return "spans created";
    }

    private void createChildSpan(int index) {
        Span span = tracer.buildSpan("child span")
                .withTag("mytag", "a child span")
                .withTag("index", index)
                .start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            sleep(10L * index);
        } catch (Exception ex) {
            Tags.ERROR.set(span, true);
            span.log(Map.of(Fields.EVENT, "error", Fields.ERROR_OBJECT, ex, Fields.MESSAGE, ex.getMessage()));
        }
    }

    private void createReferencingSpan(int index) {
        Span span = tracer.buildSpan("referencing span")
                .ignoreActiveSpan()
                .addReference(References.FOLLOWS_FROM, tracer.activeSpan().context())
                .withTag("mytag", "follows from")
                .withTag("index", index)
                .start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            sleep(10L * index);
        }
    }

    private void createIndependantSpan(int index) {
        Span span = tracer.buildSpan("independant span")
                .ignoreActiveSpan()
                .withTag("mytag", "independant")
                .withTag("index", index)
                .start();
        try (Scope scope = tracer.scopeManager().activate(span)) {
            sleep(10L * index);
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
