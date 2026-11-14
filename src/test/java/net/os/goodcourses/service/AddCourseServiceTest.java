package net.os.goodcourses.service;

import net.os.goodcourses.entity.Course;
import net.os.goodcourses.repository.storage.CourseRepository;
import net.os.goodcourses.service.impl.AddCourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class AddCourseServiceTest {
    @Mock
    private CourseRepository repository;

    @InjectMocks
    private AddCourseServiceImpl service;

    @Test
    void savesAnHttpLinkWithAHost() {
        service.createNewCourse("Platform", "Author", "Topic", " https://example.org/course ");
        ArgumentCaptor<Course> course = ArgumentCaptor.forClass(Course.class);
        verify(repository).save(course.capture());
        assertEquals("https://example.org/course", course.getValue().getLink());
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "/course", "//example.org/course", "ftp://example.org/course",
            "https:/course", "https://user@example.org/course"})
    void rejectsInvalidOrUnsupportedLinks(String link) {
        assertThrows(IllegalArgumentException.class,
                () -> service.createNewCourse("Platform", "Author", "Topic", link));
        verifyNoInteractions(repository);
    }
}
