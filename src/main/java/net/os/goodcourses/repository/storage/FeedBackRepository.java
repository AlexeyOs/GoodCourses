package net.os.goodcourses.repository.storage;

import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.repository.projection.CourseRatingProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    @EntityGraph(attributePaths = "course")
    Page<FeedBack> findAllByProfileIdOrderByStartDateDesc(long profileId, Pageable pageable);

    @EntityGraph(attributePaths = "course")
    Optional<FeedBack> findByIdAndProfileId(long feedbackId, long profileId);

    boolean existsByCourseIdAndProfileId(long courseId, long profileId);

    @Query("select avg(f.rating) as average, count(f.rating) as count " +
            "from FeedBack f where f.course.id = :courseId and f.rating is not null")
    CourseRatingProjection findRatingSummaryByCourseId(@Param("courseId") long courseId);
}
