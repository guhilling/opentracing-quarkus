package it.hilling.training.opentracing;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@Disabled
@QuarkusTest()
@QuarkusTestResource(H2DatabaseTestResource.class)
public class WorldResourceImplTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/world")
          .then()
             .statusCode(200)
             .body(is("WORLD"));
    }

}