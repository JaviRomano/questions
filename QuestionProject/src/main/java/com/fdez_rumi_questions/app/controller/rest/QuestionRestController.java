package com.fdez_rumi_questions.app.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

	private Question createQuestionFromData(Question newQuestionData) {
		if (TypeOfQuestion.MULTIPLE_ANSWER.equals(newQuestionData.getTypeOfQuestion())) {
			return QuestionMaker.makeQuestion(TypeOfQuestion.MULTIPLE_ANSWER, newQuestionData.getCategory(),
					newQuestionData.getText(), ((MultipleChoiceQuestion) newQuestionData).getCorrectAnswers(),
					((MultipleChoiceQuestion) newQuestionData).getFailAnswers());
		} else if (TypeOfQuestion.TRUE_FALSE.equals(newQuestionData.getTypeOfQuestion())) {
			return QuestionMaker.makeQuestion(TypeOfQuestion.TRUE_FALSE, newQuestionData.getCategory(),
					newQuestionData.getText(), ((TrueFalseQuestion) newQuestionData).getAnswer());
		} else {
			throw new IllegalArgumentException("Pregunta no soportada");
		}
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
	
	@GetMapping("{id:[0-9]+}")
	public ResponseEntity<Question> getQuestionById(@PathVariable Long id) {
		Question question = questionService.getQuestionById(id);
		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(question);
	}
	
	@PutMapping("{id:[0-9]+}")
	public ResponseEntity<Question> updateQuestionById(@PathVariable Long id, @RequestBody Question updatedQuestion) {
		Question question = questionService.getQuestionById(id);

		if (question == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		if (question instanceof MultipleChoiceQuestion && updatedQuestion instanceof MultipleChoiceQuestion) {
			MultipleChoiceQuestion multipleQuestion = (MultipleChoiceQuestion) question;
			MultipleChoiceQuestion updatedMultipleQuestion = (MultipleChoiceQuestion) updatedQuestion;
			multipleQuestion.setText(updatedMultipleQuestion.getText());
			multipleQuestion.setCorrectAnswers(updatedMultipleQuestion.getCorrectAnswers());
			multipleQuestion.setFailAnswers(updatedMultipleQuestion.getFailAnswers());
		} else if (question instanceof TrueFalseQuestion && updatedQuestion instanceof TrueFalseQuestion) {
			TrueFalseQuestion trueOrFalseQuestion = (TrueFalseQuestion) question;
			TrueFalseQuestion updatedTrueOrFalseQuestion = (TrueFalseQuestion) updatedQuestion;
			trueOrFalseQuestion.setText(updatedTrueOrFalseQuestion.getText());
			trueOrFalseQuestion.setAnswer(updatedTrueOrFalseQuestion.getAnswer());
		}
		Question savedQuestion = questionService.createQuestion(question);
		return ResponseEntity.ok(savedQuestion);
	}

}
