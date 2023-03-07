package com.xandecoelho5.restwithspringerudio.integrationtests.controller.withjson;

import com.xandecoelho5.restwithspringerudio.config.TestConfig;
import com.xandecoelho5.restwithspringerudio.integrationtests.testcontainer.AbstractIntegrationTest;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.AccountCredentialsVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.TokenVO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerJsonTest extends AbstractIntegrationTest {

    private static TokenVO tokenVO;

    @Test
    @Order(1)
    void testSignIn() {
        var credentials = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(credentials)
                .when().post()
                .then().statusCode(200)
                .extract()
                .body().as(TokenVO.class);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresh() {
        var newTokenVO = given()
                .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfig.HEADER_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when().put("/{username}")
                .then().statusCode(200)
                .extract()
                .body().as(TokenVO.class);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }

}
