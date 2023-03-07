package com.xandecoelho5.restwithspringerudio.integrationtests.repository;

import com.xandecoelho5.restwithspringerudio.integrationtests.testcontainer.AbstractIntegrationTest;
import com.xandecoelho5.restwithspringerudio.model.Person;
import com.xandecoelho5.restwithspringerudio.repository.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository repository;

    private static Person person;

    @BeforeAll
    static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
    void testFindByName() {
        var pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonsByName("ilb", pageable).getContent().get(0);

        assertTrue(person.getEnabled());
        assertEquals(2, person.getId());
        assertEquals("Gilbert", person.getFirstName());
        assertEquals("Rands", person.getLastName());
        assertEquals("868 Evergreen Trail", person.getAddress());
        assertEquals("Male", person.getGender());
    }

    @Test
    @Order(2)
    void testDisablePerson() {
        repository.disablePerson(person.getId());
        var pageable = PageRequest.of(0, 6, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findPersonsByName("ilb", pageable).getContent().get(0);

        assertFalse(person.getEnabled());
        assertEquals(2, person.getId());
        assertEquals("Gilbert", person.getFirstName());
        assertEquals("Rands", person.getLastName());
        assertEquals("868 Evergreen Trail", person.getAddress());
        assertEquals("Male", person.getGender());
    }
}
