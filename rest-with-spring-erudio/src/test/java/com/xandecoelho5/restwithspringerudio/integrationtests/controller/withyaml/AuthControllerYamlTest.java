package com.xandecoelho5.restwithspringerudio.integrationtests.controller.withyaml;

import com.xandecoelho5.restwithspringerudio.config.TestConfig;
import com.xandecoelho5.restwithspringerudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.xandecoelho5.restwithspringerudio.integrationtests.testcontainer.AbstractIntegrationTest;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.AccountCredentialsVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.TokenVO;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerYamlTest extends AbstractIntegrationTest {

    private static YMLMapper objectMapper;
    private static TokenVO tokenVO;

    @BeforeAll
    static void setUp() {
        objectMapper = new YMLMapper();
    }

    @Test
    @Order(1)
    void testSignIn() {
        var credentials = new AccountCredentialsVO("leandro", "admin123");

        tokenVO = given()
                .config(getRestAssuredConfig())
                .accept(TestConfig.CONTENT_TYPE_YML)
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .body(credentials, objectMapper)
                .when().post()
                .then().statusCode(200)
                .extract()
                .body().as(TokenVO.class, objectMapper);

        assertNotNull(tokenVO.getAccessToken());
        assertNotNull(tokenVO.getRefreshToken());
    }

    @Test
    @Order(2)
    void testRefresh() {
        var newTokenVO = given()
                .config(getRestAssuredConfig())
                .basePath("/auth/refresh")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .pathParam("username", tokenVO.getUsername())
                .header(TestConfig.HEADER_AUTHORIZATION, "Bearer " + tokenVO.getRefreshToken())
                .when().put("{username}")
                .then().statusCode(200)
                .extract()
                .body().as(TokenVO.class, objectMapper);

        assertNotNull(newTokenVO.getAccessToken());
        assertNotNull(newTokenVO.getRefreshToken());
    }

    private RestAssuredConfig getRestAssuredConfig() {
        return RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT));
    }
}
