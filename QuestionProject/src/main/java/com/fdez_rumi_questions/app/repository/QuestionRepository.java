package com.fdez_rumi_questions.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fdez_rumi_questions.app.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

	boolean existsByText(String text);	
}