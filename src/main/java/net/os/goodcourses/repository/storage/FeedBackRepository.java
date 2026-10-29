package net.os.goodcourses.repository.storage;

import net.os.goodcourses.entity.FeedBack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedBackRepository extends JpaRepository<FeedBack, Long> {

    @EntityGraph(attributePaths = "course")
    Page<FeedBack> findAllByProfileIdOrderByStartDateDesc(long profileId, Pageable pageable);

    @EntityGraph(attributePaths = "course")
    Optional<FeedBack> findByIdAndProfileId(long feedbackId, long profileId);

    boolean existsByCourseIdAndProfileId(long courseId, long profileId);
}
