package com.project.course.backend.service;

import com.project.course.backend.common.component.DtoEntityMapperTest;
import com.project.course.backend.module.course.CourseEntity;
import com.project.course.backend.module.course.repository.CourseRepository;
import com.project.course.backend.module.course.CourseResponse;
import com.project.course.backend.module.course.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @InjectMocks
    private CourseService courseService;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private DtoEntityMapperTest dtoEntityMapperTest;

    @BeforeEach
    void setUp() {
        // This method is called before each test to set up the mocks and the service
        // No specific setup needed for this test class
    }

    @Test
    void test_getAllCourses_shouldReturn_data() {
        List<CourseEntity> mockCourseEntities = new ArrayList<>();
        CourseEntity courseEntity = new CourseEntity();
        mockCourseEntities.add(courseEntity);

        List<CourseResponse> mockCourseListResponse = new ArrayList<>();
        CourseResponse courseResponse = CourseResponse.builder().build();
        mockCourseListResponse.add(courseResponse);

        when(courseRepository.findAll()).thenReturn(mockCourseEntities);
        when(dtoEntityMapperTest.mapListToDto(mockCourseEntities, CourseResponse.class)).thenReturn(mockCourseListResponse);
        List<CourseResponse> courseResponseList = courseService.getAllCourses();

        List<CourseResponse> expectResult = List.of(
                CourseResponse.builder().build()
        );

        verify(courseRepository, times(1)).findAll();
        Assertions.assertNotEquals(0, courseResponseList.size());
        Assertions.assertEquals(expectResult, courseResponseList);
    }

    @Test
    void test_something() {

    }


}
