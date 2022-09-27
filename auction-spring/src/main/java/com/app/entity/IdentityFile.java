package com.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
public class IdentityFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Column(unique=true)
	private Long idNumber;
	
	@Column(unique=true)
	private Long user_id;
	
    @Column(nullable = false)
	private String imageId;
	
	private String fileType;
	

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;
	

	
}
