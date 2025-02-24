package com.example.demo.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;
import com.example.demo.security.AdminDetails;

@Service
public class AdminService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

	@Autowired
	private AdminRepository adminRepository;

	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Admin admin = adminRepository.findByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("管理者が見つかりません: " + email));
		return new AdminDetails(admin);
	}

	@Transactional
	public void saveAdmin(Admin admin) {
		logger.info("管理者登録開始: email={}", admin.getEmail());

		if (adminRepository.existsByEmail(admin.getEmail())) {
			throw new IllegalArgumentException("このメールアドレスはすでに登録されています。");
		}

		logger.info("📝 新しい管理者を登録: {}", admin.getEmail());
		adminRepository.save(admin);
		logger.info("✅ 登録成功: メールアドレス {}", admin.getEmail());
	}

	// ここを修正！
	public Optional<Admin> findById(Integer id) {
		return adminRepository.findById(id);
	}
}
