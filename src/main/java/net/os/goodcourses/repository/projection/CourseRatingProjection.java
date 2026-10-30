package net.os.goodcourses.repository.projection;

public interface CourseRatingProjection {
    Double getAverage();

    long getCount();
}
