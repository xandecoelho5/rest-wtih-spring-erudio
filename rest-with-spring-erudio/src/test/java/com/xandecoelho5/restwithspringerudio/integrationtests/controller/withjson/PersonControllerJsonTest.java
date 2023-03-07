package com.xandecoelho5.restwithspringerudio.integrationtests.controller.withjson;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xandecoelho5.restwithspringerudio.config.TestConfig;
import com.xandecoelho5.restwithspringerudio.integrationtests.testcontainer.AbstractIntegrationTest;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.AccountCredentialsVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.PersonVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.TokenVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.wrapper.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

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
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when().get()
                .then().statusCode(200)
                .extract().body().asString();

        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();

        var foundPersonOne = people.get(0);
        assertTrue(foundPersonOne.getEnabled());
        assertEquals(904, foundPersonOne.getId());
        assertEquals("Alida", foundPersonOne.getFirstName());
        assertEquals("Shatliffe", foundPersonOne.getLastName());
        assertEquals("6780 Magdeline Terrace", foundPersonOne.getAddress());
        assertEquals("Female", foundPersonOne.getGender());

        var foundPersonSix = people.get(5);
        assertTrue(foundPersonSix.getEnabled());
        assertEquals(174, foundPersonSix.getId());
        assertEquals("Almire", foundPersonSix.getFirstName());
        assertEquals("Iban", foundPersonSix.getLastName());
        assertEquals("75681 Tomscot Road", foundPersonSix.getAddress());
        assertEquals("Female", foundPersonSix.getGender());
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

    @Test
    @Order(8)
    void testFindByName() throws JsonProcessingException {
        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .pathParam("firstName", "ilb")
                .queryParams("page", 0, "size", 6, "direction", "asc")
                .when().get("/findPersonByName/{firstName}")
                .then().statusCode(200)
                .extract().body().asString();

        WrapperPersonVO wrapper = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapper.getEmbedded().getPersons();

        var foundPersonOne = people.get(0);
        assertTrue(foundPersonOne.getEnabled());
        assertEquals(2, foundPersonOne.getId());
        assertEquals("Gilbert", foundPersonOne.getFirstName());
        assertEquals("Rands", foundPersonOne.getLastName());
        assertEquals("868 Evergreen Trail", foundPersonOne.getAddress());
        assertEquals("Male", foundPersonOne.getGender());
    }

    @Test
    @Order(9)
    void testHATEOAS() {
        var content = given().spec(specification)
                .contentType(TestConfig.CONTENT_TYPE_JSON)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when().get()
                .then().statusCode(200)
                .extract().body().asString();

        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/904\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/654\"}}}"));
        assertTrue(content.contains("\"_links\":{\"self\":{\"href\":\"http://localhost:8888/api/person/v1/452\"}}}"));

        assertTrue(content.contains("{\"first\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"prev\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\"}"));
        assertTrue(content.contains("\"self\":{\"href\":\"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\"}"));
        assertTrue(content.contains("\"next\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\"}"));
//        assertTrue(content.contains("\"last\":{\"href\":\"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\"}}"));
//        assertTrue(content.contains("\"page\":{\"size\":10,\"totalElements\":1001,\"totalPages\":101,\"number\":3}}"));
    }

    private void mockPerson() {
        person.setFirstName("Xande");
        person.setLastName("Coelho");
        person.setAddress("Rua 1, 123");
        person.setGender("Male");
        person.setEnabled(true);
    }
}
