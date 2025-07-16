package com.project.course.backend.common.component;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DtoEntityMapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    private DtoEntityMapper() {}

    static {
        // ตั้งค่า Matching Strategy ให้ Strict (ตรงกับชื่อฟิลด์เป๊ะ)
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public static <D, E> E mapToEntity(D dto, Class<E> entityClass) {
        return modelMapper.map(dto, entityClass);
    }

    public static <E, D> D mapToDto(E entity, Class<D> dtoClass) {
        return modelMapper.map(entity, dtoClass);
    }

    public static <D, E> List<E> mapListToEntity(List<D> dtoList, Class<E> entityClass) {
        return dtoList.stream()
                .map(dto -> mapToEntity(dto, entityClass))
                .toList();
    }

    public static <E, D> List<D> mapListToDto(List<E> entityList, Class<D> dtoClass) {
        return entityList.stream()
                .map(entity -> mapToDto(entity, dtoClass))
                .toList();
    }


}
