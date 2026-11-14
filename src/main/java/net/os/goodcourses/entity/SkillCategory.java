package net.os.goodcourses.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 */
@Entity
@Table(name="skill_category")
public class SkillCategory extends AbstractEntity<Long>{
	private static final long serialVersionUID = -8959739023562086833L;
	
	@Id
	@Column
	private Long id;
	
	@Column(nullable=false, length=50)
	private String category;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
