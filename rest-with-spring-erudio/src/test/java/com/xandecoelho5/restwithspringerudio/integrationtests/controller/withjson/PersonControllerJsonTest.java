package com.xandecoelho5.restwithspringerudio.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xandecoelho5.restwithspringerudio.config.TestConfig;
import com.xandecoelho5.restwithspringerudio.integrationtests.testcontainer.AbstractIntegrationTest;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.AccountCredentialsVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.PersonVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.TokenVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.common.mapper.TypeRef;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonVO();
    }

    @Test
    @Order(0)
    void authorization() {
        var credentials = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(credentials)
                .when().post()
                .then().statusCode(200)
                .extract()
                .body().as(TokenVO.class).getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfig.HEADER_AUTHORIZATION, "Bearer " + accessToken)
                .setBasePath("/api/person/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when().post()
                .then().statusCode(200)
                .extract().body().asString();
        var persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());
        assertTrue(persistedPerson.getId() > 0);
        assertEquals("Xande", persistedPerson.getFirstName());
        assertEquals("Coelho", persistedPerson.getLastName());
        assertEquals("Rua 1, 123", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(2)
    void testUpdate() throws JsonProcessingException {
        person.setLastName("Coelho2");

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .body(person)
                .when().put()
                .then().statusCode(200)
                .extract().body().asString();
        var persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertTrue(persistedPerson.getEnabled());
        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Xande", persistedPerson.getFirstName());
        assertEquals("Coelho2", persistedPerson.getLastName());
        assertEquals("Rua 1, 123", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(3)
    void testDisableById() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParams("id", person.getId())
                .when().patch("{id}")
                .then().statusCode(200)
                .extract().body().asString();
        var persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());
        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Xande", persistedPerson.getFirstName());
        assertEquals("Coelho2", persistedPerson.getLastName());
        assertEquals("Rua 1, 123", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(4)
    void testFindById() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .header(TestConfig.HEADER_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParams("id", person.getId())
                .when().get("{id}")
                .then().statusCode(200)
                .extract().body().asString();
        var persistedPerson = objectMapper.readValue(content, PersonVO.class);
        person = persistedPerson;

        assertNotNull(persistedPerson);
        assertNotNull(persistedPerson.getId());
        assertNotNull(persistedPerson.getFirstName());
        assertNotNull(persistedPerson.getLastName());
        assertNotNull(persistedPerson.getAddress());
        assertNotNull(persistedPerson.getGender());
        assertFalse(persistedPerson.getEnabled());
        assertEquals(person.getId(), persistedPerson.getId());
        assertEquals("Xande", persistedPerson.getFirstName());
        assertEquals("Coelho2", persistedPerson.getLastName());
        assertEquals("Rua 1, 123", persistedPerson.getAddress());
        assertEquals("Male", persistedPerson.getGender());
    }

    @Test
    @Order(5)
    void testDelete() {
        given().spec(specification)
                .pathParams("id", person.getId())
                .when().delete("{id}")
                .then().statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .when().get()
                .then().statusCode(200)
//                .extract().body().as(new TypeRef<List<PersonVO>>() {});
                .extract().body().asString();

        var people = objectMapper.readValue(content, new TypeReference<List<PersonVO>>() {
        });

        var foundPersonOne = people.get(0);
        assertNotNull(foundPersonOne.getId());
        assertNotNull(foundPersonOne.getFirstName());
        assertNotNull(foundPersonOne.getLastName());
        assertNotNull(foundPersonOne.getAddress());
        assertNotNull(foundPersonOne.getGender());
        assertTrue(foundPersonOne.getEnabled());
        assertEquals(1, foundPersonOne.getId());
        assertEquals("Ayrton", foundPersonOne.getFirstName());
        assertEquals("Senna", foundPersonOne.getLastName());
        assertEquals("São Paulo", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());

        var foundPersonSix = people.get(5);
        assertNotNull(foundPersonSix.getId());
        assertNotNull(foundPersonSix.getFirstName());
        assertNotNull(foundPersonSix.getLastName());
        assertNotNull(foundPersonSix.getAddress());
        assertNotNull(foundPersonSix.getGender());
        assertTrue(foundPersonSix.getEnabled());
        assertEquals(9, foundPersonSix.getId());
        assertEquals("Nelson", foundPersonSix.getFirstName());
        assertEquals("Mvezo", foundPersonSix.getLastName());
        assertEquals("Mvezo - South Africa", foundPersonSix.getAddress());
        assertEquals("Male", foundPersonSix.getGender());
    }

    @Test
    @Order(7)
    void testFindAllWithoutToken() {
        var specificationWithoutToken = new RequestSpecBuilder()
                .setBasePath("/api/person/v1")
                .setPort(TestConfig.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        given().spec(specificationWithoutToken)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .when().get()
                .then().statusCode(403);
    }


    private void mockPerson() {
        person.setFirstName("Xande");
        person.setLastName("Coelho");
        person.setAddress("Rua 1, 123");
        person.setGender("Male");
        person.setEnabled(true);
    }
}