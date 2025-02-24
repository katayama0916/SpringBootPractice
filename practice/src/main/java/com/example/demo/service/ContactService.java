package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactsForm;
import com.example.demo.repository.ContactRepository;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	// 全件取得
	public List<Contact> findAll() {
		return contactRepository.findAll();
	}

	// IDで取得
	public Contact findById(Long id) {
		Optional<Contact> contact = contactRepository.findById(id);
		return contact.orElse(null);
	}

	// 更新
	public void update(Long id, ContactsForm contactsForm) {
		Contact contact = contactRepository.findById(id).orElse(null);
		if (contact != null) {
			contact.setLastName(contactsForm.getLastName());
			contact.setFirstName(contactsForm.getFirstName());
			contact.setEmail(contactsForm.getEmail());
			contact.setPhone(contactsForm.getPhone());
			contact.setZipCode(contactsForm.getZipCode());
			contact.setAddress(contactsForm.getAddress());
			contact.setBuildingName(contactsForm.getBuildingName());
			contact.setContactType(contactsForm.getContactType());
			contact.setBody(contactsForm.getBody());
			contactRepository.save(contact);
		}
	}

	// 削除
	public void delete(Long id) {
		contactRepository.deleteById(id);
	}
}
