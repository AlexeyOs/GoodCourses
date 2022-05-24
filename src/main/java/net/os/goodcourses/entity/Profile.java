package net.os.goodcourses.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

/**
 *
 */
@Entity
@Table(name = "profile")
public class Profile extends AbstractEntity<Long> implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "PROFILE_ID_GENERATOR", sequenceName = "PROFILE_SEQ", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PROFILE_ID_GENERATOR")
	@Column(unique = true, nullable = false)
	private Long id;

	@Column(name = "first_name", nullable = false, length = 50)
	private String firstName;

	@Column(name = "last_name", nullable = false, length = 50)
	private String lastName;

	@Column(length = 2147483647)
	private String objective;

	@Column(name = "large_photo", length = 255)
	private String largePhoto;

	@Column(name = "small_photo", length = 255)
	private String smallPhoto;

	@Column(length = 100)
	private String email;

	@Column
	private String info;

	@Column(length = 2147483647)
	private String summary;

	@Column(nullable = false, length = 100)
	private String uid;

	@Column(nullable = false, length = 100)
	private String password;

	@Column(nullable = false)
	private boolean completed;

	@Column(insertable = false)
	private Timestamp created;



    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "course_profile",
            joinColumns = @JoinColumn(name = "profile_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
	private List<Course> courses = new ArrayList<>();

    public List<Course> getCourses() {
        return courses;
    }

	@Embedded
	private Contacts contacts;

	public Profile() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getObjective() {
		return this.objective;
	}

	public void setObjective(String objective) {
		this.objective = objective;
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUid() {
		return this.uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getLargePhoto() {
		return largePhoto;
	}

	public void setLargePhoto(String largePhoto) {
		this.largePhoto = largePhoto;
	}

	public String getSmallPhoto() {
		return smallPhoto;
	}

	public void setSmallPhoto(String smallPhoto) {
		this.smallPhoto = smallPhoto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Timestamp getCreated() {
		return created;
	}

	public void setCreated(Timestamp created) {
		this.created = created;
	}

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	@Transient
	public String getProfilePhoto() {
		if (largePhoto != null) {
			return largePhoto;
		} else {
			return "/static/img/profile-placeholder.png";
		}
	}

	public String updateProfilePhotos(String largePhoto, String smallPhoto) {
		String oldLargeImage = this.largePhoto;
		setLargePhoto(largePhoto);
		setSmallPhoto(smallPhoto);
		return oldLargeImage;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	// https://hibernate.atlassian.net/browse/HHH-7610
	public Contacts getContacts() {
		if (contacts == null) {
			contacts = new Contacts();
		}
		return contacts;
	}

	public void setContacts(Contacts contacts) {
		this.contacts = contacts;
	}

	private void updateListSetProfile(List<? extends ProfileEntity> list) {
		if (list != null) {
			for (ProfileEntity entity : list) {
				entity.setProfile(this);
			}
		}
	}
}