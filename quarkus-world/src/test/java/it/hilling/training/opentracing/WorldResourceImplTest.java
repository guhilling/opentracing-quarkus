package it.hilling.training.opentracing;

import io.agroal.api.AgroalDataSource;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingConsumer;

import javax.inject.Inject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.defaultParser;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest()
@QuarkusTestResource(H2DatabaseTestResource.class)
public class WorldResourceImplTest {

    private static final List<String> setupStatements = new ArrayList<>();


    static {
        Path sqlFile  = Path.of("", "src/test/resources/db-init").resolve("create-user.sql");
        try {
            String content = Files.readString(sqlFile);
            for(String part: content.split(";")) {
                setupStatements.add(part);
            }
        } catch (IOException e) {
            throw new RuntimeException("setup failed", e);
        }
    }

    @Inject
    AgroalDataSource dataSource;

    @BeforeEach
    public void fillDataBase() throws SQLException {
        try(Connection connection = dataSource.getConnection()) {
            setupStatements.forEach(statement -> {
                try {
                    connection.createStatement().execute(statement);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/world")
          .then()
             .statusCode(200)
             .body(is("WORLD"));
    }

}