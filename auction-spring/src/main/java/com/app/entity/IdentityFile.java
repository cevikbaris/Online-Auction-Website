package com.app.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class IdentityFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique=true)
	private Long idNumber;
	
	@OneToOne()
	@JoinColumn(name = "user_id")
	private User user;
	
    @Column(nullable = false)
	private String imageId;
	
	private String fileType;
	

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		IdentityFile that = (IdentityFile) o;
		return false;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
