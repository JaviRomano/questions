package com.fdez_rumi_questions.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.fdez_rumi_questions.app.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Long>{

	Page <Question> findByActiveTrue(Pageable pageable);
	Page <Question> findByCategoryTrueOrderByIdAsc(String category, Pageable pageable);	
	
	//long CountByCategory(String category);
	boolean existsByText(String text);	
}