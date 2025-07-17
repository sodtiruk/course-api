package com.project.course.backend.module.course;

import com.project.course.backend.common.component.DtoEntityMapperTest;
import com.project.course.backend.module.course.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final DtoEntityMapperTest dtoEntityMapperTest;

    public List<CourseResponse> getAllCourses() {
        List<CourseEntity> courseEntities = courseRepository.findAll();
        return dtoEntityMapperTest.mapListToDto(courseEntities, CourseResponse.class);
    }





}
