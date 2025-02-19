package com.fdez_rumi_questions.app.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "typeOfQuestion")
@JsonSubTypes({ @JsonSubTypes.Type(value = TrueFalseQuestion.class, name = "TRUE_FALSE_QUESTION"),
		@JsonSubTypes.Type(value = MultipleChoiceQuestion.class, name = "MULTIPLE_CHOICE_QUESTION"), })
public abstract class Question {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;

	@Column(name = "text", unique = true)
	private String text;

	@Column(name = "category")
	private String category;
	
	@Column(name = "active")
    private boolean active = true;

	public Question() {
	}

	public Question(String text, String category) {
		this.text = text;
		this.category = category;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public abstract TypeOfQuestion getTypeOfQuestion();
}
