package net.os.goodcourses.service.impl;

import lombok.AllArgsConstructor;
import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.AddCourseService;
import net.os.goodcourses.util.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AddCourseServiceImpl implements AddCourseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddFeedBackServiceImpl.class);

    private final CourseRepository courseRepository;
    private final ProfileRepository profileRepository;


    @Override
    public void createNewCourse(String platform,
                                String author,
                                String subjectOfStudy,
                                String link,
                                String description) {
        Course course = new Course();
        course.setPlatform(platform);
        course.setAuthor(author);
        course.setSubjectOfStudy(subjectOfStudy);
        course.setLink(link);
        course.setDescription(description);
        Long id = SecurityUtil.getCurrentIdProfile();
        if (id != null) {
            Optional<Profile> profile = profileRepository.findById(id);
            profile.ifPresent(value -> course.getProfiles().add(value));
        }
        courseRepository.save(course);
    }


}
