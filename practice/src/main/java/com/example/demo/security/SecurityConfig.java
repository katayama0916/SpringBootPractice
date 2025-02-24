package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.demo.repository.AdminRepository;

@Configuration
public class SecurityConfig {

	private final AdminDetailsService adminDetailsService;
	private final AdminRepository adminRepository;

	public SecurityConfig(AdminDetailsService adminDetailsService, AdminRepository adminRepository) {
		this.adminDetailsService = adminDetailsService;
		this.adminRepository = adminRepository;
	}

	// パスワードエンコーダー
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	// AuthenticationProvider の定義
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
		provider.setUserDetailsService(adminDetailsService);
		provider.setPasswordEncoder(passwordEncoder());
		return provider;
	}

	// AuthenticationManager の定義
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	// SecurityFilterChain の設定
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable()) // 必要なら有効化
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/signup", "/admin/loginconfirmation", "/admin/signin").permitAll()
						.requestMatchers("/admin/signup").permitAll()
						.requestMatchers("/admin/contacts").authenticated()
						.requestMatchers("/admin/contacts/**").authenticated()
						.anyRequest().authenticated())
				.formLogin(login -> login
						.loginPage("/admin/signin")
						.loginProcessingUrl("/admin/signin")
						.usernameParameter("email")
						.passwordParameter("password")

						.successHandler(customAuthenticationSuccessHandler()) // カスタムハンドラー
						.failureUrl("/admin/signin?error=true")
						.permitAll())
				.logout(logout -> logout
						.logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout", "GET")) // GET でもログアウト可能に
						.logoutUrl("/admin/logout") // 明示的に指定
						.logoutSuccessUrl("/admin/signin")
						.invalidateHttpSession(true)
						.deleteCookies("JSESSIONID")
						.permitAll());

		return http.build();
	}

	// カスタム認証成功時のハンドラー
	@Bean
	public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler(adminRepository);
	}
}
