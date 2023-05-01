package com.ashokit.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Table(name="AIT_USER_DTLS")
@Entity
public class UserDtlsEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId;
	private String name;
	//@Column(unique=true)
	private String email;
	private Long phNo;
	private String pwd;
	private String accStatus;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	private List<StudentEnqEntity> enquiries;
}
