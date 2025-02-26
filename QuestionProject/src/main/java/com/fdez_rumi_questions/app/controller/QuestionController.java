package com.fdez_rumi_questions.app.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdez_rumi_questions.app.entity.Question;
import com.fdez_rumi_questions.app.service.QuestionService;

@Controller
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
		
	@GetMapping("/all")	
	public String showQuestions(Model model) {
		List<Question> questions = questionService.getAllQuestions();
		model.addAttribute("questions", questions);
		return "questions";
	}
	
	@GetMapping("/upload")
	public String addQuestionFile(Model model) {
		return "upload";
	}	
	
	@PostMapping("/upload")
	public String uploadQuestionFromFile(@RequestParam MultipartFile file,
										 Model model,
										 RedirectAttributes redirectAttributes) {
		try {
			String jsonContent = new String(file.getBytes());			
			ObjectMapper objectMapper = new ObjectMapper();
		    objectMapper.readTree(jsonContent);
		        
			questionService.proccessQuestionFromJson(jsonContent);			
			redirectAttributes.addFlashAttribute("success", "Archivo JSON cargado con éxito");
			
			return "redirect:/question/all";
		} catch (IOException | IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "upload";
		}
	}
}