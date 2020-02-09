package net.os.goodcourses.repository.storage;

import net.os.goodcourses.entity.Course;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface CourseRepository extends PagingAndSortingRepository<Course, Long> {

    Course findById(long id);

    Course findByPlatform(String platform);

    Course findByAuthor(String author);

    Course findBySubjectOfStudy(String subjectOfStudy);

}
