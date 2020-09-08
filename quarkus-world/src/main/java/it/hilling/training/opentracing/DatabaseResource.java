package it.hilling.training.opentracing;

import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;

@RequestScoped
public class DatabaseResource {
    @Traced(operationName = "db_access")
    public String translate(String arg) {
        return arg.toUpperCase();
    }
}
