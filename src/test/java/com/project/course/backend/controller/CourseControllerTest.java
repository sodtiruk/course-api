package com.project.course.backend.controller;

import com.project.course.backend.common.dto.BaseResponse;
import com.project.course.backend.module.course.CourseController;
import com.project.course.backend.module.course.CourseResponse;
import com.project.course.backend.module.course.CourseService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

    @InjectMocks
    private CourseController courseController;

    @Mock
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        // something
    }

    @Test
    void test_getAllCourse_ShouldReturnCourseResponse() {
        when(courseService.getAllCourses()).thenReturn(List.of(CourseResponse.builder().build()));
        ResponseEntity<BaseResponse<List<CourseResponse>>> result = courseController.getAllCourses();

        //expect response
        List<CourseResponse> courseResponse = List.of(CourseResponse.builder().build());

        // Assert
        Assertions.assertNotNull(result.getBody());
        assertEquals(courseResponse, result.getBody().getData());
        assertEquals("success", result.getBody().getMessage());
        assertEquals(200, result.getBody().getStatus());
    }


}
