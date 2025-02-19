package it.hilling.training.opentracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/demo")
public class DemoResource {

    @Inject
    Tracer tracer;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String demo() {
        Span span = tracer.spanBuilder("demo span")
                          .startSpan();
        span.makeCurrent();
        for (int i = 0; i < 5; i++) {
            createChildSpan(i);
        }
        for (int i = 0; i < 5; i++) {
            createReferencingSpan(i);
        }
        for (int i = 0; i < 5; i++) {
            createIndependantSpan(i);
        }
        span.end();
        return "spans created";
    }

    private void createChildSpan(int index) {
        Span span = tracer.spanBuilder("child span")
                    .setAttribute("mytag", "a child span")
                    .setAttribute("index", index)
                    .startSpan();
        try (Scope scope = span.makeCurrent()) {
            sleep(10L * index);
        } catch (Exception ex) {
            span.setStatus(StatusCode.ERROR);
            span.recordException(ex);
        }
    }

    private void createReferencingSpan(int index) {
        Span span = tracer.spanBuilder("referencing span")
                    .setNoParent()
                    .addLink(Span.current().getSpanContext())
                    .setAttribute("mytag", "follows from")
                    .setAttribute("index", index)
                    .startSpan();
        try (Scope scope = span.makeCurrent()) {
            sleep(10L * index);
        }
    }

    private void createIndependantSpan(int index) {
        Span span = tracer.spanBuilder("independant span")
                    .setNoParent()
                    .setAttribute("mytag", "independant")
                    .setAttribute("index", index)
                    .startSpan();
        try (Scope scope = span.makeCurrent()) {
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
