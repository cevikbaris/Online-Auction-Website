package com.app.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
public class FileAttachment {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private long id;
	
	private String name;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	
	@OneToOne
	private Auction auction;
	
	private String fileType;


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		FileAttachment that = (FileAttachment) o;
		return false;
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
