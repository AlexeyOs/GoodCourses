package net.os.goodcourses.repository.storage;

import net.os.goodcourses.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.List;


public interface CourseRepository extends JpaRepository<Course, Long> {

    Optional<Course> findById(long id);

    Course findByPlatform(String platform);

    Course findByAuthor(String author);

    Course findBySubjectOfStudy(String subjectOfStudy);

    Page<Course> findByVisible(boolean visible, Pageable pageable);

    List<Course> findByVisibleOrderByIdAsc(boolean visible);

}
