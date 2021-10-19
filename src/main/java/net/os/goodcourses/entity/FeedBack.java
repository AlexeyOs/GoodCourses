package net.os.goodcourses.entity;


import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "feedback")
public class FeedBack extends AbstractEntity<Long> implements Serializable, ProfileEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "FEEDBACK_ID_GENERATOR", sequenceName = "FEEDBACK_SEQ", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FEEDBACK_ID_GENERATOR")
    @Column(unique = true, nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profile_id", nullable = false)
    private Profile profile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(nullable = false, length = 2147483647)
    private String description;

    @Column(name = "start_date", nullable = false)
    private Timestamp startDate;

    @Column(name = "last_update")
    private Timestamp lastUpdate;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Profile getProfile() {
        return profile;
    }

    public Course getCourse() {
        return course;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}
