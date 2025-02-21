// package ch.hftm.blogproject;

// import java.util.Set;
// import org.junit.jupiter.api.Test;
// import io.quarkus.test.common.QuarkusTestResource;
// import io.quarkus.test.junit.QuarkusTest;
// import io.quarkus.test.oidc.server.OidcWiremockTestResource;
// import io.restassured.RestAssured;
// import io.smallrye.jwt.build.Jwt;
// import static org.hamcrest.Matchers.equalTo;


// @QuarkusTest
// @QuarkusTestResource(OidcWiremockTestResource.class)
// public class BearerTokenAuthorizationTest {

//     @Test
//     public void testBearerToken() {
//         RestAssured.given().auth().oauth2(getAccessToken("alice", Set.of("user")))
//             .when().get("/api/users/me")
//             .then()
//             .statusCode(200)
//             .body("userName", equalTo("alice"));
//     }

//     private String getAccessToken(String userName, Set<String> groups) {
//         return Jwt.preferredUserName(userName)
//                 .groups(groups)
//                 .issuer("http://localhost:8088/realms/blog") // Replace with your actual Keycloak realm issuer URL
//                 .audience("backend-service") // Replace with your actual Keycloak client ID
//                 .sign();
//     }
// }
