package net.os.goodcourses.service;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.entity.FeedBack;
import net.os.goodcourses.entity.Profile;
import net.os.goodcourses.exception.DuplicateFeedbackException;
import net.os.goodcourses.exception.ResourceNotFoundException;
import net.os.goodcourses.form.FeedbackForm;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.repository.storage.FeedBackRepository;
import net.os.goodcourses.repository.storage.ProfileRepository;
import net.os.goodcourses.service.impl.FeedbackServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeedbackServiceImplTest {

    private static final long PROFILE_ID = 10L;
    private static final long OTHER_PROFILE_ID = 11L;
    private static final long COURSE_ID = 20L;
    private static final long FEEDBACK_ID = 30L;

    @Mock
    private FeedBackRepository feedBackRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private ProfileRepository profileRepository;

    private FeedbackServiceImpl feedbackService;
    private Profile profile;
    private Course course;

    @Before
    public void setUp() {
        feedbackService = new FeedbackServiceImpl(
                feedBackRepository,
                courseRepository,
                profileRepository);
        profile = new Profile();
        profile.setId(PROFILE_ID);
        course = new Course();
        course.setId(COURSE_ID);
    }

    @Test
    public void shouldDelegateMyReviewsQueryToRepository() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<FeedBack> expected = new PageImpl<>(Collections.singletonList(new FeedBack()));
        when(feedBackRepository.findAllByProfileIdOrderByStartDateDesc(PROFILE_ID, pageable))
                .thenReturn(expected);

        Page<FeedBack> actual = feedbackService.findMyReviews(PROFILE_ID, pageable);

        assertSame(expected, actual);
        verify(feedBackRepository).findAllByProfileIdOrderByStartDateDesc(PROFILE_ID, pageable);
    }

    @Test
    public void shouldCreateFeedbackAndSetCreationTimestamp() {
        when(feedBackRepository.existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID)).thenReturn(false);
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(profile));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(feedBackRepository.saveAndFlush(any(FeedBack.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        long beforeCreate = System.currentTimeMillis();

        FeedBack created = feedbackService.create(
                PROFILE_ID,
                COURSE_ID,
                new FeedbackForm("  Хороший курс  "));

        assertSame(profile, created.getProfile());
        assertSame(course, created.getCourse());
        assertEquals("Хороший курс", created.getDescription());
        assertNotNull(created.getStartDate());
        assertTrue(created.getStartDate().getTime() >= beforeCreate);
        assertNull(created.getLastUpdate());
        verify(feedBackRepository).existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID);
        verify(profileRepository).findById(PROFILE_ID);
        verify(courseRepository).findById(COURSE_ID);
        verify(feedBackRepository).saveAndFlush(created);
    }

    @Test
    public void shouldRejectDuplicateBeforeLoadingProfileAndCourse() {
        when(feedBackRepository.existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID)).thenReturn(true);

        try {
            feedbackService.create(PROFILE_ID, COURSE_ID, new FeedbackForm("Повторный отзыв"));
            fail("DuplicateFeedbackException expected");
        } catch (DuplicateFeedbackException expected) {
            assertEquals("Вы уже оставили отзыв на этот курс", expected.getMessage());
        }

        verifyNoInteractions(profileRepository, courseRepository);
        verify(feedBackRepository, never()).saveAndFlush(any(FeedBack.class));
    }

    @Test
    public void shouldTranslateDatabaseDuplicateRaceToDomainException() {
        when(feedBackRepository.existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID)).thenReturn(false);
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(profile));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(feedBackRepository.saveAndFlush(any(FeedBack.class)))
                .thenThrow(new DataIntegrityViolationException("uq_feedback_course_profile"));

        try {
            feedbackService.create(PROFILE_ID, COURSE_ID, new FeedbackForm("Отзыв"));
            fail("DuplicateFeedbackException expected");
        } catch (DuplicateFeedbackException expected) {
            assertTrue(expected.getCause() instanceof DataIntegrityViolationException);
        }
    }

    @Test
    public void shouldRejectCreateWhenProfileDoesNotExist() {
        when(feedBackRepository.existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID)).thenReturn(false);
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.empty());

        try {
            feedbackService.create(PROFILE_ID, COURSE_ID, new FeedbackForm("Отзыв"));
            fail("ResourceNotFoundException expected");
        } catch (ResourceNotFoundException expected) {
            assertEquals("Профиль не найден", expected.getMessage());
        }

        verifyNoInteractions(courseRepository);
        verify(feedBackRepository, never()).saveAndFlush(any(FeedBack.class));
    }

    @Test
    public void shouldRejectCreateWhenCourseDoesNotExist() {
        when(feedBackRepository.existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID)).thenReturn(false);
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(profile));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.empty());

        try {
            feedbackService.create(PROFILE_ID, COURSE_ID, new FeedbackForm("Отзыв"));
            fail("ResourceNotFoundException expected");
        } catch (ResourceNotFoundException expected) {
            assertEquals("Курс не найден", expected.getMessage());
        }

        verify(feedBackRepository, never()).saveAndFlush(any(FeedBack.class));
    }

    @Test
    public void shouldFindFeedbackOwnedByCurrentProfile() {
        FeedBack feedback = feedbackWithDates();
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, PROFILE_ID))
                .thenReturn(Optional.of(feedback));

        FeedBack found = feedbackService.findOwned(PROFILE_ID, FEEDBACK_ID);

        assertSame(feedback, found);
        verify(feedBackRepository).findByIdAndProfileId(FEEDBACK_ID, PROFILE_ID);
    }

    @Test
    public void shouldNotExposeFeedbackOwnedByAnotherProfile() {
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, OTHER_PROFILE_ID))
                .thenReturn(Optional.empty());

        try {
            feedbackService.findOwned(OTHER_PROFILE_ID, FEEDBACK_ID);
            fail("ResourceNotFoundException expected");
        } catch (ResourceNotFoundException expected) {
            assertEquals("Отзыв не найден", expected.getMessage());
        }
    }

    @Test
    public void shouldUpdateOwnedFeedbackAndPreserveCreationTimestamp() {
        FeedBack feedback = feedbackWithDates();
        Timestamp originalStartDate = feedback.getStartDate();
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, PROFILE_ID))
                .thenReturn(Optional.of(feedback));
        when(feedBackRepository.save(feedback)).thenReturn(feedback);
        long beforeUpdate = System.currentTimeMillis();

        FeedBack updated = feedbackService.update(
                PROFILE_ID,
                FEEDBACK_ID,
                new FeedbackForm("  Обновлённый отзыв  "));

        assertSame(feedback, updated);
        assertEquals("Обновлённый отзыв", updated.getDescription());
        assertSame(originalStartDate, updated.getStartDate());
        assertNotNull(updated.getLastUpdate());
        assertTrue(updated.getLastUpdate().getTime() >= beforeUpdate);
        verify(feedBackRepository).save(feedback);
    }

    @Test
    public void shouldNotUpdateFeedbackOwnedByAnotherProfile() {
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, OTHER_PROFILE_ID))
                .thenReturn(Optional.empty());

        try {
            feedbackService.update(
                    OTHER_PROFILE_ID,
                    FEEDBACK_ID,
                    new FeedbackForm("Чужое изменение"));
            fail("ResourceNotFoundException expected");
        } catch (ResourceNotFoundException expected) {
            assertEquals("Отзыв не найден", expected.getMessage());
        }

        verify(feedBackRepository, never()).save(any(FeedBack.class));
    }

    @Test
    public void shouldDeleteOwnedFeedback() {
        FeedBack feedback = feedbackWithDates();
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, PROFILE_ID))
                .thenReturn(Optional.of(feedback));

        feedbackService.delete(PROFILE_ID, FEEDBACK_ID);

        verify(feedBackRepository).delete(feedback);
    }

    @Test
    public void shouldNotDeleteFeedbackOwnedByAnotherProfile() {
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, OTHER_PROFILE_ID))
                .thenReturn(Optional.empty());

        try {
            feedbackService.delete(OTHER_PROFILE_ID, FEEDBACK_ID);
            fail("ResourceNotFoundException expected");
        } catch (ResourceNotFoundException expected) {
            assertEquals("Отзыв не найден", expected.getMessage());
        }

        verify(feedBackRepository, never()).delete(any(FeedBack.class));
    }

    @Test
    public void shouldRejectBlankDescriptionAtServiceBoundary() {
        FeedBack feedback = feedbackWithDates();
        when(feedBackRepository.findByIdAndProfileId(FEEDBACK_ID, PROFILE_ID))
                .thenReturn(Optional.of(feedback));

        try {
            feedbackService.update(PROFILE_ID, FEEDBACK_ID, new FeedbackForm("   "));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("Текст отзыва не может быть пустым", expected.getMessage());
        }

        verify(feedBackRepository, never()).save(any(FeedBack.class));
    }

    @Test
    public void shouldRejectTooLongDescriptionAtServiceBoundary() {
        when(feedBackRepository.existsByCourseIdAndProfileId(COURSE_ID, PROFILE_ID)).thenReturn(false);
        when(profileRepository.findById(PROFILE_ID)).thenReturn(Optional.of(profile));
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));

        try {
            feedbackService.create(
                    PROFILE_ID,
                    COURSE_ID,
                    new FeedbackForm(repeat('a', 2001)));
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException expected) {
            assertEquals("Отзыв не должен превышать 2000 символов", expected.getMessage());
        }

        verify(feedBackRepository, never()).saveAndFlush(any(FeedBack.class));
    }

    private FeedBack feedbackWithDates() {
        FeedBack feedback = new FeedBack();
        feedback.setProfile(profile);
        feedback.setCourse(course);
        feedback.setDescription("Исходный отзыв");
        feedback.setStartDate(Timestamp.valueOf("2026-01-01 10:00:00"));
        return feedback;
    }

    private String repeat(char value, int count) {
        StringBuilder result = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            result.append(value);
        }
        return result.toString();
    }
}
