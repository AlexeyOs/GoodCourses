package net.os.goodcourses.service;


public interface AddCourseService {
    void createNewCourse(String platform,
                         String author,
                         String subjectOfStudy,
                         String link,
                         String description);
}
