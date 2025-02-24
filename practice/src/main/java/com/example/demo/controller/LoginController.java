package com.example.demo.controller;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes; // 追加

import com.example.demo.entity.Admin;
import com.example.demo.entity.Contact;
import com.example.demo.form.AdminForm;
import com.example.demo.form.ContactsForm;
import com.example.demo.security.AdminDetails;
import com.example.demo.service.AdminService;
import com.example.demo.service.ContactService;

@Controller
public class LoginController {
	private final AdminService adminService;
	private final ContactService contactService;
	private final PasswordEncoder passwordEncoder;

	
	public LoginController(AdminService adminService, ContactService contactService, PasswordEncoder passwordEncoder) {
		this.adminService = adminService;
		this.contactService = contactService;
		this.passwordEncoder = passwordEncoder;
	}

	@GetMapping("/admin/signup")
	public String signup(Model model) {
		model.addAttribute("adminForm", new AdminForm());
		return "admin/signup";
	}

	@PostMapping("/admin/signup")
	public String signup(@Validated @ModelAttribute("adminForm") AdminForm adminForm,
			BindingResult errorResult,
			HttpServletRequest request,
			RedirectAttributes redirectAttributes) {

		System.out.println("⭐️ /admin/signup にリクエストが来た！"); // 確認ログ
		System.out.println("📧 登録しようとしているメール: " + adminForm.getEmail());
		System.out.println("🔑 登録しようとしているパスワード: " + adminForm.getRawPassword());

		if (adminForm.getRawPassword() == null || adminForm.getRawPassword().isEmpty()) {

			return "admin/signup";
		} else {
			adminForm.setPassword(passwordEncoder.encode(adminForm.getRawPassword()));

		}

		if (errorResult.hasErrors()) {
			System.out.println("❌ 入力エラー発生: " + errorResult.getAllErrors());
			return "admin/signup";
		}

		Admin admin = new Admin();
		admin.setLastName(adminForm.getLastName());
		admin.setFirstName(adminForm.getFirstName());
		admin.setEmail(adminForm.getEmail());
		admin.setPassword(adminForm.getPassword());

		admin.setAddress(""); // 空文字をセット
		admin.setBody(""); // 他のカラムも null にならないようにするadmin.setBuildingName(null); // 空文字ではなく null をセット
		admin.setBuildingName("");
		admin.setPhone("");
		admin.setZipCode("");

		HttpSession session = request.getSession();

		System.out.println("✅ セッションに管理者情報を保存！");
		System.out.println("🔑 ハッシュ化されたパスワード: " + adminForm.getPassword()); 

		session.setAttribute("adminForm", adminForm);

		return "redirect:/admin/loginconfirmation";
	}

	@GetMapping("/admin/loginconfirmation")
	public String loginconfirmation(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false);

		if (session == null) {
			System.out.println("⚠️ セッションが存在しません！");
			return "redirect:/admin/signup";
		}

		AdminForm adminForm = (AdminForm) session.getAttribute("adminForm");

		if (adminForm == null) {
			System.out.println("⚠️ セッション内に adminForm が存在しません！");
			return "redirect:/admin/signup";
		}

