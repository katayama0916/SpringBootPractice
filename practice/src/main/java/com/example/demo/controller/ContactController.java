package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.data.ContactData;

@Controller
public class ContactController {
	@PostMapping("/contact")
	public ModelAndView contact(@ModelAttribute ContactData contactdata ,ModelAndView mv)
			 {

		mv.setViewName("confirmation");

		mv.addObject("lastName",   contactdata.getLastName());
		mv.addObject("firstName", contactdata.getFirstName());
		mv.addObject("email", contactdata.getEmail());
		mv.addObject("phone", contactdata.getPhone());
		mv.addObject("zipCode",  contactdata.getZipCode());
		mv.addObject("address", contactdata.getAddress());
		mv.addObject("buildingName", contactdata .getBuildingName());
		mv.addObject("contactType", contactdata .getContactType());
		mv.addObject("body", contactdata.getBody());

		return mv;
	}
}
