package net.os.goodcourses.repository.storage;

import net.os.goodcourses.entity.Course;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;


public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

    Optional<Course> findById(long id);

    Course findByPlatform(String platform);

    Course findByAuthor(String author);

    Course findBySubjectOfStudy(String subjectOfStudy);

}
