package com.project.course.backend.module.course;

import com.project.course.backend.common.dto.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.project.course.backend.common.constant.CommonConstant.ErrorMessage.SUCCESS;
import static com.project.course.backend.common.constant.CommonConstant.StatusCode.SUCCESS_CODE;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<CourseResponse>>> getAllCourses() {
        return ResponseEntity.ok(BaseResponse.<List<CourseResponse>>builder()
                .data(courseService.getAllCourses())
                .message(SUCCESS)
                .status(SUCCESS_CODE)
                .build());
    }



}
