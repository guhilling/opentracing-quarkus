package it.hilling.training.opentracing;

import io.agroal.api.AgroalDataSource;
import org.eclipse.microprofile.opentracing.Traced;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;


@RequestScoped
public class DatabaseResource {

    @Inject
    AgroalDataSource dataSource;

    @Traced(operationName = "db_access")
    public String translate(String arg) {
        try(Connection connection=dataSource.getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery("SELECT * FROM demo_user left join demo_group dg on demo_user.group_id = dg.group_id;");
            if(!resultSet.next()){
                throw new RuntimeException("no result");
            }
        } catch (Exception e) {
            throw new RuntimeException("error in query", e);
        }
        return arg.toUpperCase();
    }
}
