package com.xandecoelho5.restwithspringerudio.service;

import com.xandecoelho5.restwithspringerudio.controller.PersonController;
import com.xandecoelho5.restwithspringerudio.data.vo.v1.PersonVO;
import com.xandecoelho5.restwithspringerudio.data.vo.v2.PersonVOV2;
import com.xandecoelho5.restwithspringerudio.exception.RequiredObjectIsNullException;
import com.xandecoelho5.restwithspringerudio.exception.ResourceNotFoundException;
import com.xandecoelho5.restwithspringerudio.mapper.custom.PersonMapper;
import com.xandecoelho5.restwithspringerudio.model.Person;
import com.xandecoelho5.restwithspringerudio.repository.PersonRepository;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.xandecoelho5.restwithspringerudio.mapper.CustomModelMapper.parseObject;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private PersonMapper mapper;

    @Autowired
    private PagedResourcesAssembler<PersonVO> assembler;

//    private ModelMapper modelMapper;

//    public PersonService() {
//        TypeMap<Person, PersonVO> propertyMapper = modelMapper.createTypeMap(Person.class, PersonVO.class);
//        propertyMapper.addMapping(Person::getId, PersonVO::setKey);
//    }

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

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        var personPage = repository.findAll(pageable);
        return getEntitiesWithHateoasLinks(pageable, personPage);
    }

    public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
        var personPage = repository.findPersonsByName(firstName, pageable);
        return getEntitiesWithHateoasLinks(pageable, personPage);
    }

    @NotNull
    private PagedModel<EntityModel<PersonVO>> getEntitiesWithHateoasLinks(Pageable pageable, Page<Person> personPage) {
        var personsVOsPage = personPage.map(p -> parseObject(p, PersonVO.class))
                .map(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));

        var link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personsVOsPage, link);
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

    @Transactional
    public PersonVO disablePerson(Long id) {
        repository.disablePerson(id);
        return findById(id);
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private Person getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }
}
