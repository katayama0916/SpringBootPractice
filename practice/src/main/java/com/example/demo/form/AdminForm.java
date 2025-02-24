package com.example.demo.form;

import java.io.Serializable;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.lang.Nullable;

import lombok.Data;

@Data
public class AdminForm implements Serializable {
	@NotBlank
	private String lastName;

	@NotBlank
	private String firstName;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	@Size(min = 4, max = 12)
	private String rawPassword;
	@Nullable
	private String password;

	private String address = "";
	private String phone = "";
	private String zipCode = "";
	private String buildingName = "";
	private String contactType = "";
	private String body = "";

}
