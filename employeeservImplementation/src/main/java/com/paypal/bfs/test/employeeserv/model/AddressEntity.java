package com.paypal.bfs.test.employeeserv.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name="ADDRESS")
public class AddressEntity implements Serializable {
	
	private static final long serialVersionUID = 8347802750391432820L;

	@Id
	@GeneratedValue
	@Column(name="ADDRESS_ID")
	private Integer id;
	
	@Column(name="LINE1")
	private String line1;
	
	@Column(name="LINE2")
	private String line2;
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="COUNTRY")
	private String country;
	
	@Column(name="STATE")
	private String state;
	
	@Column(name="ZIP_CODE")
	private String zipCode;

}
