package net.os.goodcourses.service.impl;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.service.AddCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

@Service
public class AddCourseServiceImpl implements AddCourseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddCourseServiceImpl.class);

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
        course.setLink(validateLink(link));
        courseRepository.save(course);
    }

    private String validateLink(String link) {
        if (link != null) {
            try {
                URI uri = new URI(link.strip());
                if (uri.getHost() != null && uri.getUserInfo() == null
                        && ("https".equalsIgnoreCase(uri.getScheme()) || "http".equalsIgnoreCase(uri.getScheme()))) {
                    return uri.toASCIIString();
                }
            } catch (URISyntaxException ignored) {
                // Fall through to the same user-facing validation response.
            }
        }
        throw new IllegalArgumentException("Укажите ссылку на курс с http:// или https://.");
    }

}
