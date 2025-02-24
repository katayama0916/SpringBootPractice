package com.example.demo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;

@Service

public class AdminDetailsService implements UserDetailsService {

	private final AdminRepository adminRepository;

	public AdminDetailsService(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("ユーザーが見つかりません: " + email));

		return new AdminDetails(admin); // AdminDetails を返す
	}
}