		model.addAttribute("adminForm", adminForm);
		System.out.println("➡️ loginconfirmation にリダイレクトします！");
		return "admin/loginconfirmation";
	}

	@PostMapping("/admin/loginconfirmation")
	public String register(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		System.out.println("📥 /admin/loginconfirmation に POST リクエストが来た！");
		if (session == null) {
			System.out.println("⚠️ セッションが存在しません！");
			return "redirect:/admin/signup";
		}

		AdminForm adminForm = (AdminForm) session.getAttribute("adminForm");
		System.out.println("📦 セッションから取得した管理者情報: " + adminForm);
		if (adminForm == null) {
			return "redirect:/admin/signup";
		}

		System.out.println("📝 DBに登録する情報:");
		System.out.println("📧 メール: " + adminForm.getEmail());
		System.out.println("🔑 ハッシュ化されたパスワード: " + adminForm.getPassword());

		// AdminForm から Admin へ変換
		Admin admin = new Admin();
		admin.setLastName(adminForm.getLastName());
		admin.setFirstName(adminForm.getFirstName());
		admin.setEmail(adminForm.getEmail());
		admin.setPassword(adminForm.getPassword());

		try {
			adminService.saveAdmin(admin);
			System.out.println("✅ DBに保存完了！");
		} catch (Exception e) {
			System.out.println("❌ DB保存時にエラー発生: " + e.getMessage());
			e.printStackTrace(); // 例外の詳細を出力
		}
		session.removeAttribute("adminForm");

		return "redirect:/admin/signin";
	}

	@GetMapping("/contact/complete")
	public String complete(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return "redirect:/contact";
		}

		AdminForm adminForm = (AdminForm) session.getAttribute("adminForm");
		model.addAttribute("adminForm", adminForm);

		session.invalidate();

		return "completion";
	}

	@GetMapping("/admin/signin")
	public String signin(Model model, @AuthenticationPrincipal AdminDetails adminDetails) {
		if (adminDetails != null) {
			// 既にログインしている場合、管理者のページ（contacts など）へリダイレクト
			return "redirect:/admin/contacts";
		}
		model.addAttribute("adminForm", new AdminForm());
		return "admin/signin"; // 未ログインの場合、signin ページを表示
	}

	@GetMapping("/admin/contacts")
	public String showContacts(Model model, @AuthenticationPrincipal AdminDetails adminDetails) {
		List<Contact> contacts = contactService.findAll();

		if (contacts == null || contacts.isEmpty()) {
			System.out.println("Contacts が取得できませんでした。");
		} else {
			System.out.println("取得した Contacts: " + contacts);
		}

		model.addAttribute("contacts", contacts);
		model.addAttribute("adminDetails", adminDetails); // 追加

		System.out.println("モデルに渡す contacts: " + model.containsAttribute("contacts"));

		return "admin/contacts"; // admin/contacts.html を表示
	}

	@GetMapping("/admin/contacts/{id}")
	public String showContactDetail(@PathVariable("id") Long id, Model model,
			@AuthenticationPrincipal AdminDetails adminDetails) {
		if (adminDetails == null) {
			return "redirect:/admin/signin";
		}

		// Admin情報を取得（findById の戻り値を Optional<Admin> にする）
		Contact contact = contactService.findById(id);

		if (contact == null) {
			System.out.println("⚠️ 指定されたIDの管理者が見つかりません: " + id);
			return "redirect:/admin/contacts";
		}

		// Admin情報をフォームにセット
		ContactsForm contactsForm = new ContactsForm();
		contactsForm.setLastName(contact.getLastName());
		contactsForm.setFirstName(contact.getFirstName());
		contactsForm.setEmail(contact.getEmail());
		contactsForm.setPhone(contact.getPhone());
		contactsForm.setZipCode(contact.getZipCode());
		contactsForm.setAddress(contact.getAddress());
		contactsForm.setBuildingName(contact.getBuildingName());
		contactsForm.setContactType(contact.getContactType());
		contactsForm.setBody(contact.getBody());

		model.addAttribute("contact", contact);
		model.addAttribute("contactsForm", contactsForm);

		return "admin/contacts_detail";
	}

	// お問い合わせ編集画面
	@GetMapping("/admin/contacts/{id}/edit")
	public String editContact(@PathVariable Long id, Model model,
			@AuthenticationPrincipal AdminDetails adminDetails) {
		if (adminDetails == null) {
			return "redirect:/admin/signin";
		}

		Contact contact = contactService.findById(id);
		if (contact == null) {
			return "redirect:/admin/contacts";
		}

		model.addAttribute("contact", contact);
		return "admin/contacts_edit";
	}

	// お問い合わせ更新処理
	@PostMapping("/admin/contacts/{id}/edit")
	public String updateContact(@PathVariable Long id, @ModelAttribute ContactsForm contactsForm) {
		contactService.update(id, contactsForm);
		return "redirect:/admin/contacts/" + id;
	}

	// お問い合わせ削除処理
	@PostMapping("/admin/contacts/{id}/delete")
	public String deleteContact(@PathVariable Long id) {
		contactService.delete(id);
		return "redirect:/admin/contacts";
	}

}
