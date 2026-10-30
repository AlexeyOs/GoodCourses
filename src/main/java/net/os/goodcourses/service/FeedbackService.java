package net.os.goodcourses.service;

import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.form.FeedbackForm;
import net.os.goodcourses.model.CourseRatingSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FeedbackService {

    Page<FeedBack> findMyReviews(long profileId, Pageable pageable);

    FeedBack create(long profileId, long courseId, FeedbackForm form);

    FeedBack findOwned(long profileId, long feedbackId);

    FeedBack update(long profileId, long feedbackId, FeedbackForm form);

    void delete(long profileId, long feedbackId);

    CourseRatingSummary getCourseRatingSummary(long courseId);
}
