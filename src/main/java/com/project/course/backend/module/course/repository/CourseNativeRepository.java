package com.project.course.backend.module.course.repository;

import com.project.course.backend.module.course.CourseUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CourseNativeRepository {

    private final JdbcTemplate jdbcTemplate;

    public List<CourseUserDTO> getAllCourses() {
        String sql = """
                        SELECT c.course_id,
                            c.course_name,
                            c.description,
                            c.price,
                            c.image,
                            u.user_id,
                            concat(u.first_name, ' ', u.last_name) as full_name,
                            cc.category_id,
                            cc.category_name,
                            c.created_at,
                            c.updated_at
                    FROM courses c
                    LEFT JOIN
                    users u ON c.user_id = u.user_id
                    LEFT JOIN categories_course cc
                    ON c.category_id = cc.category_id
                """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            CourseUserDTO c = new CourseUserDTO();
            c.setCourseId(rs.getLong("course_id"));
            c.setCourseName(rs.getString("course_name"));
            c.setDescription(rs.getString("description"));
            c.setPrice(rs.getBigDecimal("price"));
            c.setImage(rs.getString("image"));
            c.setUserId(rs.getLong("user_id"));
            c.setFullName(rs.getString("full_name"));
            c.setCategoryId(rs.getLong("category_id"));
            c.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            c.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            // Map other fields as necessary
            return c;
        });

    }



}
