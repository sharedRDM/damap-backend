package at.medunigraz.damap.rest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import org.damap.base.rest.ConfigResource;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.junit.jupiter.api.Test;

@QuarkusTest
@TestHTTPEndpoint(ConfigResource.class)
class ConfigResourceTest {

  @ConfigProperty(name = "damap.auth.frontend.url")
  String authUrl;

  @Test
  void testGetConfigEndpoint() {
    given().when().get().then().statusCode(200).body("authUrl", is(authUrl));
  }
}
