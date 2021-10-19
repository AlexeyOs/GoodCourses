package net.os.goodcourses.service;



import net.os.goodcourses.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface FindCourseService {

    Optional<Course> findById(String uid);

    Page<Course> findAll(Pageable pageable);

    Page<Course> findByVisible(boolean visible, Pageable pageable);
}
