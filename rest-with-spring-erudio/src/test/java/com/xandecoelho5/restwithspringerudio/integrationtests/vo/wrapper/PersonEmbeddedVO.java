package com.xandecoelho5.restwithspringerudio.integrationtests.vo.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xandecoelho5.restwithspringerudio.integrationtests.vo.PersonVO;

import java.util.List;

public class PersonEmbeddedVO {

    @JsonProperty("personVOList")
    private List<PersonVO> persons;

    public PersonEmbeddedVO() {
    }

    public List<PersonVO> getPersons() {
        return persons;
    }

    public void setPersons(List<PersonVO> persons) {
        this.persons = persons;
    }
}
