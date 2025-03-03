package com.fdez_rumi_questions.app.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdez_rumi_questions.app.entity.MultipleChoiceQuestion;
import com.fdez_rumi_questions.app.entity.Question;
import com.fdez_rumi_questions.app.entity.QuestionMaker;
import com.fdez_rumi_questions.app.entity.TrueFalseQuestion;
import com.fdez_rumi_questions.app.entity.TypeOfQuestion;
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

	@GetMapping("/{id}")
	public String GetQuestionById(@PathVariable Long id, Model model) {
		Question question = questionService.getQuestionById(id);
		model.addAttribute("question", question);
		return "questionInfo";
	}

	@GetMapping("/add")
	public String addQuestion(Model model, @RequestParam(required = false) String stage,
			@RequestParam(required = false) String questionType, @RequestParam(required = false) String category) {

		String currentStage = (stage == null) ? "first" : stage;
		model.addAttribute("stage", currentStage);
		model.addAttribute("categories", category);

		if ("second".equals(currentStage)) {
			model.addAttribute("questionType", questionType);
			model.addAttribute("category", category);
		}
		return "add-question";
	}

	@PostMapping("/save")
	public String saveQuestion(@RequestParam String questionType, @RequestParam String category,
			@RequestParam String text, @RequestParam(required = false) String correctAnswers,
			@RequestParam(required = false) String failAnswers, @RequestParam(required = false) Boolean answer,
			Model model) {
		try {
			Question newQuestion = createQuestionFromParams(questionType, category, text, correctAnswers, failAnswers,
					answer);
			questionService.createQuestion(newQuestion);
			model.addAttribute("success", "Pregunta creada con éxito");
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
		}
		return "redirect:/question/all";
	}
	
	@GetMapping("/update/{id}")
	public String updateQuestion(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
	    Question question = questionService.getQuestionById(id);
	    if (question == null) {
	        redirectAttributes.addFlashAttribute("error", "Pregunta no encontrada.");
	        return "redirect:/question/all";
	    }

	    model.addAttribute("question", question);
	    return "editQuestion";
	}

	@PostMapping("/update/{id}")
	public String editById(@PathVariable Long id, @RequestParam String category, @RequestParam String text,
			@RequestParam(name = "answer", required = false) Boolean answer,
			@RequestParam(name = "correctAnswers", required = false) List<String> correctAnswers,
			@RequestParam(name = "failAnswers", required = false) List<String> failAnswers,
			RedirectAttributes redirectAttributes) {
		
		Question previousQuestion = questionService.getQuestionById(id);
		if (previousQuestion == null) {
			redirectAttributes.addFlashAttribute("error", "Pregunta no encontrada.");
			return "redirect:/question/all";
		}
		
		Question updatedQuestion = previousQuestion;	
		
	    updatedQuestion.setText(text);
	    updatedQuestion.setCategory(category);
		
		if (previousQuestion.getTypeOfQuestion() == TypeOfQuestion.TRUE_FALSE) {
			updatedQuestion = new TrueFalseQuestion();
		        ((TrueFalseQuestion) updatedQuestion).setAnswer(answer);		    		
		} else if (previousQuestion.getTypeOfQuestion() == TypeOfQuestion.MULTIPLE_ANSWER) {
			updatedQuestion = new MultipleChoiceQuestion();
			((MultipleChoiceQuestion) updatedQuestion).setCorrectAnswers(correctAnswers != null
					? correctAnswers.stream().filter(ans -> !ans.isEmpty()).collect(Collectors.toList())
					: new ArrayList<>());
			((MultipleChoiceQuestion) updatedQuestion).setFailAnswers(failAnswers != null
					? failAnswers.stream().filter(ans -> !ans.isEmpty()).collect(Collectors.toList())
					: new ArrayList<>());
		} else {
			throw new IllegalArgumentException("La pregunta no tiene formato valido.");
		}

		questionService.updateQuestion(id, updatedQuestion);
		
		redirectAttributes.addFlashAttribute("success", "Pregunta actualizada correctamente.");
		return "redirect:/question/" + id;
	}

	@PostMapping("/desactive/{id}")
	public String desactiveById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		questionService.desactivateById(id);
		redirectAttributes.addFlashAttribute("success", "Pregunta desactivada.");
		return "redirect:/question/" + id;
	}

	@PostMapping("/activate/{id}")
	public String reactiveById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		questionService.reactivateById(id);
		redirectAttributes.addFlashAttribute("success", "Pregunta reactivada.");
		return "redirect:/question/" + id;
	}

	@PostMapping("/delete/{id}")
	public String deleteById(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		questionService.deleteQuestion(id);
		redirectAttributes.addFlashAttribute("success", "Pregunta eliminada.");
		return "redirect:/question/all";
	}

	private Question createQuestionFromParams(String questionType, String category, String text, String correctAnswers,
			String failAnswers, Boolean answer) {
		TypeOfQuestion type = TypeOfQuestion.valueOf(questionType);
		switch (type) {
		case MULTIPLE_ANSWER:
			return QuestionMaker.makeQuestion(type, category, text, Arrays.asList(correctAnswers.split(",")),
					Arrays.asList(failAnswers.split(",")));
		case TRUE_FALSE:
			return QuestionMaker.makeQuestion(type, category, text, answer);
		default:
			throw new IllegalArgumentException("Tipo de pregunta no válido");
		}
	}

	@GetMapping("/upload")
	public String addQuestionFile(Model model) {
		return "upload";
	}

	@PostMapping("/upload")
	public String uploadQuestionFromFile(@RequestParam MultipartFile file, Model model,
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