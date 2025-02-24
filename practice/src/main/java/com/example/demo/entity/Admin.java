package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
public class Admin {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL の AUTO_INCREMENT に対応
	@Column(name = "id")
	private Long id;

	@Column(name = "last_name", nullable = false)
	private String lastName;

	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "phone", nullable = false)
	private String phone;

	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "buildingname", nullable = false)
	private String buildingname;

	@Column(name = "contacttype", nullable = false)
	private String contacttype;

	@Column(name = "body", nullable = false)
	private String body;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
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

	public String getPhone() {
		return phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBuildingName() {
		return buildingname;
	}

	public void setBuildingName(String buildingname) {
		this.buildingname = buildingname;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getContactType() {
		return contacttype;
	}

	public void setContactType(String contacttype) {
		this.contacttype = contacttype;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

}
