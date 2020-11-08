package com.paypal.bfs.test.employeeserv.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "EMPLOYEE")
public class EmployeeEntity implements Serializable {
	private static final long serialVersionUID = -3646241430698304971L;

	@Id
	@GeneratedValue
	@Column(name = "ID")
	private Integer id;

	@Column(name = "FIRST_NAME", nullable=false)
	private String firstName;

	@Column(name = "LAST_NAME", nullable=false)
	private String lastName;
	
	@Column(name = "DATE_OF_BIRTH", nullable=false)
	private String dob;
	
	@OneToOne(cascade = CascadeType.ALL)
	private AddressEntity addressEntity;

}
