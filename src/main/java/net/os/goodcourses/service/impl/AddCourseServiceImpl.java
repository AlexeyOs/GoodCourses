package net.os.goodcourses.service.impl;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.service.AddCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddCourseServiceImpl implements AddCourseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddFeedBackServiceImpl.class);

    @Autowired
    CourseRepository courseRepository;

    @Override
    public void createNewCourse(String platform,
                                String author,
                                String subjectOfStudy,
                                String link) {
        Course course = new Course();
        course.setPlatform(platform);
        course.setAuthor(author);
        course.setSubjectOfStudy(subjectOfStudy);
        course.setLink(link);
        courseRepository.save(course);
    }


}
