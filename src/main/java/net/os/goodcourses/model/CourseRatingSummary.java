package net.os.goodcourses.model;

import java.io.Serializable;

public class CourseRatingSummary implements Serializable {
    private static final long serialVersionUID = 1L;

    private final Double average;
    private final long count;

    public CourseRatingSummary(Double average, long count) {
        this.average = average;
        this.count = count;
    }

    public Double getAverage() {
        return average;
    }

    public long getCount() {
        return count;
    }

    public boolean hasRatings() {
        return count > 0;
    }
}
