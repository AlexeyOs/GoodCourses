package net.os.goodcourses.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

@Getter
@Setter
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

	@Transient
	public String getFullName() {
		return firstName + " " + lastName;
	}

	// https://hibernate.atlassian.net/browse/HHH-7610
	public Contacts getContacts() {
		if (contacts == null) {
			contacts = new Contacts();
		}
		return contacts;
	}

}