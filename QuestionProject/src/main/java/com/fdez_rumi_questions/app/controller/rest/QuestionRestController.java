package com.fdez_rumi_questions.app.controller.rest;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdez_rumi_questions.app.entity.MultipleChoiceQuestion;
import com.fdez_rumi_questions.app.entity.Question;
import com.fdez_rumi_questions.app.entity.QuestionMaker;
import com.fdez_rumi_questions.app.entity.TrueFalseQuestion;
import com.fdez_rumi_questions.app.entity.TypeOfQuestion;
import com.fdez_rumi_questions.app.service.QuestionService;

@RestController
@RequestMapping("/api/question")
public class QuestionRestController {

	@Autowired
	private QuestionService questionService;

	@GetMapping("/all")
	public ResponseEntity<List<Question>> getAllQuestions() {
		List<Question> questions = questionService.getAllQuestions();
		if (questions.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(questions);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
		Question question = questionService.getQuestionById(id);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(question);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Question> addQuestion(@RequestBody Question newQuestionData) {
		try {
			Question newQuestion = createQuestionFromData(newQuestionData);
			Question savedQuestion = questionService.createQuestion(newQuestion);
			return ResponseEntity.status(HttpStatus.CREATED).body(savedQuestion);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
	}
	
	@GetMapping("/categories")
	public ResponseEntity<Set<String>> getCategories() {
	    Set<String> categories = questionService.getAllCategories();
	    return ResponseEntity.ok(categories);
	}

	@PutMapping("/update/{id}")
	public ResponseEntity<Question> updateQuestion(@PathVariable Long id, @RequestBody Question updatedQuestion) {
		Question existingQuestion = questionService.getQuestionById(id);

		if (existingQuestion == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		existingQuestion.setCategory(updatedQuestion.getCategory());
		existingQuestion.setText(updatedQuestion.getText());

		if (existingQuestion instanceof MultipleChoiceQuestion && updatedQuestion instanceof MultipleChoiceQuestion) {			
			MultipleChoiceQuestion existingMultipleQuestion = (MultipleChoiceQuestion) existingQuestion;
			MultipleChoiceQuestion updatedMultipleQuestion = (MultipleChoiceQuestion) updatedQuestion;
			existingMultipleQuestion.setCorrectAnswers(updatedMultipleQuestion.getCorrectAnswers());
			existingMultipleQuestion.setFailAnswers(updatedMultipleQuestion.getFailAnswers());
		} else if (existingQuestion instanceof TrueFalseQuestion && updatedQuestion instanceof TrueFalseQuestion) {
			TrueFalseQuestion existingTrueOrFalseQuestion = (TrueFalseQuestion) existingQuestion;
			TrueFalseQuestion updatedTrueOrFalseQuestion = (TrueFalseQuestion) updatedQuestion;
			existingTrueOrFalseQuestion.setAnswer(updatedTrueOrFalseQuestion.getAnswer());
		}
		Question savedQuestion = questionService.createQuestion(existingQuestion);
		return ResponseEntity.ok(savedQuestion);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
		questionService.deleteQuestion(id);
		return ResponseEntity.noContent().build();
	}

	@PostMapping(value = "/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadQuestionsFromTheFormFile(@RequestParam("file") MultipartFile file) {
		try {
			String jsonContent = new String(file.getBytes());
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.readTree(jsonContent);

			questionService.proccessQuestionFromJson(jsonContent);
			return ResponseEntity.ok("Archivo JSON importado correctamente");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar JSON: " + e.getMessage());	        
		}
	}
	
	private Question createQuestionFromData(Question newQuestionData) {
		if (newQuestionData instanceof MultipleChoiceQuestion) {
	        return QuestionMaker.makeQuestion(
	                TypeOfQuestion.MULTIPLE_ANSWER,
	                newQuestionData.getCategory(),
	                newQuestionData.getText(),
	                ((MultipleChoiceQuestion) newQuestionData).getCorrectAnswers(),
	                ((MultipleChoiceQuestion) newQuestionData).getFailAnswers()
	        );
	    } else if (newQuestionData instanceof TrueFalseQuestion) {
	        return QuestionMaker.makeQuestion(
	                TypeOfQuestion.TRUE_FALSE,
	                newQuestionData.getCategory(),
	                newQuestionData.getText(),
	                ((TrueFalseQuestion) newQuestionData).getAnswer()
	        );
	    } else {
	        throw new IllegalArgumentException("Tipo de pregunta no soportado");
	    }
	}
}