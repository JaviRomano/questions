package com.fdez_rumi_questions.app.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class MultipleChoiceQuestion extends Question{
	
	@Column(name = "fail_answers")
	private List<String> failAnswers;
	
	@Column(name = "correct_answers")
    private List<String> correctAnswers;
	
	public MultipleChoiceQuestion() {}
	
	public MultipleChoiceQuestion(String question, String category, List<String> failAnswers, List<String> correctAnswers) {
		super(question, category);
		this.failAnswers = failAnswers;
		this.correctAnswers = correctAnswers;
	}

	public List<String> getFailAnswers() {
		return failAnswers;
	}

	public void setFailAnswers(List<String> failAnswers) {
		this.failAnswers = failAnswers;
	}

	public List<String> getCorrectAnswers() {
		return correctAnswers;
	}

	public void setCorrectAnswers(List<String> correctAnswers) {
		this.correctAnswers = correctAnswers;
	}

	@Override
	public TypeOfQuestion getTypeOfQuestion() {
		return TypeOfQuestion.MULTIPLE_ANSWER;
	}
	
}