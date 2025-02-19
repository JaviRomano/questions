package com.fdez_rumi_questions.app.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "true_or_false_question")
public class TrueFalseQuestion extends Question {	
	
	@Column(name = "answer")
	private Boolean answer;
	
	public TrueFalseQuestion() {}
	
	public TrueFalseQuestion(String question, String category, Boolean answer) {
		super(question, category);
		this.answer = answer;
	}	

	public Boolean getAnswer() {
		return answer;
	}

	public void setAnswer(Boolean answer) {
		this.answer = answer;
	}

	@Override
	public TypeOfQuestion getTypeOfQuestion() {
		return TypeOfQuestion.TRUE_FALSE;
	}
}