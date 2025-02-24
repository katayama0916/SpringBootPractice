package com.example.demo.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.entity.Admin;

public class AdminDetails implements UserDetails {

	private final Admin admin;

	public AdminDetails(Admin admin) {
		this.admin = admin;
	}

	public Admin getAdmin() {
		return admin;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.emptyList(); // 権限を管理しない場合
	}

	private String firstName; // ← フィールドがあることを確認

	public String getFirstName() { // ← getter が存在するか確認
		return firstName;
	}

	private String lastName;

	public String getLastName() {
		return lastName;
	}

	@Override
	public String getPassword() {
		return admin.getPassword();
	}

	@Override
	public String getUsername() {
		return admin.getEmail(); // ユーザー名は通常、メールアドレスにする
	}

	public String getPhone() {
		return admin.getPhone();
	}

	public String getAddress() {
		return admin.getAddress();
	}

	public String getBuildingName() {
		return admin.getBuildingName();
	}

	public String getContactType() {
		return admin.getContactType();
	}

	public String getBody() {
		return admin.getBody();
	}

	public String getZipcode() {
		return admin.getZipCode();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
