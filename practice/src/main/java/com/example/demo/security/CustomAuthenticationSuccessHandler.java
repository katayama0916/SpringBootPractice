package com.example.demo.security;

import java.io.IOException;
import java.util.Optional;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.demo.entity.Admin;
import com.example.demo.repository.AdminRepository;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	private final AdminRepository adminRepository;

	public CustomAuthenticationSuccessHandler(AdminRepository adminRepository) {
		this.adminRepository = adminRepository;
	}

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String email = authentication.getName(); // ログインした管理者のメール取得
		Optional<Admin> admin = adminRepository.findByEmail(email);

		if (admin.isPresent()) {
			HttpSession session = request.getSession();
			session.setAttribute("admin", admin.get()); // セッションに管理者情報を保存
			System.out.println("✅ セッションに管理者情報を保存しました: " + admin.get().getEmail());
		}

		response.sendRedirect("/admin/contacts"); // ログイン後のリダイレクト先
	}
}
