package com.fdez_rumi_questions.app.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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

	public void offQuestionById(Long id) {
		Question question = questionRepository.findById(id)
				.orElseThrow(() -> new EntityNotFoundException("No existe la pregunta con id[" + id + "]."));
		question.setActive(false);
		questionRepository.save(question);
	}

	public Page<Question> getAllQuestionActivePageable() {
		return getAllQuestionActivePageable(0, 10);
	}

	public Page<Question> getAllQuestionActivePageable(int page, int size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Order.asc("id")));
		return questionRepository.findByActiveTrue(pageable);
	}

	public Page<Question> getAllQuestionActivePageable(String category, int page, int size) {
		PageRequest pageable = PageRequest.of(page, size);
		return questionRepository.findByCategoryAndActiveTrueOrderByIdAsc(category, pageable);
	}

	public List<Integer> getNumberOfPages(Page<Question> questionPage) {
		return IntStream.range(0, questionPage.getTotalPages()).boxed().collect(Collectors.toList());
	}

	public Question createQuestion(Question question) {
		return questionRepository.save(question);
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
}