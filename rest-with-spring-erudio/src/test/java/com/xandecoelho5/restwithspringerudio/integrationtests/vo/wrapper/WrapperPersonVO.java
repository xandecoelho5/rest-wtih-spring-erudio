package com.xandecoelho5.restwithspringerudio.integrationtests.vo.wrapper;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class WrapperPersonVO {

    @JsonProperty("_embedded")
    private PersonEmbeddedVO embedded;

    public WrapperPersonVO() {
    }

    public PersonEmbeddedVO getEmbedded() {
        return embedded;
    }

    public void setEmbedded(PersonEmbeddedVO embedded) {
        this.embedded = embedded;
    }
}
