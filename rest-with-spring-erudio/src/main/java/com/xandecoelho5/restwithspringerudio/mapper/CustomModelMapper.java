package com.xandecoelho5.restwithspringerudio.mapper;

import org.modelmapper.ModelMapper;

import java.util.List;

public class CustomModelMapper {

    private CustomModelMapper() {
    }

    private static final ModelMapper mapper = new ModelMapper();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        return origin.stream().map(o -> mapper.map(o, destination)).toList();
    }
}
