package com.project.course.backend.module.course;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseUserDTO {

    private Long courseId;
    private String courseName;
    private String description;
    private BigDecimal price;
    private String image;
    private Long userId;
    private String fullName;
    private Long categoryId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
