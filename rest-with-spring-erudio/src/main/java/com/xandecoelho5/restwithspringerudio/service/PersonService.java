package com.xandecoelho5.restwithspringerudio.service;

import com.xandecoelho5.restwithspringerudio.controller.PersonController;
import com.xandecoelho5.restwithspringerudio.data.vo.v1.PersonVO;
import com.xandecoelho5.restwithspringerudio.data.vo.v2.PersonVOV2;
import com.xandecoelho5.restwithspringerudio.exception.RequiredObjectIsNullException;
import com.xandecoelho5.restwithspringerudio.exception.ResourceNotFoundException;
import com.xandecoelho5.restwithspringerudio.mapper.custom.PersonMapper;
import com.xandecoelho5.restwithspringerudio.model.Person;
import com.xandecoelho5.restwithspringerudio.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xandecoelho5.restwithspringerudio.mapper.DozerMapper.parseListObjects;
import static com.xandecoelho5.restwithspringerudio.mapper.DozerMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private PersonMapper mapper;

    public PersonVO create(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        var entity = parseObject(person, Person.class);
        var vo = parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        var entity = mapper.convertVoToEntity(person);
        return mapper.convertEntityToVo(repository.save(entity));
    }

    public List<PersonVO> findAll() {
        var persons = parseListObjects(repository.findAll(), PersonVO.class);
        persons.forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return persons;
    }

    public PersonVO findById(Long id) {
        var vo = parseObject(getById(id), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNullException();

        Person entity = getById(person.getKey());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = parseObject(getById(person.getKey()), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private Person getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }
}
