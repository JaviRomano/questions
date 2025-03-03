package com.fdez_rumi_questions.app.service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdez_rumi_questions.app.entity.MultipleChoiceQuestion;
import com.fdez_rumi_questions.app.entity.Question;
import com.fdez_rumi_questions.app.entity.TrueFalseQuestion;
import com.fdez_rumi_questions.app.repository.QuestionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class QuestionService {

	@Autowired
	private QuestionRepository questionRepository;

	public void updateQuestion(Long id, Question updatedQuestion) {
		Question previousQuestion = questionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No existe registro con el ID[[" + id + "]]"));

		previousQuestion.setText(updatedQuestion.getText());
		previousQuestion.setCategory(updatedQuestion.getCategory());

		if (previousQuestion instanceof TrueFalseQuestion && updatedQuestion instanceof TrueFalseQuestion) {
			((TrueFalseQuestion) previousQuestion).setAnswer(((TrueFalseQuestion) updatedQuestion).getAnswer());
		} else if (previousQuestion instanceof MultipleChoiceQuestion
				&& updatedQuestion instanceof MultipleChoiceQuestion) {
			((MultipleChoiceQuestion) previousQuestion)
					.setCorrectAnswers(((MultipleChoiceQuestion) updatedQuestion).getCorrectAnswers());
			((MultipleChoiceQuestion) previousQuestion)
					.setFailAnswers(((MultipleChoiceQuestion) updatedQuestion).getFailAnswers());
		}
		questionRepository.save(previousQuestion);
	}

	public List<Question> getAllQuestions() {
		return questionRepository.findAll();
	}

	public Question getQuestionById(Long id) {
		return questionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("Pregunta con ID " + id + " no encontrada."));
	}

	public Question createQuestion(Question question) {
		return questionRepository.save(question);
	}
	
	public Set<String> getAllCategories() {
        return questionRepository.findAll()
                .stream()
                .map(Question::getCategory)
                .collect(Collectors.toSet());
    }

	public void deleteQuestion(Long id) {
		if (!questionRepository.existsById(id)) {
			throw new EntityNotFoundException("No existe pregunta con ID:" + id);
		}
		questionRepository.deleteById(id);
	}

	public void deleteAll() {
		questionRepository.deleteAll();
	}

	public void proccessQuestionFromJson(String jsonContent) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		List<Question> questions = objectMapper.readValue(jsonContent,
				objectMapper.getTypeFactory().constructCollectionType(List.class, Question.class));
		
		for (Question question : questions) {
			if (!questionRepository.existsByText(question.getText())) {
				questionRepository.save(question);
			}
		}		
	}

	public void desactivateById(Long id) {
		Question question = questionRepository.findById(id)
		        .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada " + id));
		question.setActive(false);
		questionRepository.save(question);
	}
	
	public void reactivateById(Long id) {
		Question question = questionRepository.findById(id)
		        .orElseThrow(() -> new EntityNotFoundException("Pregunta no encontrada " + id));
		question.setActive(true);
		questionRepository.save(question);
	}
}