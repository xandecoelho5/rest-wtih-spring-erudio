package com.xandecoelho5.restwithspringerudio.integrationtests.controller.withyaml;

import com.xandecoelho5.restwithspringerudio.config.TestConfig;
import com.xandecoelho5.restwithspringerudio.integrationtests.controller.withyaml.mapper.YMLMapper;
import com.xandecoelho5.restwithspringerudio.integrationtests.testcontainer.AbstractIntegrationTest;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.AccountCredentialsVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.PersonVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.TokenVO;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.pagedmodels.PagedModelPerson;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.wrapper.WrapperPersonVO;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerYamlTest extends AbstractIntegrationTest {

    private static RequestSpecification specification;
    private static YMLMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    static void setup() {
        objectMapper = new YMLMapper();
        person = new PersonVO();
    }

    @Test
    @Order(0)
    void authorization() {
        var credentials = new AccountCredentialsVO("leandro", "admin123");

        var accessToken = given()
                .config(getRestAssuredConfig())
                .basePath("/auth/signin")
                .port(TestConfig.SERVER_PORT)
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .body(credentials, objectMapper)
                .when().post()
                .then().statusCode(200)
                .extract()
                .body().as(TokenVO.class, objectMapper).getAccessToken();

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
    void testCreate() {
        mockPerson();

        var persistedPerson = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .body(person, objectMapper)
                .when().post()
                .then().statusCode(200)
                .extract()
                .body().as(PersonVO.class, objectMapper);

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
    void testUpdate() {
        person.setLastName("Coelho2");

        var persistedPerson = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .body(person, objectMapper)
                .when().put()
                .then().statusCode(200)
                .extract()
                .body().as(PersonVO.class, objectMapper);

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
    void testDisablePerson() {
        var persistedPerson = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .header(TestConfig.HEADER_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParams("id", person.getId())
                .when().patch("{id}")
                .then().statusCode(200)
                .extract()
                .body().as(PersonVO.class, objectMapper);

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
    void testFindById() {
        mockPerson();

        var persistedPerson = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .header(TestConfig.HEADER_ORIGIN, TestConfig.ORIGIN_ERUDIO)
                .pathParams("id", person.getId())
                .when().get("{id}")
                .then().statusCode(200)
                .extract()
                .body().as(PersonVO.class, objectMapper);

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
                .config(getRestAssuredConfig())
                .pathParams("id", person.getId())
                .when().delete("{id}")
                .then().statusCode(204);
    }

    @Test
    @Order(6)
    void testFindAll() {
        var wrapper = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .accept(TestConfig.CONTENT_TYPE_YML)
                .when().get()
                .then().statusCode(200)
                .extract()
                .body().as(PagedModelPerson.class, objectMapper);

        var people = wrapper.getContent();

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
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .when().get()
                .then().statusCode(403);
    }

    @Test
    @Order(8)
    void testFindByName() {
        var wrapper = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .pathParam("firstName", "ayr")
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .accept(TestConfig.CONTENT_TYPE_YML)
                .when().get("/findPersonByName/{firstName}")
                .then().statusCode(200)
                .extract()
                .body().as(PagedModelPerson.class, objectMapper);

        var people = wrapper.getContent();

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
    }

    @Test
    @Order(9)
    void testHATEOAS() {
        var unthreatedContent = given().spec(specification)
                .config(getRestAssuredConfig())
                .contentType(TestConfig.CONTENT_TYPE_YML)
                .accept(TestConfig.CONTENT_TYPE_YML)
                .queryParams("page", 3, "size", 10, "direction", "asc")
                .when().get()
                .then().statusCode(200)
                .extract().body().asString();

        var content = unthreatedContent.replace("\n", "").replace("\r", "");

        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/person/v1/677\""));
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/person/v1/846\""));
        assertTrue(content.contains("rel: \"self\"    href: \"http://localhost:8888/api/person/v1/714\""));

        assertTrue(content.contains("rel: \"first\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=0&size=10&sort=firstName,asc\""));
        assertTrue(content.contains("rel: \"prev\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=2&size=10&sort=firstName,asc\""));
        assertTrue(content.contains("rel: \"self\"  href: \"http://localhost:8888/api/person/v1?page=3&size=10&direction=asc\""));
        assertTrue(content.contains("rel: \"next\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=4&size=10&sort=firstName,asc\""));
        assertTrue(content.contains("rel: \"last\"  href: \"http://localhost:8888/api/person/v1?direction=asc&page=100&size=10&sort=firstName,asc\""));

        assertTrue(content.contains("page:  size: 10  totalElements: 1007  totalPages: 101  number: 3"));
    }


    private void mockPerson() {
        person.setFirstName("Xande");
        person.setLastName("Coelho");
        person.setAddress("Rua 1, 123");
        person.setGender("Male");
        person.setEnabled(true);
    }

    private RestAssuredConfig getRestAssuredConfig() {
        return RestAssuredConfig.config().encoderConfig(EncoderConfig.encoderConfig().encodeContentTypeAs(TestConfig.CONTENT_TYPE_YML, ContentType.TEXT));
    }
}
