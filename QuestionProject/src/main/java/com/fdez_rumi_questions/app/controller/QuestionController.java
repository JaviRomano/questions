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
	public String showQuestions(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
								@RequestParam(required = false) String category, Model model) {
		Page<Question> questionPage;
		if (category != null) {
			questionPage = questionService.getAllQuestionActivePageable(category, page, size);
			model.addAttribute("category", category.toString());
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
		
	@GetMapping("/new")
	public String createQuestion(Model model, @RequestParam(required = false) String stage,
			@RequestParam(required = false) String questionType, @RequestParam(required = false) String category) {
		
		String currentStage = (stage == null) ? "first" : stage;
		model.addAttribute("stage", currentStage);
		model.addAttribute("categories", category.equals(currentStage));		
		if ("second".equals(currentStage)) {
			model.addAttribute("questionType", questionType);
			model.addAttribute("category", category);
		}
		return "add-question";
	}
	
	@PostMapping("/new")
	public String processFormToAddNewQuestion(@RequestParam String questionType, @RequestParam String category,
			RedirectAttributes redirectAttributes) {
		if (!"MULTIPLE_QUESTION".equals(questionType) && !"TRUE_OR_FALSE".equals(questionType)) {
			return "redirect:/question/new";
		}
		redirectAttributes.addAttribute("stage", "second");
		redirectAttributes.addAttribute("questionType", questionType);
		redirectAttributes.addAttribute("category", category);
		return "redirect:/question/new";
	}
	
	@PostMapping("/upload")
	public String uploadQuestionFromTheFormFile(@RequestParam MultipartFile file, Model model, RedirectAttributes redirectAttributes) {
		try {
			String jsonContent = new String(file.getBytes());
			
			 ObjectMapper objectMapper = new ObjectMapper();
		        objectMapper.readTree(jsonContent);
		        
			questionService.proccessQuestionFromJson(jsonContent);
			
			model.addAttribute("questions", questionService.getAllQuestionActivePageable());
			redirectAttributes.addFlashAttribute("success", "Todo bien");
			
			return "redirect:/question/all";
		} catch (IOException | IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
			return "add-question-file";
		}
	}
		
//	@PostMapping("/save")
//	public String saveOrPudateQuestion(@RequestParam(required = false) Long id,
//									   @RequestParam String text,
//									   @RequestParam(required = false) String correctAnswer,
//									   @RequestParam(required = false) String failAnswer,
//									   @RequestParam(required = false) String answer,
//									   @RequestParam String questionType,
//									   @RequestParam String categoty,
//									   RedirectAttributes redirectAttributes) {
//		TypeOfQuestion tipeOfQuestion = TypeOfQuestion.valueOf(questionType);
//		String category = questionService.g
//		
//	}

//	@DeleteMapping("/{id}")
//	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
//		questionService.deleteQuestion(id);
//		return ResponseEntity.noContent().build();
//	}
	
//	@GetMapping("/add-questions-from-file")
//	public String showUploadPage() {
//	    return "add-questions-from-file";
//	}

}