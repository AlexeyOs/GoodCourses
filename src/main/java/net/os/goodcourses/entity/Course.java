package net.os.goodcourses.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "course")
public class Course extends AbstractFinishDateEntity<Long> implements Serializable {
	private static final long serialVersionUID = 4206575925684228495L;

	@Id
	@SequenceGenerator(name = "COURSE_ID_GENERATOR", sequenceName = "COURSE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COURSE_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(length = 60)
	private String platform;

	@Column(length = 60)
	private String author;

	@Column(name = "description")
	private String description;

	@Column(nullable = false)
	private boolean visible;

	@Column(name = "subject_of_study", length = 120)
	private String subjectOfStudy;

	@Column(name = "link")
	private String link;

	/**
	 * 0 - находится на премодерации
	 * 1 - прошел премодерацию и доступен для оценки других пользователй
	 * 2 - устарел и является не актуальным
	 */
	@Column(name = "status")
	private int status;


    @ManyToMany
    @JoinTable(name = "course_profile",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "profile_id"))
	private List<Profile> profiles = new ArrayList<>();

	@OneToMany(mappedBy = "course", cascade = {CascadeType.ALL, CascadeType.PERSIST})
	private List<FeedBack> feedbacks;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		Course course = (Course) o;
		return visible == course.visible &&
				status == course.status &&
				Objects.equals(id, course.id) &&
				Objects.equals(platform, course.platform) &&
				Objects.equals(author, course.author) &&
				Objects.equals(description, course.description) &&
				Objects.equals(subjectOfStudy, course.subjectOfStudy) &&
				Objects.equals(link, course.link) &&
				Objects.equals(profiles, course.profiles) &&
				Objects.equals(feedbacks, course.feedbacks);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), id, platform, author, description, visible, subjectOfStudy, link, status, profiles, feedbacks);
	}
}
