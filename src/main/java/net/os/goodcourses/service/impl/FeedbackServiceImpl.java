package net.os.goodcourses.service.impl;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.exception.DuplicateFeedbackException;
import net.os.goodcourses.exception.ResourceNotFoundException;
import net.os.goodcourses.form.FeedbackForm;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.repository.storage.FeedBackRepository;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedBackRepository feedBackRepository;
    private final CourseRepository courseRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public FeedbackServiceImpl(FeedBackRepository feedBackRepository,
                               CourseRepository courseRepository,
                               ProfileRepository profileRepository) {
        this.feedBackRepository = feedBackRepository;
        this.courseRepository = courseRepository;
        this.profileRepository = profileRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FeedBack> findMyReviews(long profileId, Pageable pageable) {
        return feedBackRepository.findAllByProfileIdOrderByStartDateDesc(profileId, pageable);
    }

    @Override
    @Transactional
    public FeedBack create(long profileId, long courseId, FeedbackForm form) {
        if (feedBackRepository.existsByCourseIdAndProfileId(courseId, profileId)) {
            throw new DuplicateFeedbackException("Вы уже оставили отзыв на этот курс");
        }

        Profile profile = profileRepository.findById(profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Профиль не найден"));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Курс не найден"));

        FeedBack feedback = new FeedBack();
        feedback.setProfile(profile);
        feedback.setCourse(course);
        feedback.setDescription(normalizeDescription(form));
        feedback.setStartDate(new Timestamp(System.currentTimeMillis()));

        try {
            return feedBackRepository.saveAndFlush(feedback);
        } catch (DataIntegrityViolationException exception) {
            throw new DuplicateFeedbackException("Вы уже оставили отзыв на этот курс", exception);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public FeedBack findOwned(long profileId, long feedbackId) {
        return feedBackRepository.findByIdAndProfileId(feedbackId, profileId)
                .orElseThrow(() -> new ResourceNotFoundException("Отзыв не найден"));
    }

    @Override
    @Transactional
    public FeedBack update(long profileId, long feedbackId, FeedbackForm form) {
        FeedBack feedback = findOwned(profileId, feedbackId);
        feedback.setDescription(normalizeDescription(form));
        feedback.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        return feedBackRepository.save(feedback);
    }

    @Override
    @Transactional
    public void delete(long profileId, long feedbackId) {
        FeedBack feedback = findOwned(profileId, feedbackId);
        feedBackRepository.delete(feedback);
    }

    private String normalizeDescription(FeedbackForm form) {
        if (form == null || form.getDescription() == null || form.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Текст отзыва не может быть пустым");
        }
        String description = form.getDescription().trim();
        if (description.length() > 2000) {
            throw new IllegalArgumentException("Отзыв не должен превышать 2000 символов");
        }
        return description;
    }
}
