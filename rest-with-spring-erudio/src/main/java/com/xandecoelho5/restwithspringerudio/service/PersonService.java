package com.xandecoelho5.restwithspringerudio.service;

import com.xandecoelho5.restwithspringerudio.data.vo.v1.PersonVO;
import com.xandecoelho5.restwithspringerudio.data.vo.v2.PersonVOV2;
import com.xandecoelho5.restwithspringerudio.exception.ResourceNotFoundException;
import com.xandecoelho5.restwithspringerudio.mapper.DozerMapper;
import com.xandecoelho5.restwithspringerudio.mapper.custom.PersonMapper;
import com.xandecoelho5.restwithspringerudio.model.Person;
import com.xandecoelho5.restwithspringerudio.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.xandecoelho5.restwithspringerudio.mapper.DozerMapper.parseListObjects;
import static com.xandecoelho5.restwithspringerudio.mapper.DozerMapper.parseObject;

@Service
public class PersonService {

    @Autowired
    private PersonRepository repository;
    @Autowired
    private PersonMapper mapper;

    public PersonVO create(PersonVO person) {
        var entity = parseObject(person, Person.class);
        return parseObject(repository.save(entity), PersonVO.class);
    }

    public PersonVOV2 createV2(PersonVOV2 person) {
        var entity = mapper.convertVoToEntity(person);
        return mapper.convertEntityToVo(repository.save(entity));
    }

    public List<PersonVO> findAll() {
        return parseListObjects(repository.findAll(), PersonVO.class);
    }

    public PersonVO findById(Long id) {
        return parseObject(getById(id), PersonVO.class);
    }

    public PersonVO update(PersonVO person) {
        Person entity = getById(person.getId());

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return parseObject(repository.save(entity), PersonVO.class);
    }

    public void delete(Long id) {
        repository.delete(getById(id));
    }

    private Person getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID"));
    }
}
