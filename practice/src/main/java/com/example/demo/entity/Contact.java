package com.example.demo.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@Getter
@Setter
public class Contact {

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
	private String buildingName;

	@Column(name = "contact_type", nullable = false)
	private String contactType;

	@Column(name = "body", nullable = false)
	private String body;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	public LocalDateTime getUpdatedAt() {
		return createdAt;
	}

	@Override
	public String toString() {
		return "Contact{id=" + id +
				", lastName='" + lastName + '\'' +
				", firstName='" + firstName + '\'' +
				", contactType='" + contactType + '\'' +
				", createdAt=" + createdAt +
				", updatedAt=" + updatedAt + '}';
	}

}
