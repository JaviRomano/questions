package com.fdez_rumi_questions.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	@GetMapping({"", "/", "/home"})
	public String home(Model model) {
        return "home";
	}	
	 
//	@GetMapping("other")
//	public void forceInternalServerError() {
//		throw new RuntimeException("Forcing Error 500.");
//	}	
	
}