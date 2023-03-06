package com.xandecoelho5.restwithspringerudio.mapper;

import com.github.dozermapper.core.DozerBeanMapperBuilder;
import com.github.dozermapper.core.Mapper;

import java.util.List;

public class DozerMapper {

    private DozerMapper() {
    }

    private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        return origin.stream().map(o -> mapper.map(o, destination)).toList();
    }
}
