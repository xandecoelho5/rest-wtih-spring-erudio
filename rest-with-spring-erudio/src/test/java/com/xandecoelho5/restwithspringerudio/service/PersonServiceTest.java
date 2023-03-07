package com.xandecoelho5.restwithspringerudio.service;

import com.xandecoelho5.restwithspringerudio.data.vo.v1.PersonVO;
import com.xandecoelho5.restwithspringerudio.exception.RequiredObjectIsNullException;
import com.xandecoelho5.restwithspringerudio.mapper.mocks.MockPerson;
import com.xandecoelho5.restwithspringerudio.model.Person;
import com.xandecoelho5.restwithspringerudio.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    private MockPerson input;

    @InjectMocks
    private PersonService service;

    @Mock
    private PersonRepository repository;

    @BeforeEach
    void setUpMocks() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void create() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.save(entity)).thenReturn(entity);
        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void createWithNullPerson() {
        var exception = assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }

    @Test
    void findById() {
        Person entity = input.mockEntity(1);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        var result = service.findById(1L);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void update() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(entity);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Addres Test1", result.getAddress());
        assertEquals("First Name Test1", result.getFirstName());
        assertEquals("Last Name Test1", result.getLastName());
        assertEquals("Female", result.getGender());
    }

    @Test
    void updateWithNullPerson() {
        var exception = assertThrows(RequiredObjectIsNullException.class, () -> service.update(null));
        assertEquals("It is not allowed to persist a null object!", exception.getMessage());
    }

    @Test
    void delete() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}
