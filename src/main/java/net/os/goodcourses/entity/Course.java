package net.os.goodcourses.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;


/**
 *
 */
@Entity
@Table(name = "course")
public class Course extends AbstractFinishDateEntity<Long> implements Serializable {
	private static final long serialVersionUID = 4206575925684228495L;

	@Id
	@SequenceGenerator(name = "COURSE_ID_GENERATOR", sequenceName = "COURSE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COURSE_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(length=60)
	private String platform;

	@Column(length=60)
	private String author;

	@Column(name = "subject_of_study", length =120)
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

	@OneToMany(mappedBy = "course", cascade={CascadeType.ALL, CascadeType.PERSIST})
	private List<FeedBack> feedbacks;

    public List<Profile> getProfiles() {
        return profiles;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((getFinishDate() == null) ? 0 : getFinishDate().hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((platform == null) ? 0 : platform.hashCode());
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((subjectOfStudy == null) ? 0 : subjectOfStudy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof Course))
			return false;
		Course other = (Course) obj;
		if (getFinishDate() == null) {
			if (other.getFinishDate() != null)
				return false;
		} else if (!getFinishDate().equals(other.getFinishDate()))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (platform == null) {
			if (other.platform != null)
				return false;
		} else if (!platform.equals(other.platform))
			return false;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (subjectOfStudy == null) {
			if (other.subjectOfStudy != null)
				return false;
		} else if (!subjectOfStudy.equals(other.subjectOfStudy))
			return false;

		return true;
	}

	public List<FeedBack> getFeedbacks() {
		return feedbacks;
	}

	public void setFeedbacks(List<FeedBack> feedbacks) {
		this.feedbacks = feedbacks;
		//TODO возможно нужен будет метод для обновления отзывов
		//updateListSetFeedBacks(this.feedbacks);
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSubjectOfStudy() {
		return subjectOfStudy;
	}

	public void setSubjectOfStudy(String subjectOfStudy) {
		this.subjectOfStudy = subjectOfStudy;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
