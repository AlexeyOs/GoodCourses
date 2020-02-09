package net.os.goodcourses.service.impl;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.repository.storage.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.os.goodcourses.service.FindCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FindCourseServiceImpl implements FindCourseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(FindCourseServiceImpl.class);

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Course findById(String id) {
        return courseRepository.findById(Long.parseLong(id));
    }

    @Override
    public Page<Course> findAll(Pageable pageable) {
        return courseRepository.findAll(pageable);
    }

}
