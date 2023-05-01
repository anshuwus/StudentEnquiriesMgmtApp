package com.ashokit.entity;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="AIT_STUDENT_ENQUIRES")
public class StudentEnqEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer enquiryId;
	private String studentName;
	private Long studentPhno;
	private String classMode;
	private String courseName;
	private String enquiryStatus;
	@CreationTimestamp
	private LocalDate createdDate;
	@UpdateTimestamp
	private LocalDate lastUpdatedDate;
	
	//---Association mapping--
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private UserDtlsEntity user;

	@Override
	public String toString() {
		return "StudentEnqEntity [enquiryId=" + enquiryId + ", studentName=" + studentName + ", studentPhno="
				+ studentPhno + ", classMode=" + classMode + ", courseName=" + courseName + ", enquiryStatus="
				+ enquiryStatus + ", createdDate=" + createdDate + ", lastUpdatedDate=" + lastUpdatedDate + "]";
	}
}
