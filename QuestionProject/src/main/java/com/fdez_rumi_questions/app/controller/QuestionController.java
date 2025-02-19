package com.fdez_rumi_questions.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdez_rumi_questions.app.entity.Question;
import com.fdez_rumi_questions.app.service.QuestionService;

@RestController
@RequestMapping("/question")
public class QuestionController {

	@Autowired
	private QuestionService questionService;
		
	@GetMapping("/question")	
	public String showQuestions(@RequestParam(defaultValue = "0") int page,
								@RequestParam(defaultValue = "10") int size,
								@RequestParam(required = false) String category,
								Model model) {
		Page<Question> questionPage;
		if (category != null) {
			questionPage = questionService.getAllQuestionActivePageable(category, page, size);
			model.addAttribute("category", category);
		} else {
			questionPage = questionService.getAllQuestionActivePageable(page, size);
			model.addAttribute("category", "");
		}
		model.addAttribute("questions", questionPage.getContent());
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", questionPage.getTotalPages());
		model.addAttribute("pageNumbers", questionService.getNumberOfPages(questionPage));
		return "list-questions";
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
			model.addAttribute("questions", questionService.getAllQuestionActivePageable());
			redirectAttributes.addFlashAttribute("success", "Archivo JSON cargado con éxito");
			
			return "redirect:/question/all";
		} catch (IOException | IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "add-question-file";
		}
	}
}